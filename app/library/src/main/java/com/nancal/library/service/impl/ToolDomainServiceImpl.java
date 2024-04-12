package com.nancal.library.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.excel.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.nancal.api.model.*;
import com.nancal.api.model.dataset.FileAttrReq;
import com.nancal.api.utils.DictUtil;
import com.nancal.api.utils.EntityUtil;
import com.nancal.api.utils.ExportExcelUtil;
import com.nancal.api.utils.FileUtils;
import com.nancal.api.utils.excel.analysis.CustomFieldPoiWordAnalysis;
import com.nancal.api.utils.excel.verify.ResourcesPoolVerify;
import com.nancal.common.base.Response;
import com.nancal.common.base.TableRequest;
import com.nancal.common.base.TableResponse;
import com.nancal.common.constants.DictConstant;
import com.nancal.common.enums.ErrorCode;
import com.nancal.common.exception.ServiceException;
import com.nancal.library.service.IItemDomainServiceAdaptor;
import com.nancal.remote.service.RemoteLezaoIntegrationService;
import com.nancal.remote.to.MoreDictEntryReq;
import com.nancal.remote.vo.IntegrationFileVo;
import com.nancal.remote.vo.LezaoResult;
import com.nancal.remote.vo.MoreDictEntryGroupVo;
import com.nancal.remote.vo.MoreDictEntryVo;
import com.nancal.service.service.IDatasetDomainService;
import com.nancal.service.service.IExtraPropertyDataDomainService;
import com.nancal.service.service.IToolDomainService;
import lombok.SneakyThrows;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;


@Service
public class ToolDomainServiceImpl extends CustomFieldPoiWordAnalysis<LibraryAttributeToolReq> implements IToolDomainService, IItemDomainServiceAdaptor {
    @Qualifier("attachmentDomainServiceImpl")
    @Autowired
    IDatasetDomainService service;

    @Autowired
    private DictUtil dictUtil;

    @Autowired
    private ToolDomainServiceImpl toolDomainService;

    @Autowired
    private RemoteLezaoIntegrationService remoteLezaoIntegrationService;

    @Autowired
    private IExtraPropertyDataDomainService iExtraPropertyDataDomainService;

    @Override
    public WorkspaceObjectResp save(BusinessObjectReq req) {
        LibraryAttributeToolReq toolReq = (LibraryAttributeToolReq) req;
        WorkspaceObjectResp save = IItemDomainServiceAdaptor.super.save(toolReq.getToolRevisionReq());
        if (CollectionUtils.isNotEmpty(toolReq.getToolRevisionReq().getFiles())) {
            IToolDomainService.super.saveAttachment(toolReq.getToolRevisionReq().getFiles(),EntityUtil.getRevision(EntityUtil.getObjectType()),save.getRightObject());
        }
        List<ExtraPropertyDataReq> extraPropertyDataReqs = toolReq.getExtraPropertyDataReq();
        iExtraPropertyDataDomainService.insert(extraPropertyDataReqs,save);
        return save;
    }


    @Override
    public void exportExcelModel(HttpServletResponse request){
        MoreDictEntryReq moreDictEntryReq = new MoreDictEntryReq();
        List<String> appCodes = new ArrayList<>(3);
        appCodes.add("lz624-library");
        List<String> dictTypes = new ArrayList<>();
        dictTypes.add(DictConstant.TOOL_EXTRADATA);
        moreDictEntryReq.setAppCodes(appCodes);
        moreDictEntryReq.setDictTypes(dictTypes);
        List<MoreDictEntryGroupVo> codeValueList = dictUtil.getMoreDictEntryVo(moreDictEntryReq);
        List<String> titles = new ArrayList<>();
        titles.add("名称");
        titles.add("图号");
        titles.add("附件");
        List<String> required = new ArrayList<>();
        required.add("非必填项");
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
        ExportExcelUtil.customExportExcels(request,required,titles,"资源库模板-工夹具");
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Response<Gte4ImportRevisionResp> importExcelVerify(MultipartFile file){

        Map<String, AuxiliaryMaterialRevisionImportReq> auxiliaryMaterialRevisionImportReqHashMap = new HashMap<>();
        Map<String, MoreDictEntryVo> moreDictEntryVoMap = new HashMap<>();
        MoreDictEntryReq moreDictEntryReq = new MoreDictEntryReq();
        List<String> appCodes = new ArrayList<>(3);
        appCodes.add("lz624-library");
        List<String> dictTypes = new ArrayList<>();
        dictTypes.add(DictConstant.TOOL_EXTRADATA);
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
        super.analysis(file);
        Gte4ImportRevisionResp gte4ImportRevisionResp = new Gte4ImportRevisionResp();
        gte4ImportRevisionResp.setOkNum((Integer)statisticsMap.get("okNum"));
        gte4ImportRevisionResp.setFailNum((Integer)statisticsMap.get("failNum"));
        gte4ImportRevisionResp.setMsg((String)statisticsMap.get("errorMsg"));
        gte4ImportRevisionResp.setAllNum((Integer)statisticsMap.get("failNum")+(Integer)statisticsMap.get("okNum"));
        return Response.of(gte4ImportRevisionResp);
    }


    @SneakyThrows
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Response<Gte4ImportRevisionResp> importExcel(MultipartFile zipFile, String leftObjec, String  leftObjectType,boolean isExcel){

        int index = zipFile.getOriginalFilename().lastIndexOf(".");
        String fileName = zipFile.getOriginalFilename().substring(0, index);
        File multipartFile = FileUtils.getMultipartFile(zipFile);
        // 临时路径
        String path = System.getProperty("user.dir");
        FileUtils.unzipFiles(path, multipartFile);
        File documentFile = new File(path + File.separator + fileName);
        final File[] files = documentFile.listFiles();
        Map<String, Object> statisticsMap = new HashMap<>(3);
        Map<String,LezaoResult<IntegrationFileVo>> map = new HashMap<>();
        try {
            for (File file : files) {
                if (file.isFile()) {
                    List<Map<String, Object>> param = new ArrayList<>();
                    List<LibraryAttributeToolReq> analysis = excelImportVerify(statisticsMap, param, FileUtils.getMultipartFile(file), path + File.separator + fileName);
                    if (isExcel) {
                        AtomicInteger atomicInteger = new AtomicInteger();
                        batchImport(analysis, leftObjec, leftObjectType, path + File.separator + fileName,map,atomicInteger);
                        toolDomainService.statistics(leftObjectType,leftObjec,atomicInteger.get());
                    } else {
                        Gte4ImportRevisionResp gte4ImportRevisionResp = new Gte4ImportRevisionResp();
                        gte4ImportRevisionResp.setOkNum((Integer) statisticsMap.get("okNum"));
                        gte4ImportRevisionResp.setFailNum((Integer) statisticsMap.get("failNum"));
                        gte4ImportRevisionResp.setMsg((String) statisticsMap.get("errorMsg"));
                        gte4ImportRevisionResp.setAllNum((Integer) statisticsMap.get("failNum") + (Integer) statisticsMap.get("okNum"));
                        return Response.of(gte4ImportRevisionResp);
                    }
                }
            }
        }catch (Exception e){
            throw new ServiceException(ErrorCode.E_10,"文件内部格式异常，解析失败");
        }
        finally {
            multipartFile.delete();
            FileUtils.deleteDir(documentFile);
        }

        return Response.of();
    }

    @Transactional(rollbackFor = Exception.class)
    public void batchImport(List<LibraryAttributeToolReq> analysis,String leftObject,String leftObjectType,String path,Map<String,LezaoResult<IntegrationFileVo>> map,AtomicInteger atomicInteger){
        ThreadPoolTaskExecutor executor = SpringUtil.getBean("applicationTaskExecutor",ThreadPoolTaskExecutor.class);
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        List<CompletableFuture<Void>> all = new ArrayList<>();
        List<List<LibraryAttributeToolReq>> split = CollUtil.split(analysis, 200);
        split.forEach(l ->{
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                l.forEach(revisionReq ->{
                    RequestContextHolder.setRequestAttributes(requestAttributes);
                    // 附件上传
                    List<FileAttrReq> files = new ArrayList();
                    if (null != revisionReq.getToolRevisionReq().getGte4Attaches()) {
                        String updatePath = path + File.separator + revisionReq.getToolRevisionReq().getGte4Attaches();
                        LezaoResult<IntegrationFileVo> lzdigit = null;
                        if (!map.containsKey(updatePath)) {
                            File gte4Attaches = new File(path + File.separator + revisionReq.getToolRevisionReq().getGte4Attaches());
                            lzdigit = remoteLezaoIntegrationService.upload(FileUtils.getMultipartFile(gte4Attaches), "lzdigit");
                            map.put(updatePath, lzdigit);
                        }else {
                            lzdigit = map.get(updatePath);
                        }
                        FileAttrReq fileAttrRep = new FileAttrReq();
                        final IntegrationFileVo data = lzdigit.getData();
                        fileAttrRep.setBucketName(data.getGroup());
                        fileAttrRep.setFilePath(data.getUrl());
                        fileAttrRep.setFileSize(data.getSize());
                        fileAttrRep.setFileType(data.getExt());
                        files.add(fileAttrRep);
                    }
                    revisionReq.getToolRevisionReq().setFiles(files);
                    revisionReq.getToolRevisionReq().setLeftObject(leftObject);
                    revisionReq.getToolRevisionReq().setLeftObjectType(leftObjectType);
                    WorkspaceObjectResp insert = toolDomainService.insert(revisionReq.getToolRevisionReq());
                    if (CollectionUtils.isNotEmpty(revisionReq.getToolRevisionReq().getFiles())) {
                        IToolDomainService.super.saveAttachment(revisionReq.getToolRevisionReq().getFiles(),EntityUtil.getRevision(EntityUtil.getObjectType()),insert.getRightObject());
                    }
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

    }



    /**
     * 解析excel 获取集合  失败数 成功数  失败秒速
     * @param statisticsMap
     * @param param
     * @param file
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public List<LibraryAttributeToolReq> excelImportVerify(Map<String,Object> statisticsMap,List<Map<String, Object>> param,MultipartFile file,String filePath){
        Map<String, AuxiliaryMaterialRevisionImportReq> auxiliaryMaterialRevisionImportReqHashMap = new HashMap<>();
        Map<String, MoreDictEntryVo> moreDictEntryVoMap = new HashMap<>();
        MoreDictEntryReq moreDictEntryReq = new MoreDictEntryReq();
        List<String> appCodes = new ArrayList<>(3);
        appCodes.add("lz624-library");
        List<String> dictTypes = new ArrayList<>();
        dictTypes.add(DictConstant.TOOL_EXTRADATA);
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
        resourcesPoolVerify.setFilePath(filePath);
        resourcesPoolVerify.setExistFile(true);
        statisticsMap.put("okNum",0);
        statisticsMap.put("failNum",0);
        statisticsMap.put("errorMsg","");
        resourcesPoolVerify.setStatisticsMap( statisticsMap);
        super.setMap(moreDictEntryVoMap);
        super.setNumber("序号");
        super.setDictUtil(dictUtil);
        super.setSelectMap(auxiliaryMaterialRevisionImportReqHashMap);
        super.setCheck(resourcesPoolVerify);
        return super.analysis(file);
    }



    @Override
    public TableResponse<List<Map<String,Object>>> customPage(TableRequest<? extends ItemRevisionReq> req){
        TableResponse<WorkspaceObjectResp> response = IToolDomainService.super.pagePlus(req);
        return iExtraPropertyDataDomainService.getCustomObject(response,DictConstant.TOOL_EXTRADATA);
    }
}
