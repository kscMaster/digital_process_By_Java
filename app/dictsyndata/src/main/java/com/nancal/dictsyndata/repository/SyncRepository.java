package com.nancal.dictsyndata.repository;

import cn.hutool.extra.spring.SpringUtil;
import com.nancal.auth.util.UserUtils;
import com.nancal.common.utils.BeanUtil;
import com.nancal.common.utils.IdGeneratorUtil;
import com.nancal.dictsyndata.entity.LezaoDictionariesEntity;
import com.nancal.dictsyndata.entity.LezaoDictionariesTypeEntity;
import com.nancal.dictsyndata.entity.QLezaoDictionariesEntity;
import com.nancal.dictsyndata.entity.QLezaoDictionariesTypeEntity;
import com.nancal.dictsyndata.model.LezaoDictionariesResp;
import com.nancal.dictsyndata.model.LezaoDictionariesTypeResp;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author hewei
 * @date 2022/7/20 11:39
 * @Description
 */
@Repository
public class SyncRepository {

    /**
     * 统一删除同步方法
     * @param dictionariesTypeReps
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteSync(List<LezaoDictionariesTypeResp> dictionariesTypeReps){
        EntityManager entityManager = SpringUtil.getBean(EntityManager.class);
        dictionariesTypeReps.forEach(dictionariesType ->{
            // 字典类型表
            LezaoDictionariesTypeEntity lezaoDictionariesTypeEntity = new LezaoDictionariesTypeEntity();
            BeanUtil.copyPropertiesIgnoreNull(dictionariesType,lezaoDictionariesTypeEntity);
            lezaoDictionariesTypeEntity.setDelFlag(1);
            entityManager.merge(lezaoDictionariesTypeEntity);
        });

    }

    /**
     * 统一新增同步方法
     * @param dictionariesTypeReps
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateSync(List<LezaoDictionariesTypeResp> dictionariesTypeReps,String tenantId){
        EntityManager entityManager = SpringUtil.getBean(EntityManager.class);
        dictionariesTypeReps.forEach(dictionariesType ->{
            // 字典类型表
            LezaoDictionariesTypeEntity lezaoDictionariesTypeEntity = new LezaoDictionariesTypeEntity();
            BeanUtil.copyPropertiesIgnoreNull(dictionariesType,lezaoDictionariesTypeEntity);
            lezaoDictionariesTypeEntity.setTenantId(tenantId);
            entityManager.merge(createDictType(lezaoDictionariesTypeEntity));
            // 字典表
            if (null != dictionariesType.getInsertChildren()) {
                dictionariesType.getInsertChildren().forEach(dictionaries -> {
                    LezaoDictionariesEntity lezaoDictionariesEntity = new LezaoDictionariesEntity();
                    BeanUtil.copyPropertiesIgnoreNull(dictionaries, lezaoDictionariesEntity);
                    lezaoDictionariesEntity.setTenantId(tenantId);
                    lezaoDictionariesEntity.setId(Long.parseLong(IdGeneratorUtil.generate()));
                    entityManager.persist(createDict(lezaoDictionariesEntity));
                });
            }
            if (null != dictionariesType.getUpdateChildren()) {
                dictionariesType.getUpdateChildren().forEach(dictionaries -> {
                    LezaoDictionariesEntity lezaoDictionariesEntity = new LezaoDictionariesEntity();
                    BeanUtil.copyPropertiesIgnoreNull(dictionaries, lezaoDictionariesEntity);
                    lezaoDictionariesEntity.setTenantId(tenantId);
                    entityManager.merge(createDict(lezaoDictionariesEntity));
                });
            }
            if (null != dictionariesType.getDeleteChildren()) {
                dictionariesType.getDeleteChildren().forEach(dictionaries -> {
                    LezaoDictionariesEntity lezaoDictionariesEntity = new LezaoDictionariesEntity();
                    BeanUtil.copyPropertiesIgnoreNull(dictionaries, lezaoDictionariesEntity);
                    lezaoDictionariesEntity.setDelFlag(1);
                    entityManager.merge(lezaoDictionariesEntity);
                });
            }
        });

    }


    /**
     * 统一新增同步方法
     * @param dictionariesTypeReps
     * @param tenantId 租户id
     */
    @Transactional(rollbackFor = Exception.class)
    public void insertSync(List<LezaoDictionariesTypeResp> dictionariesTypeReps,String tenantId){
        EntityManager entityManager = SpringUtil.getBean(EntityManager.class);
        dictionariesTypeReps.forEach(dictionariesType ->{
            // 字典类型表
            LezaoDictionariesTypeEntity lezaoDictionariesTypeEntity = new LezaoDictionariesTypeEntity();
            BeanUtil.copyPropertiesIgnoreNull(dictionariesType,lezaoDictionariesTypeEntity);
            lezaoDictionariesTypeEntity.setId(Long.parseLong(IdGeneratorUtil.generate()));
            lezaoDictionariesTypeEntity.setTenantId(tenantId);
            entityManager.persist(createDictType(lezaoDictionariesTypeEntity));
            // 字典表
            if (null != dictionariesType.getChildren()) {
                dictionariesType.getChildren().forEach(dictionaries -> {
                    LezaoDictionariesEntity lezaoDictionariesEntity = new LezaoDictionariesEntity();
                    BeanUtil.copyPropertiesIgnoreNull(dictionaries, lezaoDictionariesEntity);
                    lezaoDictionariesEntity.setId(Long.parseLong(IdGeneratorUtil.generate()));
                    lezaoDictionariesEntity.setTenantId(tenantId);
                    entityManager.persist(createDict(lezaoDictionariesEntity));
                });
            }
        });
    }

    private LezaoDictionariesTypeEntity createDictType(LezaoDictionariesTypeEntity dictTypeEntity){
        UserUtils userUtils = SpringUtil.getBean(UserUtils.class);
        dictTypeEntity.setDelFlag(0);
        dictTypeEntity.setUpdateTime(LocalDateTime.now());
//        dictTypeEntity.setLastUpdateUsername(userUtils.getCurrentLoginName());
//        dictTypeEntity.setLastUpdateUserId(userUtils.getCurrentUserId());
        dictTypeEntity.setCreateTime(LocalDateTime.now());
//        dictTypeEntity.setCreationUserId(userUtils.getCurrentUserId());
//        dictTypeEntity.setCreationUsername(userUtils.getCurrentLoginName());
        return dictTypeEntity;
    }

    private LezaoDictionariesEntity createDict(LezaoDictionariesEntity dictTypeEntity){
        UserUtils userUtils = SpringUtil.getBean(UserUtils.class);
        dictTypeEntity.setDelFlag(0);
        dictTypeEntity.setUpdateTime(LocalDateTime.now());
//        dictTypeEntity.setLastUpdateUsername(userUtils.getCurrentLoginName());
//        dictTypeEntity.setLastUpdateUserId(userUtils.getCurrentUserId());
        dictTypeEntity.setCreateTime(LocalDateTime.now());
//        dictTypeEntity.setCreationUserId(userUtils.getCurrentUserId());
//        dictTypeEntity.setCreationUsername(userUtils.getCurrentLoginName());
        return dictTypeEntity;
    }



    /**
     * 统一查询接口
     * @param tenantId
     * @return
     */
    public List<LezaoDictionariesTypeResp> getDictionariesDataList(String tenantId) {
        /**
         * 获取乐造平台-字典类型表数据集合
         */
        QLezaoDictionariesTypeEntity qLezaoDictionariesTypeEntity = QLezaoDictionariesTypeEntity.lezaoDictionariesTypeEntity;
        BooleanBuilder dictTypeBuilder = new BooleanBuilder();
        dictTypeBuilder.and(qLezaoDictionariesTypeEntity.tenantId.eq(tenantId)).and(qLezaoDictionariesTypeEntity.delFlag.eq(0));
        JPAQueryFactory query = SpringUtil.getBean(JPAQueryFactory.class);
        List<LezaoDictionariesTypeEntity> dictionariesTypeEntityList = query.selectFrom(qLezaoDictionariesTypeEntity).where(dictTypeBuilder).fetch();
        /**
         * 获取乐造平台-字典表数据集合
         */
        QLezaoDictionariesEntity qLezaoDictionariesEntity = QLezaoDictionariesEntity.lezaoDictionariesEntity;
        BooleanBuilder dictBuilder = new BooleanBuilder();
        dictBuilder.and(qLezaoDictionariesEntity.tenantId.eq(tenantId)).and(qLezaoDictionariesEntity.delFlag.eq(0));
        List<LezaoDictionariesEntity> dictionariesEntityList = query.selectFrom(qLezaoDictionariesEntity).where(dictBuilder).fetch();

        List<LezaoDictionariesTypeResp> lezaoDictionariesTypeResps = new LinkedList<>();
        dictionariesTypeEntityList.forEach( dictType ->{
            LezaoDictionariesTypeResp lezaoDictionariesTypeResp = new LezaoDictionariesTypeResp();
            BeanUtil.copyPropertiesIgnoreNull(dictType,lezaoDictionariesTypeResp);
            if (!dictionariesEntityList.isEmpty()) {
                List<LezaoDictionariesResp> collect = dictionariesEntityList.stream().filter(dictionariesEntity -> null != dictionariesEntity.getDictTypeId() && dictionariesEntity.getDictTypeId().equals(dictType.getId())).map(dict -> {
                    LezaoDictionariesResp lezaoDictionariesResp = new LezaoDictionariesResp();
                    BeanUtil.copyPropertiesIgnoreNull(dict, lezaoDictionariesResp);
                    return lezaoDictionariesResp;
                }).collect(Collectors.toList());
                lezaoDictionariesTypeResp.setChildren(collect);
            }
            lezaoDictionariesTypeResps.add(lezaoDictionariesTypeResp);
        });
        return new ArrayList<>(lezaoDictionariesTypeResps);
    }
}
