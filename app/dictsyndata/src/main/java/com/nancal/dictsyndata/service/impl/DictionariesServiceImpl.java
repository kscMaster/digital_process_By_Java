package com.nancal.dictsyndata.service.impl;

import com.nancal.dictsyndata.config.DataSourceSwitch;
import com.nancal.dictsyndata.config.DataSourceType;
import com.nancal.dictsyndata.model.*;
import com.nancal.dictsyndata.repository.SyncRepository;
import com.nancal.dictsyndata.service.DictionariesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author hewei
 * @date 2022/7/12 17:00
 * @Description
 */
@Service
@Slf4j
public class DictionariesServiceImpl implements DictionariesService {

    @Autowired
    private SyncRepository syncRepository;

    @Value("${tenant.dev}")
    private String devTenant;

    @Value("${tenant.test}")
    private String testTenant;

    @Value("${tenant.current}")
    private String currentTenant;


    @Override
    @Transactional(rollbackFor = Exception.class)
    @DataSourceSwitch(DataSourceType.DEV)
    public String devSync(List<LezaoDictionariesTypeResp> insertDictTypeList,
                          List<LezaoDictionariesTypeResp> updateDictTypeList,
                          List<LezaoDictionariesTypeResp> deleteDictTypeList) {
        syncRepository.insertSync(insertDictTypeList,devTenant);
        syncRepository.updateSync(updateDictTypeList,devTenant);
        syncRepository.deleteSync(deleteDictTypeList);
        return "同步成功";
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @DataSourceSwitch(DataSourceType.TEST)
    public String testSync(List<LezaoDictionariesTypeResp> insertDictTypeList,
                           List<LezaoDictionariesTypeResp> updateDictTypeList,
                           List<LezaoDictionariesTypeResp> deleteDictTypeList) {
        syncRepository.insertSync(insertDictTypeList,testTenant);
        syncRepository.updateSync(updateDictTypeList,testTenant);
        syncRepository.deleteSync(deleteDictTypeList);
        return "同步成功";
    }




    @DataSourceSwitch(DataSourceType.TEST)
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<LezaoDictionariesTypeResp> getDict214Data(){
        return syncRepository.getDictionariesDataList(testTenant);
    }

    @DataSourceSwitch(DataSourceType.DEV)
    @Override
    public List<LezaoDictionariesTypeResp> getDict213Data(){
        return syncRepository.getDictionariesDataList(devTenant);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SyncDictionariesRep computeSync( List<LezaoDictionariesTypeResp> targetData,List<LezaoDictionariesTypeResp> originalData) {
        switch (currentTenant){
            case "dev":
              return   syncDictData(targetData,originalData);
            case "test":
             return    syncDictData(originalData,targetData);
            default:
               return new  SyncDictionariesRep();
        }
    }



    public SyncDictionariesRep syncDictData(List<LezaoDictionariesTypeResp> targetData,List<LezaoDictionariesTypeResp> originalData){
        List<LezaoDictionariesTypeResp>  insertDictTypeList = new ArrayList<>();
        List<LezaoDictionariesTypeResp>  updateDictTypeList = new ArrayList<>();
        List<LezaoDictionariesTypeResp>  deleteDictTypeList = new ArrayList<>();
        originalData.forEach(dictTypeModel -> {
            /**
             *  无数据则进新增集合添加更新
             *  有数据则判断数值更新数值
             *  多出来的数据则删除
             */
            final List<LezaoDictionariesTypeResp> dictionariesType =
                    targetData.stream().filter(dictOriginal -> dictOriginal.equals(dictTypeModel)).collect(Collectors.toList());
            // 判断 如果集合为空 说明 目标没有这条数据 则进新增集合
            if (dictionariesType.isEmpty()){
                insertDictTypeList.add(dictTypeModel);
            }else {
                //集合不为空 则判断值是否相同  不相同则进更新集合
                LezaoDictionariesTypeResp lezaoDictionariesTypeResp = dictionariesType.get(0);
                /**
                 * 筛选下级字典表集合
                 */
                // 214下级数据
                List<LezaoDictionariesResp> dictDataListTest = lezaoDictionariesTypeResp.getChildren();
                List<LezaoDictionariesResp> dictDataListDev = dictTypeModel.getChildren();
                dictDataListDev.forEach(dictOriginal->{
                    List<LezaoDictionariesResp>  insertDictList = new ArrayList<>();
                    List<LezaoDictionariesResp>  updateDictList = new ArrayList<>();
                    List<LezaoDictionariesResp>  deleteDictList = new ArrayList<>();
                    dictDataListDev.forEach(dictType -> {
                        /**
                         *  无数据则进新增集合添加更新
                         *  有数据则判断数值更新数值
                         *  多出来的数据则删除
                         */
                        final List<LezaoDictionariesResp> dictionariesTypeChildren =
                                dictDataListTest.stream().filter(dict214 -> dict214.equals(dictType)).collect(Collectors.toList());
                        // 判断 如果集合为空 说明 214没有这条数据 则进新增集合
                        if (dictionariesTypeChildren.isEmpty()){
                            dictType.setDictTypeId(lezaoDictionariesTypeResp.getId());
                            insertDictList.add(dictType);
                        }else {
                            LezaoDictionariesResp dict = dictionariesTypeChildren.get(0);
                            dictType.setId(dict.getId());
                            dictType.setDictTypeId(dict.getDictTypeId());
                            updateDictList.add(dictType);
                        }
                        // 判断214集合中判断 新增/修改集合是否包换214对象 都不包含咋删除该数据

                    });
                    dictDataListTest.forEach(deleteDictTargetChildren->{
                        boolean insertIsExist = insertDictList.stream().anyMatch(dictTarget -> dictTarget.equals(deleteDictTargetChildren));

                        boolean updateIsExist = updateDictList.stream().anyMatch(dictTarget -> dictTarget.equals(deleteDictTargetChildren));

                        if (!insertIsExist && !updateIsExist){
                            deleteDictList.add(deleteDictTargetChildren);
                        }
                    });
                    dictTypeModel.setDeleteChildren(deleteDictList);
                    dictTypeModel.setInsertChildren(insertDictList);
                    dictTypeModel.setUpdateChildren(updateDictList);
                });
                dictTypeModel.setId(lezaoDictionariesTypeResp.getId());
                updateDictTypeList.add(dictTypeModel);

            }

        });
        // 判断214集合中判断 新增/修改集合是否包换214对象 都不包含咋删除该数据
        targetData.forEach(deleteDictTarget->{
            boolean insertIsExist = insertDictTypeList.stream().anyMatch(dictTarget -> dictTarget.equals(deleteDictTarget));

            boolean updateIsExist = updateDictTypeList.stream().anyMatch(dictTarget -> dictTarget.equals(deleteDictTarget));

            if (!insertIsExist && !updateIsExist){
                deleteDictTypeList.add(deleteDictTarget);
            }
        });
        SyncDictionariesRep syncDictionariesRep = new SyncDictionariesRep();
        syncDictionariesRep.setDeleteDictTypeList(deleteDictTypeList);
        syncDictionariesRep.setInsertDictTypeList(insertDictTypeList);
        syncDictionariesRep.setUpdateDictTypeList(updateDictTypeList);
        return syncDictionariesRep;
    }










}
