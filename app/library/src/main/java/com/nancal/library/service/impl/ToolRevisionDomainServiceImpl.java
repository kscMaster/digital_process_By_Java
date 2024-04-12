package com.nancal.library.service.impl;


import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.nancal.api.model.*;
import com.nancal.api.utils.DictUtil;
import com.nancal.api.utils.EntityUtil;
import com.nancal.auth.util.UserUtils;
import com.nancal.common.base.IdRequest;
import com.nancal.common.base.TableRequest;
import com.nancal.common.base.TableResponse;
import com.nancal.common.constants.DictConstant;
import com.nancal.common.enums.ErrorCode;
import com.nancal.common.enums.OperatorEnum;
import com.nancal.common.exception.ServiceException;
import com.nancal.common.utils.BeanUtil;
import com.nancal.common.utils.IdGeneratorUtil;
import com.nancal.common.utils.VersionUtil;
import com.nancal.library.service.IItemRevisionDomainServiceAdaptor;
import com.nancal.model.entity.*;
import com.nancal.service.bo.FileStorage;
import com.nancal.service.bo.IncludeRL;
import com.nancal.service.bo.ItemRevision;
import com.nancal.service.factory.IncludeRLFactory;
import com.nancal.service.factory.ItemRevisionFactory;
import com.nancal.service.factory.ObjectIndexFactory;
import com.nancal.service.service.IDatasetDomainService;
import com.nancal.service.service.IExtraPropertyDataDomainService;
import com.nancal.service.service.IToolRevisionDomainService;
import com.querydsl.core.types.Ops;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.apache.commons.lang3.tuple.Triple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class ToolRevisionDomainServiceImpl implements IToolRevisionDomainService, IItemRevisionDomainServiceAdaptor {


    @Autowired
    private IExtraPropertyDataDomainService iExtraPropertyDataDomainService;

    @Autowired
    private DictUtil dictUtil;

    /**
     * 基础库修改（不需要校验拥有者权限）
     * @param req
     * @author: 薛锦龙
     * @time: 2022/5/18
     * @return: {@link BusinessObjectResp}
     */
    @Override
    public BusinessObjectResp update(BusinessObjectReq req) {
        EntityManager entityManager = SpringUtil.getBean(EntityManager.class);
        ItemRevisionReq myReq = (ItemRevisionReq) req;
        String revision = EntityUtil.getRevision(StrUtil.blankToDefault(myReq.getObjectType(), EntityUtil.getObjectType()));
        // 根据uid获取数据库记录
        WorkspaceObjectEntity objectEntity = EntityUtil.getById(revision, req.getUid());
        if (ObjectUtil.isEmpty(objectEntity)) {
            throw new ServiceException(ErrorCode.E_12);
        }
        ItemRevisionEntity dbRevision = (ItemRevisionEntity) objectEntity;
        // 权限校验
        this.verifyAuthority(dbRevision, OperatorEnum.Write);
        ItemRevisionResp resp = ReflectUtil.newInstance(EntityUtil.getRespPackage(revision));
        // 比较对象是否存在修改
        if (myReq.equals(dbRevision)) {
            BeanUtil.copyPropertiesIgnoreNull(dbRevision, resp);
            resp.setLeftObject(myReq.getLeftObject());
            resp.setLeftObjectType(myReq.getLeftObjectType());
            return resp;
        }
        UserUtils userUtils = SpringUtil.getBean(UserUtils.class);
        // 针对新请求数据进行升版次，并保存
        ItemRevisionEntity newRevision = ReflectUtil.newInstance(EntityUtil.getEntityPackage(revision));
        BeanUtil.copyPropertiesIgnoreNull(dbRevision, newRevision);
        BeanUtil.copyPropertiesIgnoreNull(myReq, newRevision);
        newRevision.setUid(IdGeneratorUtil.generate());
        newRevision.setSequence(VersionUtil.createSequence(dbRevision.getSequence()));
        newRevision.setOwnerId(userUtils.getCurrentUserId());
        newRevision.setOwnerName(userUtils.getCurrentUserName());
        newRevision.setActive(true);
        newRevision.setObjectType(revision);
        entityManager.persist(newRevision);
        SpringUtil.getBean(ObjectIndexFactory.class).create(newRevision).saveOrUpdate();
        // 设置数据库对象作为老数据(老版次)
        dbRevision.setActive(false);
        entityManager.merge(dbRevision);
        // 建立新关系
        IncludeRL includeRL = SpringUtil.getBean(IncludeRLFactory.class).create();
        includeRL.saveRelation(myReq.getLeftObject(), myReq.getLeftObjectType(), newRevision.getUid(), revision);
        //判断是否上传了新的数据集
        QFileStorageEntity fileStorage = QFileStorageEntity.fileStorageEntity;
        ItemRevision itemRevision = SpringUtil.getBean(ItemRevisionFactory.class).create();
        JPAQueryFactory jpaQueryFactory = SpringUtil.getBean(JPAQueryFactory.class);
        DictUtil dictUtil = SpringUtil.getBean(DictUtil.class);
        //字典获取附件类型
        Map<String, String> dataSetType = dictUtil.getCodeValueMap("DataSetType");
        Set<Map.Entry<String, String>> entries = dataSetType.entrySet();
        if (CollectionUtil.isNotEmpty(myReq.getFiles())) {
            String filePath = myReq.getFiles().get(0).getFilePath();
            String name = filePath.substring(filePath.lastIndexOf("/") + 1);
            if (StrUtil.isBlank(myReq.getFiles().get(0).getUid())) {
                //获取相同版本系的版次
                List<WorkspaceObjectEntity> allSequences = itemRevision.getAllSequences(myReq.getUid(), EntityUtil.getObjectType());
                List<String> ids = allSequences.stream().map(WorkspaceObjectEntity::getUid).collect(Collectors.toList());
                //获取相同版本下所有的附件
                ItemRevision itemRevision2 = SpringUtil.getBean(ItemRevisionFactory.class).create();
                Map<String, List<FileStorage>> map = new HashMap<>();
                Map<String, String> idss = new HashMap<>();
                List<FileStorage> list = new ArrayList<>();
                ids.forEach(id -> {
                    List<FileStorage> file = itemRevision2.getFileStorage(id, EntityUtil.getObjectType());
                    list.addAll(file);
                    map.put(id, file);
                    file.forEach(data -> {
                        idss.put(data.getUid(), id);
                    });
                });

                if (CollectionUtil.isNotEmpty(list)) {
                    //遍历查看是否存在相同名称的附件
                    list.forEach(data -> {
                        //判断名称是否相同,相同删除关系
                        if ((data.getOriginFileName() + StrUtil.DOT + data.getType()).equals(name)) {
                            String id = idss.get(data.getUid());
                            //查找字典类型
                            List<String> rightObjectRelations = dictUtil.getRightObjectRelations(EntityUtil.getObjectType());
                            List<Triple<String, Ops, Object>> triples = new ArrayList<>();
                            triples.add(Triple.of(RelationEntity.LEFT_OBJECT, Ops.EQ, id));
                            triples.add(Triple.of(RelationEntity.LEFT_OBJECT_TYPE, Ops.EQ, EntityUtil.getObjectType()));
                            WorkspaceObjectEntity workspaceObject = EntityUtil.getDynamicQuery(rightObjectRelations.get(0), triples).fetchFirst();
                            if (ObjectUtil.isNotEmpty(workspaceObject)) {
                                workspaceObject.setDelFlag(true);
                                entityManager.merge(workspaceObject);
                            }
                        }
                    });
                }
            }
            String value = "";
            for (Map.Entry<String, String> entry : entries) {
                boolean contains = entry.getKey().contains(myReq.getFiles().get(0).getFileType());
                if (contains) {
                    value = entry.getValue();
                    break;
                }
            }
            //新增附件
            AttachmentReq attachmentReq = new AttachmentReq();
            attachmentReq.setObjectName(filePath.substring(filePath.lastIndexOf("/") + 1));
            attachmentReq.setFiles(myReq.getFiles());
            attachmentReq.setLeftObjectType(EntityUtil.getRevision(newRevision.getObjectType()));
            attachmentReq.setObjectType(value);
            attachmentReq.setLeftObject(newRevision.getUid());
            WorkspaceObjectResp save = SpringUtil.getBean(IDatasetDomainService.class).save(attachmentReq);
        }

        BeanUtil.copyPropertiesIgnoreNull(newRevision, resp);
        resp.setObjectName(newRevision.getObjectName());
        resp.setLeftObject(myReq.getLeftObject());
        resp.setLeftObjectType(myReq.getLeftObjectType());
        return resp;
    }

    @Override
    public BusinessObjectResp getObject(IdRequest id) {
        BusinessObjectResp resp = IItemRevisionDomainServiceAdaptor.super.getObject(id);
        if (Objects.isNull(resp)) {
            return null;
        }

        ItemRevisionResp workspaceObjectResp = (ItemRevisionResp) resp;
        workspaceObjectResp.setExtraPropertyDataRespList(iExtraPropertyDataDomainService.getList(workspaceObjectResp));
        ItemRevision revision = SpringUtil.getBean(ItemRevisionFactory.class).create();
        String objectType =Objects.isNull(id.getObjectType()) ? EntityUtil.getObjectType() : id.getObjectType();
        boolean permission = revision.editItemIdPermission(id.getUid(), objectType);
        workspaceObjectResp.setHasDrawingCode(permission);
        return workspaceObjectResp;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BusinessObjectResp updatePlus(BusinessObjectReq req) {
        LibraryAttributeToolReq toolReq = (LibraryAttributeToolReq)req;
        ToolRevisionReq toolRevisionReq = toolReq.getToolRevisionReq();
        BusinessObjectResp businessObjectResp = IToolRevisionDomainService.super.updatePlus(toolReq.getToolRevisionReq());
        List<ExtraPropertyDataReq> extraPropertyDataReqs = toolReq.getExtraPropertyDataReq();
        iExtraPropertyDataDomainService.update(extraPropertyDataReqs,toolRevisionReq.getLeftObject(),toolRevisionReq.getLeftObjectType());
        return businessObjectResp;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public WorkspaceObjectResp customUpgradeAndDataSet(LibraryAttributeToolReq req){
        WorkspaceObjectResp workspaceObject = IToolRevisionDomainService.super.upgradeAndDataSet(req.getToolRevisionReq());
        iExtraPropertyDataDomainService.upgradeRelation(req.getToolRevisionReq(),req.getExtraPropertyDataReq());
        return workspaceObject;
    }


    @Override
    public TableResponse<List<Map<String,Object>>> customPageLikeAndFiles(TableRequest<? extends ItemRevisionReq> req){
        ToolRevisionReq toolRevisionReq = (ToolRevisionReq)req.getData();
        List<ExtraPropertyDataReq> customFieldList = toolRevisionReq.getCustomFieldList();
        List<ExtraPropertyDataResp> likeList = iExtraPropertyDataDomainService.getLikeList(customFieldList, req.getData().getUid());
        TableResponse<WorkspaceObjectResp> response = new TableResponse<>();;
        if (!likeList.isEmpty()) {
            List<String> uids = likeList.stream().map(ExtraPropertyDataResp::getLeftObject).collect(Collectors.toList());
            List<Triple<String, Ops, Object>> triples = Arrays.asList(
                    Triple.of( ToolRevisionEntity.UID, Ops.IN, uids));
            response = IToolRevisionDomainService.super.pageLike(req, triples);
            return iExtraPropertyDataDomainService.getCustomObject(response,DictConstant.TOOL_EXTRADATA);
        }else {
            response = IToolRevisionDomainService.super.pageLike(req);
        }
        return iExtraPropertyDataDomainService.getCustomObject(response, DictConstant.MEASURE_EXTRADATA);
    }
}
