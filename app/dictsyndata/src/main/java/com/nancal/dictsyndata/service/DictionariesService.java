package com.nancal.dictsyndata.service;


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
import com.nancal.dictsyndata.model.SyncDictionariesRep;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author hewei
 * @date 2022/7/12 16:00
 * @Description
 */

public interface DictionariesService {

    /**
     * 同步接口
     * @return
     */
    String devSync(List<LezaoDictionariesTypeResp> insertDictTypeList,
                   List<LezaoDictionariesTypeResp> updateDictTypeList,
                   List<LezaoDictionariesTypeResp> deleteDictTypeList);

    /**
     * 同步接口
     * @return
     */
    String testSync(List<LezaoDictionariesTypeResp> insertDictTypeList,
                    List<LezaoDictionariesTypeResp> updateDictTypeList,
                    List<LezaoDictionariesTypeResp> deleteDictTypeList);




    /**
     * 查询213数据方法
     * @return
     */
    List<LezaoDictionariesTypeResp> getDict213Data();

    /**
     * 查询214数据方法
     * @return
     */
    List<LezaoDictionariesTypeResp> getDict214Data();

    /**
     * 计算同步数据
     * @param originalData 同步数据
     * @param targetData 被同步数据
     * @return
     */
    SyncDictionariesRep computeSync(List<LezaoDictionariesTypeResp> targetData, List<LezaoDictionariesTypeResp> originalData);




}
