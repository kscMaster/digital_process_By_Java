package com.nancal.service.bo;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.nancal.api.model.dataset.FileAttrReq;
import com.nancal.common.utils.BeanUtil;
import com.nancal.model.entity.DatasetFileRLEntity;
import com.nancal.model.entity.FileStorageEntity;
import com.nancal.model.entity.QDatasetFileRLEntity;
import com.nancal.model.entity.QFileStorageEntity;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;


@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Data
@ToString
@NoArgsConstructor
@ApiModel(value = "DatasetFileRL 数据集文件关系 的BO")
public class DatasetFileRL extends Relation {


    @Override
    public String getRelationType() {
        return "IncludeRL";
    }

    public List<FileStorage> getFileStorages(List<String> leftObjectUidList) {
        return getFileStorages(leftObjectUidList,null);
    }

    /***
     * 根据文件uid（左对象id）获取文件列表
     *
     * @param leftObjectUidList 左对象id
     * @author 徐鹏军
     * @date 2022/4/12 17:20
     * @return {@link List<  FileStorage >}
     */
    public List<FileStorage> getFileStorages(List<String> leftObjectUidList,String leftObjectType) {
        JPAQueryFactory jpaQueryFactory = SpringUtil.getBean(JPAQueryFactory.class);
        BooleanBuilder builder = new BooleanBuilder();
        QDatasetFileRLEntity datasetFileRL = QDatasetFileRLEntity.datasetFileRLEntity;
        builder.and(datasetFileRL.leftObject.in(leftObjectUidList));
        builder.and(datasetFileRL.delFlag.isFalse());
        if(!StrUtil.isBlank(leftObjectType)){
            builder.and(datasetFileRL.leftObjectType.in(leftObjectType));
        }
        List<DatasetFileRLEntity> rlList = jpaQueryFactory.selectFrom(datasetFileRL).where(builder).fetch();
        if (CollUtil.isEmpty(rlList)) {
            return Collections.emptyList();
        }
        // 获取所有文件uid
        List<String> rightUidList = rlList.stream().map(DatasetFileRLEntity::getRightObject).collect(Collectors.toList());
        QFileStorageEntity fileStorage = QFileStorageEntity.fileStorageEntity;
        builder = new BooleanBuilder();
        builder.and(fileStorage.uid.in(rightUidList));
        builder.and(fileStorage.delFlag.isFalse());
        List<FileStorageEntity> storageList = jpaQueryFactory.selectFrom(fileStorage).where(builder)
                .orderBy(new OrderSpecifier<>(Order.DESC,fileStorage.creationDate))
                .fetch();
        return Optional.ofNullable(storageList).orElse(Collections.emptyList()).stream().map(data -> {
            FileStorage storage = new FileStorage();
            BeanUtil.copyPropertiesIgnoreNull(data, storage);
            return storage;
        }).collect(Collectors.toList());
    }

    /***
     * 校验文件是否被修改
     *
     * @param uids 主键id
     * @param reqs 文件请求参数
     * @author 徐鹏军
     * @date 2022/4/20 13:55
     * @return {@link boolean} false 被修改了  true 未被修改
     */
    public boolean verifyFileStorageUpdate(List<String> uids, List<FileAttrReq> reqs) {
        JPAQueryFactory jpaQueryFactory = SpringUtil.getBean(JPAQueryFactory.class);
        // 判断文件是否被修改，如果被修改则更新文件表的修改时间。
        QFileStorageEntity fileStorage = QFileStorageEntity.fileStorageEntity;
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(fileStorage.uid.in(uids));
        List<FileStorageEntity> storageEntityList = jpaQueryFactory.selectFrom(fileStorage).where(builder).fetch();
        if (CollUtil.isEmpty(storageEntityList)) {
            return true;
        }
        Map<String, FileAttrReq> reqMap = reqs.stream().collect(Collectors.toMap(FileAttrReq::getFilePath, Function.identity(), (key1, key2) -> key2));
        return storageEntityList.stream().anyMatch(data -> {
            // 如果数据库中的文件，在请求过来的参数中，找不到，则说明被修改了
            if (!reqMap.containsKey(data.getFilePath())) {
                return false;
            }
            // 如果文件属性被修改
            FileAttrReq req = reqMap.get(data.getFilePath());
            return req.equals(data);
        });
    }
}
