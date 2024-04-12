package com.nancal.service.service;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.fastjson.JSON;
import com.nancal.api.model.BusinessObjectResp;
import com.nancal.api.model.LibraryFolderResp;
import com.nancal.api.model.LibraryResp;
import com.nancal.api.utils.DictUtil;
import com.nancal.api.utils.EntityUtil;
import com.nancal.common.base.IdRequest;
import com.nancal.common.enums.ErrorCode;
import com.nancal.common.enums.LibraryEnum;
import com.nancal.common.enums.OperatorEnum;
import com.nancal.common.exception.ServiceException;
import com.nancal.model.entity.*;
import com.nancal.remote.to.MoreDictEntryReq;
import com.nancal.remote.vo.MoreDictEntryGroupVo;
import com.nancal.remote.vo.MoreDictEntryVo;
import com.nancal.service.dao.IncludeRLEntityRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.stream.Collectors;

public interface ILibraryFolderDomainService extends IFolderDomainService {


    /**
     * 通过 uid 查询所以最下级Uid
     * @param uid
     * @return
     */
    default List<String> treeLibrary(String uid){
        List<String> respList = new ArrayList<>();
        if (!hasChildren(uid) && StringUtils.isNotBlank(uid)){
            respList.add(uid);
        }
        JPAQueryFactory jpaQueryFactory = SpringUtil.getBean(JPAQueryFactory.class);
        //通过类型获取所有数据
        QLibraryFolderEntity qLibraryFolderEntity = QLibraryFolderEntity.libraryFolderEntity;
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qLibraryFolderEntity.delFlag.isFalse());
        builder.and(qLibraryFolderEntity.libraryType.in(LibraryEnum.GTE4INSPECTION.getCode()));
        List<LibraryFolderEntity> libraryFolderEntities = jpaQueryFactory.selectFrom(qLibraryFolderEntity).where(builder).fetch();
        if (CollUtil.isEmpty(libraryFolderEntities)) {
            return Collections.emptyList();
        }
        //获取右对象所有的uid
        List<String> uids = libraryFolderEntities.stream().map(LibraryFolderEntity::getUid).collect(Collectors.toList());
        //查询中间表找到对应关系
        QIncludeRLEntity includeRL = QIncludeRLEntity.includeRLEntity;
        BooleanBuilder where = new BooleanBuilder();
        where.and(includeRL.delFlag.isFalse());
        where.and(includeRL.rightObject.in(uids));
        List<IncludeRLEntity> itemList = jpaQueryFactory.selectFrom(includeRL).where(where).fetch();
        if (CollUtil.isEmpty(itemList)) {
            return Collections.emptyList();
        }
        recursionLast(itemList,uid,respList);
        return respList;
    }

    default void recursionLast(List<IncludeRLEntity> itemList,String uid,List<String> respList){
        List<IncludeRLEntity> collectList;
        if (StringUtils.isNotBlank(uid)) {
             collectList = itemList.stream().filter(includeRLEntity -> includeRLEntity.getLeftObject().equals(uid)).collect(Collectors.toList());
        }else {
             collectList = itemList.stream().filter(includeRLEntity -> includeRLEntity.getLeftObject().equals("0")).collect(Collectors.toList());
        }
        collectList.forEach(includeRLEntity -> {
            List<IncludeRLEntity> lastChildren = itemList.stream().filter(last -> last.getLeftObject().equals(includeRLEntity.getRightObject())).collect(Collectors.toList());
            if (lastChildren.isEmpty()){
                respList.add(includeRLEntity.getRightObject());
            }else {
                recursionLast(itemList, includeRLEntity.getRightObject(), respList);
            }
        });
    }


    /**
     * 通过 uid 查询所以节点名称
     * @param uid
     * @return
     */
    default String treeLibraryName(String uid,String fileName){
        if (!StringUtils.isNotBlank(uid)){
            return "项目检验";
        }
        JPAQueryFactory jpaQueryFactory = SpringUtil.getBean(JPAQueryFactory.class);
        //通过类型获取所有数据
        QLibraryFolderEntity qLibraryFolderEntity = QLibraryFolderEntity.libraryFolderEntity;
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qLibraryFolderEntity.delFlag.isFalse());
        builder.and(qLibraryFolderEntity.libraryType.in(LibraryEnum.GTE4INSPECTION.getCode()));
        List<LibraryFolderEntity> libraryFolderEntities = jpaQueryFactory.selectFrom(qLibraryFolderEntity).where(builder).fetch();
        if (CollUtil.isEmpty(libraryFolderEntities)) {
            return "";
        }
        //获取右对象所有的uid
        List<String> uids = libraryFolderEntities.stream().map(LibraryFolderEntity::getUid).collect(Collectors.toList());
        //查询中间表找到对应关系
        QIncludeRLEntity includeRL = QIncludeRLEntity.includeRLEntity;
        BooleanBuilder where = new BooleanBuilder();
        where.and(includeRL.delFlag.isFalse());
        where.and(includeRL.rightObject.in(uids));
        List<IncludeRLEntity> itemList = jpaQueryFactory.selectFrom(includeRL).where(where).fetch();
        if (CollUtil.isEmpty(itemList)) {
            return "";
        }
        //将检验项目库对象转换成map（uid为key）
        Map<String, LibraryResp> dataMap = libraryFolderEntities.stream().map(library -> {
            LibraryResp resp = new LibraryResp();
            BeanUtil.copyProperties(library, resp);
            return resp;
        }).collect(Collectors.toMap(LibraryResp::getUid, Function.identity()));
        List<IncludeRLEntity> ires = itemList.stream().filter(item -> item.getRightObject().equals(uid)).collect(Collectors.toList());
        AtomicReference<String> name = new AtomicReference<>();
        name.set(fileName);
        ires.forEach(ire->{
            recursionName(ire.getRightObject(), name, dataMap, itemList);
        });
        return name.get();
    }

    default AtomicReference<String> recursionName(String uid,AtomicReference<String> fileName, Map<String, LibraryResp> dataMap,List<IncludeRLEntity> itemList){
        final List<IncludeRLEntity> collect = itemList.stream().filter(item -> item.getRightObject().equals(uid)).collect(Collectors.toList());
        if (!collect.isEmpty() && !collect.get(0).getLeftObject().equals(LibraryEnum.ZERO.getName())){
            final IncludeRLEntity includeRLEntity = collect.get(0);
            LibraryResp libraryResp = dataMap.get(includeRLEntity.getLeftObject());
            fileName.set(libraryResp.getObjectName()+"-"+fileName.get());
            recursionName(includeRLEntity.getLeftObject(),fileName,dataMap,itemList);
        }
        return fileName;
    }


    
    /**
     * 获取库文件第一层
     * @param
     * @author: 薛锦龙
     * @time: 2022/4/27
     * @return: {@link List< LibraryResp>}
     */
    List<LibraryResp> treeLibrary();

    /**
     * 获取基础库树结构
     * @param
     * @author: 薛锦龙
     * @time: 2022/4/26
     * @return: {@link List<  LibraryFolderResp >}
     */
    default List<LibraryResp> tree(List<String> type){
        JPAQueryFactory jpaQueryFactory = SpringUtil.getBean(JPAQueryFactory.class);
        //通过类型获取所有数据
        QLibraryFolderEntity qLibraryFolderEntity = QLibraryFolderEntity.libraryFolderEntity;
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qLibraryFolderEntity.delFlag.isFalse());
        builder.and(qLibraryFolderEntity.libraryType.in(type));
        List<LibraryFolderEntity> libraryFolderEntities = jpaQueryFactory.selectFrom(qLibraryFolderEntity).where(builder).fetch();
        List<String> appCodes = new ArrayList<>(3);
        appCodes.add("lz624-library");
        libraryFolderEntities.forEach(libraryFolderEntity -> {
            MoreDictEntryReq moreDictEntryReq = new MoreDictEntryReq();
            List<String> dictTypes = new ArrayList<>();
            DictUtil dictUtil = SpringUtil.getBean(DictUtil.class);
            moreDictEntryReq.setAppCodes(appCodes);
            dictTypes.add(libraryFolderEntity.getLibraryType()+"_ExtraData");
            moreDictEntryReq.setDictTypes(dictTypes);
            List<MoreDictEntryGroupVo> codeValueList = dictUtil.getMoreDictEntryVo(moreDictEntryReq);
            if (!codeValueList.isEmpty()){
                List<MoreDictEntryVo> dictList = codeValueList.get(0).getDictList();
                List<MoreDictEntryVo> moreDictEntryVos = JSON.parseArray(libraryFolderEntity.getColumnKey(), MoreDictEntryVo.class);
                dictList.forEach(dict ->{
                    if (StringUtils.isNotBlank(dict.getParentId())) {
                        boolean isExistParent = dictList.stream().anyMatch(t -> StringUtils.isNotBlank(dict.getParentId()) && t.getId().equals(dict.getParentId()));
                        if (isExistParent) {
                            moreDictEntryVos.add(dict);
                        }
                    }
                });
                libraryFolderEntity.setColumnKey(JSON.toJSONString(moreDictEntryVos));
            }
        });
        if (CollUtil.isEmpty(libraryFolderEntities)) {
            return Collections.emptyList();
        }
        //获取右对象所有的uid
        List<String> uids = libraryFolderEntities.stream().map(LibraryFolderEntity::getUid).collect(Collectors.toList());
        //查询中间表找到对应关系
        QIncludeRLEntity includeRL = QIncludeRLEntity.includeRLEntity;
        BooleanBuilder where = new BooleanBuilder();
        where.and(includeRL.delFlag.isFalse());
        where.and(includeRL.rightObject.in(uids));
        List<IncludeRLEntity> itemList = jpaQueryFactory.selectFrom(includeRL).where(where).fetch();
        if (CollUtil.isEmpty(itemList)) {
            return Collections.emptyList();
        }
        //将检验项目库对象转换成map（uid为key）
        Map<String, LibraryResp> dataMap = libraryFolderEntities.stream().map(library -> {
            LibraryResp resp = new LibraryResp();
            BeanUtil.copyProperties(library, resp);
            return resp;
        }).collect(Collectors.toMap(LibraryResp::getUid, Function.identity()));
        List<LibraryResp> respList = new ArrayList<>();

        // 组装树
        itemList.stream().filter(data -> dataMap.containsKey(data.getRightObject())).forEach(data -> {
            // 获取当前节点
            LibraryResp resp = dataMap.get(data.getRightObject());
            // 判断是否根节点
            if (data.getLeftObject().equals(LibraryEnum.ZERO.getName())) {
                respList.add(resp);
                return;
            }
            // 获取父节点
            LibraryResp parent = dataMap.get(data.getLeftObject());
            if (CollUtil.isEmpty(parent.getChildren())) {
                parent.setChildren(new ArrayList<>());
            }
            parent.getChildren().add(resp);
        });
        // 计算每个节点的数量
        respList.forEach(this::sumCount);
        return respList;
    }

    /**
     *  统计节点的数量
     * @param node
     * @author: 薛锦龙
     * @time: 2022/4/27
     * @return: {@link Integer}
     */
    default LibraryResp sumCount(LibraryResp node){
        List<LibraryResp> children = node.getChildren();
        Integer count = node.getQuantity();
        if(!CollectionUtils.isEmpty(children)) {
            for (LibraryResp subNode : children) {
                sumCount(subNode);
                if (BeanUtil.isEmpty(subNode.getQuantity())){
                    continue;
                }
                count = count+ subNode.getQuantity();
            }
        }
        node.setQuantity(count);
        return node;
    }

    /**
     * 左侧树移除
     * @param id
     * @author: 薛锦龙
     * @time: 2022/4/26
     * @return: {@link BusinessObjectResp}
     */
    @Transactional
    default BusinessObjectResp deleteObject(IdRequest id) {
        String uid = id.getUid();
        WorkspaceObjectEntity entity = EntityUtil.getById(EntityUtil.getObjectType(),uid);
        if (Objects.isNull(entity)) {
            throw new ServiceException(ErrorCode.E_12);
        }
        // 校验权限
        this.verifyAuthority(entity, OperatorEnum.Delete);

        if (hasChildren(uid)){
            throw new ServiceException(ErrorCode.E_10,"此库存在子库，请先删除子库");
        }
        if (libraryAssociation(uid)){
            throw new ServiceException(ErrorCode.E_10,"此库存在数据，请先删除数据");
        }
        JPAQueryFactory jpaQueryFactory = SpringUtil.getBean(JPAQueryFactory.class);
        EntityManager entityManager = SpringUtil.getBean(EntityManager.class);
        IncludeRLEntityRepository includeRLEntityRepository = SpringUtil.getBean(IncludeRLEntityRepository.class);
        //查看是否存下父节点
        QIncludeRLEntity includeRLEntity = QIncludeRLEntity.includeRLEntity;
        BooleanBuilder where = new BooleanBuilder();
        where.and(includeRLEntity.delFlag.isFalse());
        where.and(includeRLEntity.rightObject.eq(uid));
        IncludeRLEntity fetchFirst = (IncludeRLEntity) jpaQueryFactory.from(includeRLEntity).where(where).fetchFirst();
        if (BeanUtil.isNotEmpty(fetchFirst)){
            //删除库文件夹
            if (Objects.nonNull(entity)) {
                entity.setDelFlag(true);
                entityManager.merge(entity);
            }
        }
        //删除关系
        includeRLEntityRepository.delete(fetchFirst);
        return null;
    }

    /**
     * 查看是否有子集
     * @param uid
     * @author: 薛锦龙
     * @time: 2022/4/26
     * @return: {@link Boolean}
     */
    default boolean hasChildren(String uid){
        JPAQueryFactory jpaQueryFactory = SpringUtil.getBean(JPAQueryFactory.class);
        QIncludeRLEntity qIncludeRLEntity = QIncludeRLEntity.includeRLEntity;
        BooleanBuilder where = new BooleanBuilder();
        where.and(qIncludeRLEntity.delFlag.isFalse());
        where.and(qIncludeRLEntity.leftObject.eq(uid));
        long count = jpaQueryFactory.selectFrom(qIncludeRLEntity).where(where).fetchCount();
        if (count>0){
            return true;
        }
        return  false;
    }
    /**
     * 查询是否存在库关联关系
     * @param uid
     * @author: 薛锦龙
     * @time: 2022/4/26
     * @return: {@link Boolean}
     */
    default boolean libraryAssociation(String uid){
        JPAQueryFactory jpaQueryFactory = SpringUtil.getBean(JPAQueryFactory.class);
        QLibraryFolderRLEntity libraryIncludeRLEntity = QLibraryFolderRLEntity.libraryFolderRLEntity;
        BooleanBuilder where = new BooleanBuilder();
        where.and(libraryIncludeRLEntity.delFlag.isFalse());
        where.and(libraryIncludeRLEntity.leftObject.eq(uid));
        long count = jpaQueryFactory.selectFrom(libraryIncludeRLEntity).where(where).fetchCount();
        if (count>0){
            return true;
        }
        return false;
    }

    default LibraryResp getLibraryObject(IdRequest id) {
        String uid = id.getUid();
        //查询中间表找到左对象
        JPAQueryFactory jpaQueryFactory = SpringUtil.getBean(JPAQueryFactory.class);
        QIncludeRLEntity qIncludeRLEntity = QIncludeRLEntity.includeRLEntity;
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qIncludeRLEntity.delFlag.isFalse());
        builder.and(qIncludeRLEntity.rightObject.eq(uid));
        IncludeRLEntity includeRLEntity = jpaQueryFactory.selectFrom(qIncludeRLEntity).where(builder).fetchFirst();
        if (ObjectUtil.isEmpty(includeRLEntity)){
            throw new ServiceException(ErrorCode.E_12,"数据不存在");
        }
        List<String> uids = new ArrayList<>();
        uids.add(includeRLEntity.getLeftObject());
        uids.add(includeRLEntity.getRightObject());
        //查找具体数据
        QLibraryFolderEntity libraryFolderEntity = QLibraryFolderEntity.libraryFolderEntity;
        BooleanBuilder where = new BooleanBuilder();
        where.and(libraryFolderEntity.delFlag.isFalse());
        where.and(libraryFolderEntity.uid.in(uids));
        List<LibraryFolderEntity> fetch = jpaQueryFactory.selectFrom(libraryFolderEntity).where(where).fetch();
        if (CollectionUtils.isEmpty(fetch)){
            throw new ServiceException(ErrorCode.E_12,"数据不存在");
        }
        Map<String, LibraryFolderEntity> collect = fetch.stream().collect(Collectors.toMap(LibraryFolderEntity::getUid, Function.identity()));
        LibraryFolderEntity libraryFolderEntity1 = collect.get(uids.get(uids.size()-1));
        LibraryResp libraryResp = new LibraryResp();
        com.nancal.common.utils.BeanUtil.copyPropertiesIgnoreNull(libraryFolderEntity1,libraryResp);
        if (!includeRLEntity.getLeftObject().equals(LibraryEnum.ZERO.getName())){
            libraryResp.setParentName(collect.get(uids.get(collect.size()-2)).getObjectName());
        }
        return libraryResp;
    }
}
