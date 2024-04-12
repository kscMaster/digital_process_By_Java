package com.nancal.library.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.excel.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.nancal.api.model.*;
import com.nancal.api.utils.DictUtil;
import com.nancal.api.utils.ExportExcelUtil;
import com.nancal.api.utils.excel.analysis.CustomFieldPoiWordAnalysis;
import com.nancal.api.utils.excel.verify.ResourcesPoolVerify;
import com.nancal.common.base.Response;
import com.nancal.common.base.TableRequest;
import com.nancal.common.base.TableResponse;
import com.nancal.common.constants.DictConstant;
import com.nancal.library.service.IItemDomainServiceAdaptor;
import com.nancal.remote.to.MoreDictEntryReq;
import com.nancal.remote.vo.MoreDictEntryGroupVo;
import com.nancal.remote.vo.MoreDictEntryVo;
import com.nancal.service.service.IEquipmentDomainService;
import com.nancal.service.service.IExtraPropertyDataDomainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;


@Service
public class EquipmentDomainServiceImpl extends CustomFieldPoiWordAnalysis<LibraryAttributeEquipmentReq> implements IEquipmentDomainService, IItemDomainServiceAdaptor {


    @Autowired
    private IExtraPropertyDataDomainService iExtraPropertyDataDomainService;

    @Autowired
    private EquipmentDomainServiceImpl equipmentDomainService;

    @Autowired
    private DictUtil dictUtil;

    @Override
    public WorkspaceObjectResp save(BusinessObjectReq req) {
        LibraryAttributeEquipmentReq libraryAttributeEquipmentReq = (LibraryAttributeEquipmentReq)req;
        WorkspaceObjectResp save = IItemDomainServiceAdaptor.super.save(libraryAttributeEquipmentReq.getEquipmentRevisionReq());
        List<ExtraPropertyDataReq> extraPropertyDataReqs = libraryAttributeEquipmentReq.getExtraPropertyDataReq();
        iExtraPropertyDataDomainService.insert(extraPropertyDataReqs,save);
        return save;

    }

    @Override
    public void exportExcelModel(HttpServletResponse request){
        MoreDictEntryReq moreDictEntryReq = new MoreDictEntryReq();
        List<String> appCodes = new ArrayList<>(1);
        appCodes.add("lz624-library");
        List<String> dictTypes = new ArrayList<>();
        dictTypes.add(DictConstant.EQUIPMENT_EXTRADATA);
        moreDictEntryReq.setAppCodes(appCodes);
        moreDictEntryReq.setDictTypes(dictTypes);
        List<MoreDictEntryGroupVo> codeValueList = dictUtil.getMoreDictEntryVo(moreDictEntryReq);
        List<String> titles = new ArrayList<>();
        titles.add("名称");
        titles.add("型号");
        List<String> required = new ArrayList<>();
        required.add("非必填项");
        required.add("非必填项");
        codeValueList.forEach(codeValue ->{
            codeValue.getDictList().forEach(moreDictEntryVo -> {
                if (StringUtils.isNotBlank(moreDictEntryVo.getParentId())) {
                    boolean isExistParent = codeValue.getDictList().stream().anyMatch(t ->  StringUtils.isNotBlank(moreDictEntryVo.getParentId()) && t.getId().equals(moreDictEntryVo.getParentId()));
                    if (isExistParent) {
                        AuxiliaryMaterialRevisionImportReq parseObject = JSON.parseObject(moreDictEntryVo.getRemark(), AuxiliaryMaterialRevisionImportReq.class);
                        if (parseObject.getType().equals("input") && parseObject.getDataType().equals("int")) {
                            titles.add(moreDictEntryVo.getValue() + "#");
                        } else if (parseObject.getType().equals("radio")) {
                            titles.add(moreDictEntryVo.getValue() + "&");
                        } else if (parseObject.getType().equals("select")) {
                            titles.add(moreDictEntryVo.getValue() + "*");
                        } else {
                            titles.add(moreDictEntryVo.getValue());
                        }
                        if (parseObject.getRequired()) {
                            required.add("必填项");
                        } else {
                            required.add("非必填项");
                        }
                    }
                }
            });
        });
        ExportExcelUtil.customExportExcels(request,required,titles,"资源库模板-设备");
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public Response<Gte4ImportRevisionResp> importExcel(MultipartFile file,String leftObjec,String  leftObjectType,boolean isExcel){
        Map<String, AuxiliaryMaterialRevisionImportReq> auxiliaryMaterialRevisionImportReqHashMap = new HashMap<>();
        Map<String, MoreDictEntryVo> moreDictEntryVoMap = new HashMap<>();
        MoreDictEntryReq moreDictEntryReq = new MoreDictEntryReq();
        List<String> appCodes = new ArrayList<>(1);
        appCodes.add("lz624-library");
        List<String> dictTypes = new ArrayList<>();
        dictTypes.add(DictConstant.EQUIPMENT_EXTRADATA);
        moreDictEntryReq.setAppCodes(appCodes);
        moreDictEntryReq.setDictTypes(dictTypes);
        List<MoreDictEntryGroupVo> codeValueList = dictUtil.getMoreDictEntryVo(moreDictEntryReq);
        if (!codeValueList.isEmpty()){
            MoreDictEntryGroupVo moreDictEntryGroupVo = codeValueList.get(0);
            moreDictEntryGroupVo.getDictList().forEach(moreDictEntryVo -> {
                if (StringUtils.isNotBlank(moreDictEntryVo.getParentId())) {
                    AuxiliaryMaterialRevisionImportReq parseObject = JSON.parseObject(moreDictEntryVo.getRemark(), AuxiliaryMaterialRevisionImportReq.class);
                    if (parseObject.getType().equals("input") && parseObject.getDataType().equals("int")){
                        moreDictEntryVoMap.put(moreDictEntryVo.getValue()+"#",moreDictEntryVo);
                        auxiliaryMaterialRevisionImportReqHashMap.put(moreDictEntryVo.getValue()+"#",parseObject);
                    }else if (parseObject.getType().equals("radio")) {
                        moreDictEntryVoMap.put(moreDictEntryVo.getValue()+"&",moreDictEntryVo);
                        auxiliaryMaterialRevisionImportReqHashMap.put(moreDictEntryVo.getValue() + "&",parseObject);
                    }else if (parseObject.getType().equals("select")){
                        moreDictEntryVoMap.put(moreDictEntryVo.getValue()+"*",moreDictEntryVo);
                        auxiliaryMaterialRevisionImportReqHashMap.put(moreDictEntryVo.getValue() + "*",parseObject);
                    }else {
                        moreDictEntryVoMap.put(moreDictEntryVo.getValue(),moreDictEntryVo);
                        auxiliaryMaterialRevisionImportReqHashMap.put(moreDictEntryVo.getValue(),parseObject);
                    }
                }
            });
        }

        ResourcesPoolVerify resourcesPoolVerify = new ResourcesPoolVerify();
        resourcesPoolVerify.setParam(new ArrayList<>());
        resourcesPoolVerify.setDictUtil(dictUtil);
        resourcesPoolVerify.setMap(auxiliaryMaterialRevisionImportReqHashMap);
        Map<String, Object> statisticsMap = new HashMap<>(3);
        statisticsMap.put("okNum",0);
        statisticsMap.put("failNum",0);
        statisticsMap.put("errorMsg","");
        resourcesPoolVerify.setStatisticsMap( statisticsMap);
        super.setMap(moreDictEntryVoMap);
        super.setNumber("序号");
        super.setDictUtil(dictUtil);
        super.setSelectMap(auxiliaryMaterialRevisionImportReqHashMap);
        super.setCheck(resourcesPoolVerify);
        List<LibraryAttributeEquipmentReq> analysis = super.analysis(file);
        if (!isExcel){
            Gte4ImportRevisionResp gte4ImportRevisionResp = new Gte4ImportRevisionResp();
            gte4ImportRevisionResp.setOkNum((Integer)statisticsMap.get("okNum"));
            gte4ImportRevisionResp.setFailNum((Integer)statisticsMap.get("failNum"));
            gte4ImportRevisionResp.setMsg((String)statisticsMap.get("errorMsg"));
            gte4ImportRevisionResp.setAllNum((Integer)statisticsMap.get("failNum")+(Integer)statisticsMap.get("okNum"));
            return Response.of(gte4ImportRevisionResp);
        }
        ThreadPoolTaskExecutor executor = SpringUtil.getBean("applicationTaskExecutor",ThreadPoolTaskExecutor.class);
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        List<CompletableFuture<Void>> all = new ArrayList<>();
        List<List<LibraryAttributeEquipmentReq>> split = CollUtil.split(analysis, 200);
        AtomicInteger atomicInteger = new AtomicInteger();
        split.forEach(l ->{
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                l.forEach(revisionReq ->{
                    RequestContextHolder.setRequestAttributes(requestAttributes);
                    EquipmentRevisionReq equipmentRevisionReq = revisionReq.getEquipmentRevisionReq();
                    equipmentRevisionReq.setLeftObject(leftObjec);
                    equipmentRevisionReq.setLeftObjectType(leftObjectType);
                    WorkspaceObjectResp insert = equipmentDomainService.insert(revisionReq.getEquipmentRevisionReq());
                    List<ExtraPropertyDataReq> extraPropertyDataReqs = revisionReq.getExtraPropertyDataReq();
                    iExtraPropertyDataDomainService.insert(extraPropertyDataReqs,insert);
                    atomicInteger.incrementAndGet();
                });
            }, executor);
            all.add(future);
        });

        if (CollUtil.isNotEmpty(all)) {
            CompletableFuture.allOf(all.toArray(new CompletableFuture[]{})).join();
        }
        equipmentDomainService.statistics(leftObjectType,leftObjec,atomicInteger.get());
        return Response.of();
    }


    @Override
    public TableResponse<List<Map<String,Object>>> customPage(TableRequest<? extends ItemRevisionReq> req){
        TableResponse<WorkspaceObjectResp> response = IEquipmentDomainService.super.pagePlus(req);
        return iExtraPropertyDataDomainService.getCustomObject(response,DictConstant.EQUIPMENT_EXTRADATA);
    }
}
