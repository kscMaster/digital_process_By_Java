package com.nancal.service.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.nancal.api.model.*;
import com.nancal.api.model.WorkspaceObjectResp;
import com.nancal.api.model.dataset.FileAttrReq;
import com.nancal.api.model.dataset.FileAttrResp;
import com.nancal.api.utils.DictUtil;
import com.nancal.api.utils.EntityUtil;
import com.nancal.auth.util.UserUtils;
import com.nancal.common.base.IdRequest;
import com.nancal.common.enums.ErrorCode;
import com.nancal.common.enums.OperatorEnum;
import com.nancal.common.exception.ServiceException;
import com.nancal.common.utils.BeanUtil;
import com.nancal.common.utils.IdGeneratorUtil;
import com.nancal.model.entity.*;
import com.nancal.service.bo.IncludeRL;
import com.nancal.service.dao.DatasetFileRLEntityRepository;
import com.nancal.service.dao.FileStorageEntityRepository;
import com.nancal.service.factory.DatasetFileRLFactory;
import com.nancal.service.factory.IncludeRLFactory;
import com.nancal.service.factory.ObjectIndexFactory;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public interface IDatasetDomainService extends IWorkspaceObjectDomainService {

    Logger log = LoggerFactory.getLogger(IDatasetDomainService.class);

    @Transactional
    @Override
    default WorkspaceObjectResp save(BusinessObjectReq req) {
        DatasetReq myReq = (DatasetReq) req;
        UserUtils userUtils = SpringUtil.getBean(UserUtils.class);
        EntityManager entityManager = SpringUtil.getBean(EntityManager.class);
        String objectType = myReq.getObjectType();
        if (StrUtil.isBlank(myReq.getObjectType())){
            objectType = EntityUtil.getObjectType();
        }
        // 创建文件对象
        DatasetEntity dataset = ReflectUtil.newInstance(EntityUtil.getEntityPackage(objectType));
        dataset.setOwnerId(userUtils.getCurrentUserId());
        dataset.setOwnerName(userUtils.getCurrentUserName());
        dataset.setObjectName(myReq.getObjectName());
        dataset.setObjectDesc(myReq.getObjectDesc());
        dataset.setSecretLevel(myReq.getSecretLevel());
        dataset.setUid(IdGeneratorUtil.generate());
        dataset.setObjectType(objectType);
        entityManager.persist(dataset);
        // 保存对象索引数据
        SpringUtil.getBean(ObjectIndexFactory.class).create(dataset).saveOrUpdate();
        IncludeRL includeRL = SpringUtil.getBean(IncludeRLFactory.class).create();
        for (FileAttrReq file : myReq.getFiles()) {
            // 创建文件存储对象(fileStorage)
            FileStorageEntity fileStorage = new FileStorageEntity();
            fileStorage.setOriginFileName(FileNameUtil.mainName(file.getFilePath()));
            fileStorage.setFileExt(FileNameUtil.extName(file.getFilePath()));
            fileStorage.setFilePath(file.getFilePath());
            fileStorage.setType(file.getFileType());
            fileStorage.setOwnerId(dataset.getOwnerId());
            fileStorage.setOwnerName(dataset.getOwnerName());
            fileStorage.setSecretLevel(dataset.getSecretLevel());
            fileStorage.setUid(IdGeneratorUtil.generate());
            fileStorage.setFileSize(file.getFileSize().toString());
            fileStorage.setObjectType(new FileStorageReq().getObjectType());

            entityManager.persist(fileStorage);
            // 创建数据集关系对象(dataSetDFileRL),将PDF和fileStorage绑定起来
            includeRL.saveRelation(dataset.getUid(), objectType, fileStorage.getUid(), fileStorage.getObjectType());
        }
        // 绑定关系表名称
        includeRL.saveRelation(myReq.getLeftObject(), myReq.getLeftObjectType(), dataset.getUid(), objectType);
        // 创建响应对象返回前端
        DatasetResp resp = ReflectUtil.newInstance(EntityUtil.getRespPackage(dataset.getObjectType()));
        BeanUtil.copyPropertiesIgnoreNull(dataset, resp);
        resp.setObjectType(myReq.getObjectType());
        resp.setLeftObject(myReq.getUid());
        resp.setLeftObjectType(myReq.getObjectType());
        return resp;
    }

    @Transactional
    @Override
    default BusinessObjectResp update(BusinessObjectReq req) {
        UserUtils userUtils = SpringUtil.getBean(UserUtils.class);
        EntityManager entityManager = SpringUtil.getBean(EntityManager.class);
        DatasetFileRLEntityRepository datasetFileRLEntityRepository = SpringUtil.getBean(DatasetFileRLEntityRepository.class);
        FileStorageEntityRepository fileStorageEntityRepository = SpringUtil.getBean(FileStorageEntityRepository.class);
        DatasetReq myReq = (DatasetReq) req;
        String rightObjectType = StrUtil.blankToDefault(myReq.getObjectType(), EntityUtil.getObjectType());
        WorkspaceObjectEntity workspaceObject = EntityUtil.getById(rightObjectType, myReq.getUid());
        if (Objects.isNull(workspaceObject)) {
            throw new ServiceException(ErrorCode.E_12);
        }
        // 校验权限
        verifyAuthority(workspaceObject, OperatorEnum.Write);
        // 创建响应对象
        WorkspaceObjectResp resp = ReflectUtil.newInstance(EntityUtil.getRespPackage(rightObjectType));
        boolean isUpdate = myReq.equals(workspaceObject);
        workspaceObject.setObjectName(myReq.getObjectName());
        workspaceObject.setObjectDesc(myReq.getObjectDesc());
        workspaceObject.setSecretLevel(myReq.getSecretLevel());
        // 处理关联表,删除之前的文件，重新保存
        List<DatasetFileRLEntity> datasetFileRLs = datasetFileRLEntityRepository.findByLeftObject(myReq.getUid());
        if (CollUtil.isEmpty(datasetFileRLs)) {
            if (!isUpdate) {
                entityManager.merge(workspaceObject);
                // 保存对象索引数据
                SpringUtil.getBean(ObjectIndexFactory.class).create(workspaceObject).saveOrUpdate();
            }
            BeanUtil.copyPropertiesIgnoreNull(workspaceObject, resp);
            resp.setLeftObject(myReq.getLeftObject());
            resp.setLeftObjectType(myReq.getLeftObjectType());
            return resp;
        }
        // 删除文件存储
        List<String> rightUidList = datasetFileRLs.stream().map(DatasetFileRLEntity::getRightObject).collect(Collectors.toList());
        boolean isUpdateFile = SpringUtil.getBean(DatasetFileRLFactory.class).create().verifyFileStorageUpdate(rightUidList, myReq.getFiles());
        // 判断文件是否被修改,如果没被修改，直接返回
        if (isUpdateFile) {
            // 说明修改了文件的基本属性，未修改文件，此时需要更新文件的时间
            if (!isUpdate) {
                entityManager.merge(workspaceObject);
                // 保存对象索引数据
                SpringUtil.getBean(ObjectIndexFactory.class).create(workspaceObject).saveOrUpdate();
            }
            BeanUtil.copyPropertiesIgnoreNull(workspaceObject, resp);
            resp.setLeftObject(myReq.getLeftObject());
            resp.setLeftObjectType(myReq.getLeftObjectType());
            return resp;
        }
        fileStorageEntityRepository.deleteAllById(rightUidList);
        // 删除关系表数据
        List<String> uids = datasetFileRLs.stream().map(DatasetFileRLEntity::getUid).collect(Collectors.toList());
        datasetFileRLEntityRepository.deleteAllById(uids);
        // 更新文件
        workspaceObject.setLastUpdate(LocalDateTime.now());
        workspaceObject.setObjectType(myReq.getObjectType());
        entityManager.merge(workspaceObject);
        // 保存对象索引数据
        SpringUtil.getBean(ObjectIndexFactory.class).create(workspaceObject).saveOrUpdate();
        IncludeRL includeRL = SpringUtil.getBean(IncludeRLFactory.class).create();
        // 保存新文件
        for (FileAttrReq file : myReq.getFiles()) {
            // 创建文件存储对象(fileStorage)
            FileStorageEntity fileStorage = new FileStorageEntity();
            fileStorage.setOriginFileName(FileNameUtil.mainName(file.getFilePath()));
            fileStorage.setFileExt(FileNameUtil.extName(file.getFilePath()));
            fileStorage.setFilePath(file.getFilePath());
            fileStorage.setType(file.getFileType());
            fileStorage.setOwnerId(userUtils.getCurrentUserId());
            fileStorage.setOwnerName(userUtils.getCurrentUserName());
            fileStorage.setSecretLevel(workspaceObject.getSecretLevel());
            fileStorage.setUid(IdGeneratorUtil.generate());
            fileStorage.setFileSize(file.getFileSize().toString());
            entityManager.persist(fileStorage);
            // 创建数据集关系对象,将文件和fileStorage绑定起来
            includeRL.saveRelation(myReq.getUid(), rightObjectType, fileStorage.getUid(), fileStorage.getObjectType());
        }
        BeanUtil.copyPropertiesIgnoreNull(workspaceObject, resp);
        resp.setLeftObject(myReq.getLeftObject());
        resp.setLeftObjectType(myReq.getLeftObjectType());
        return resp;
    }

    @Override
    default BusinessObjectResp getObject(IdRequest id) {
        JPAQueryFactory jpaQueryFactory = SpringUtil.getBean(JPAQueryFactory.class);
        DictUtil dictUtil = SpringUtil.getBean(DictUtil.class);
        String objectType = StrUtil.blankToDefault(id.getObjectType(),EntityUtil.getObjectType());
        // 查询文件数据
        WorkspaceObjectEntity workspaceObjectEntity = EntityUtil.getById(objectType, id.getUid());
        if (Objects.isNull(workspaceObjectEntity)) {
            return null;
        }
        // 查询文件关系表
        BooleanBuilder builder = new BooleanBuilder();
        QDatasetFileRLEntity datasetFileRL = QDatasetFileRLEntity.datasetFileRLEntity;
        builder.and(datasetFileRL.delFlag.isFalse());
        builder.and(datasetFileRL.leftObject.eq (id.getUid()));
        builder.and(datasetFileRL.leftObjectType.eq(objectType));
        List<DatasetFileRLEntity> entities = jpaQueryFactory.selectFrom(datasetFileRL).where(builder).fetch();
        if (CollUtil.isEmpty(entities)) {
            return null;
        }
        // 获取右对象id
        List<String> rightUidList = entities.stream().map(DatasetFileRLEntity::getRightObject).collect(Collectors.toList());
        QFileStorageEntity fileStorage = QFileStorageEntity.fileStorageEntity;
        builder = new BooleanBuilder();
        builder.and(fileStorage.delFlag.isFalse());
        builder.and(fileStorage.uid.in(rightUidList));
        List<FileStorageEntity> entityList = jpaQueryFactory.selectFrom(fileStorage).where(builder).fetch();
        if (CollUtil.isEmpty(entityList)) {
            return null;
        }
        DatasetResp resp = ReflectUtil.newInstance(EntityUtil.getRespPackage(objectType));
        BeanUtil.copyPropertiesIgnoreNull(workspaceObjectEntity, resp);
        List<FileAttrResp> attrRespList = entityList.stream().map(data -> {
            FileAttrResp attrResp = new FileAttrResp();
            BeanUtil.copyPropertiesIgnoreNull(data, attrResp);
            return attrResp;
        }).collect(Collectors.toList());
        resp.setFiles(attrRespList);
        dictUtil.translate(resp);
        return resp;
    }

}
