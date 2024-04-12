package com.nancal.imexport.service.impl;

import com.nancal.api.model.*;
import com.nancal.api.utils.DictUtil;
import com.nancal.api.utils.excel.analysis.CustomFieldPoiWordAnalysis;
import com.nancal.common.base.Response;
import com.nancal.common.config.TokenContext;
import com.nancal.common.config.TokenInfo;
import com.nancal.common.constants.DictConstant;
import com.nancal.common.enums.AppNameEnum;
import com.nancal.imexport.service.IImexportServiceAdaptor;
import com.nancal.imexport.service.IImexportTaskDomainServiceAdaptor;
import com.nancal.model.entity.EquipmentEntity;
import com.nancal.remote.to.MoreDictEntryReq;
import com.nancal.remote.vo.MoreDictEntryGroupVo;
import com.nancal.remote.vo.MoreDictEntryVo;
import com.nancal.service.service.IExtraPropertyDataDomainService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author hewei
 * @date 2022/10/31 17:31
 * @Description
 */
@Service
@Slf4j
public class EquipmentImportServiceImpl extends CustomFieldPoiWordAnalysis<LibraryAttributeEquipmentReq>
        implements IImexportServiceAdaptor{

    @Autowired
    private IExtraPropertyDataDomainService iExtraPropertyDataDomainService;

    @Autowired
    private IImexportTaskDomainServiceAdaptor iImexportTaskDomainService;

    @Autowired
    private DictUtil dictUtil;


    /**
     * 异步导入设备数据
     * @param leftObject 左侧菜单uid
     * @param leftObjectType 左侧菜单类型
     * @param uid 任务uid
     * @param tokenInfo 异步请求需要的请求头信息
     * @return
     */
    @Async
    @Transactional
    public Response<Gte4ImportRevisionResp> importExcel(String leftObject, String  leftObjectType,String uid, TokenInfo tokenInfo ){
        try {
            TokenContext.setToken(tokenInfo);
            MultipartFile file = TokenContext.getToken().getFile();
            Map<String, AuxiliaryMaterialRevisionImportReq> auxiliaryMaterialRevisionImportReqHashMap = new HashMap<>();
            Map<String, MoreDictEntryVo> moreDictEntryVoMap = new HashMap<>();
            MoreDictEntryReq moreDictEntryReq = new MoreDictEntryReq();
            List<String> appCodes = new ArrayList<>(1);
            appCodes.add(AppNameEnum.LIBRARY.getCode());
            List<String> dictTypes = new ArrayList<>();
            dictTypes.add(DictConstant.EQUIPMENT_EXTRADATA);
            moreDictEntryReq.setAppCodes(appCodes);
            moreDictEntryReq.setDictTypes(dictTypes);
            List<MoreDictEntryGroupVo> codeValueList = dictUtil.getMoreDictEntryVo(moreDictEntryReq);
            this.customField(codeValueList,auxiliaryMaterialRevisionImportReqHashMap,moreDictEntryVoMap);
            super.setMap(moreDictEntryVoMap);
            super.setNumber("序号");
            super.setDictUtil(dictUtil);
            super.setSelectMap(auxiliaryMaterialRevisionImportReqHashMap);
            List<LibraryAttributeEquipmentReq> analysis = super.analysis(file);
            AtomicInteger atomicInteger = new AtomicInteger();
            analysis.forEach(revisionReq -> {
                EquipmentRevisionReq equipmentRevisionReq = revisionReq.getEquipmentRevisionReq();
                equipmentRevisionReq.setLeftObject(leftObject);
                equipmentRevisionReq.setLeftObjectType(leftObjectType);
                equipmentRevisionReq.setObjectType(new EquipmentEntity().getObjectType());
                WorkspaceObjectResp insert = save(equipmentRevisionReq);
                List<ExtraPropertyDataReq> extraPropertyDataReqs = revisionReq.getExtraPropertyDataReq();
                iExtraPropertyDataDomainService.insert(extraPropertyDataReqs, insert);
                atomicInteger.incrementAndGet();
            });

            this.statistics(leftObjectType, leftObject, atomicInteger.get());
            iImexportTaskDomainService.success(uid);
        }catch (Exception e){
            iImexportTaskDomainService.error(uid,e.getMessage());
        }
        return Response.of();
    }




}
