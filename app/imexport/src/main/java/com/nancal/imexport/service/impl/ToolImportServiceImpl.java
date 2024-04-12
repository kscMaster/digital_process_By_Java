package com.nancal.imexport.service.impl;

import com.nancal.api.model.*;
import com.nancal.api.model.dataset.FileAttrReq;
import com.nancal.api.utils.DictUtil;
import com.nancal.api.utils.EntityUtil;
import com.nancal.api.utils.FileUtils;
import com.nancal.api.utils.excel.analysis.CustomFieldPoiWordAnalysis;
import com.nancal.common.base.Response;
import com.nancal.common.config.TokenContext;
import com.nancal.common.config.TokenInfo;
import com.nancal.common.constants.DictConstant;
import com.nancal.common.enums.AppNameEnum;
import com.nancal.imexport.service.IImexportServiceAdaptor;
import com.nancal.imexport.service.IImexportTaskDomainServiceAdaptor;
import com.nancal.model.entity.ToolEntity;
import com.nancal.remote.service.RemoteLezaoIntegrationService;
import com.nancal.remote.to.MoreDictEntryReq;
import com.nancal.remote.vo.IntegrationFileVo;
import com.nancal.remote.vo.LezaoResult;
import com.nancal.remote.vo.MoreDictEntryGroupVo;
import com.nancal.remote.vo.MoreDictEntryVo;
import com.nancal.service.service.IExtraPropertyDataDomainService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author hewei
 * @date 2022/10/31 17:46
 * @Description
 */
@Service
@Slf4j
public class ToolImportServiceImpl extends CustomFieldPoiWordAnalysis<LibraryAttributeToolReq> implements IImexportServiceAdaptor {

    @Autowired
    private DictUtil dictUtil;

    @Autowired
    private RemoteLezaoIntegrationService remoteLezaoIntegrationService;

    @Autowired
    private IImexportTaskDomainServiceAdaptor iImexportTaskDomainService;

    @Autowired
    private IExtraPropertyDataDomainService iExtraPropertyDataDomainService;

    @SneakyThrows
    @Transactional(rollbackFor = Exception.class)
    @Async
    public Response<Gte4ImportRevisionResp> importExcel(String leftObjec, String  leftObjectType, String uid, TokenInfo tokenInfo){
        TokenContext.setToken(tokenInfo);
        MultipartFile zipFile = TokenContext.getToken().getFile();
        int index = zipFile.getOriginalFilename().lastIndexOf(".");
        String fileName = zipFile.getOriginalFilename().substring(0, index);
        File multipartFile = FileUtils.getMultipartFile(zipFile);
        // 临时路径
        String path = System.getProperty("user.dir");
        FileUtils.unzipFiles(path, multipartFile);
        File documentFile = new File(path + File.separator + fileName);
        final File[] files = documentFile.listFiles();
        Map<String, Object> statisticsMap = new HashMap<>(3);
        Map<String, LezaoResult<IntegrationFileVo>> map = new HashMap<>();
        try {
            for (File file : files) {
                if (file.isFile()) {
                    List<LibraryAttributeToolReq> analysis = excelImportVerify(FileUtils.getMultipartFile(file));
                    AtomicInteger atomicInteger = new AtomicInteger();
                    batchImport(analysis, leftObjec, leftObjectType, path + File.separator + fileName,map,atomicInteger);
                    this.statistics(leftObjectType,leftObjec,atomicInteger.get());
                    iImexportTaskDomainService.success(uid);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            iImexportTaskDomainService.error(uid,e.getMessage());
        }
        finally {
            multipartFile.delete();
            FileUtils.deleteDir(documentFile);
        }

        return Response.of();
    }


    @Transactional(rollbackFor = Exception.class)
    public void batchImport(List<LibraryAttributeToolReq> analysis,String leftObject,String leftObjectType,String path,Map<String,LezaoResult<IntegrationFileVo>> map,AtomicInteger atomicInteger){
        analysis.forEach(revisionReq ->{
            // 附件上传
            List<FileAttrReq> files = new ArrayList();
            /**
             * 已存在附件不重复上传
             */
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
                IntegrationFileVo data = lzdigit.getData();
                fileAttrRep.setBucketName(data.getGroup());
                fileAttrRep.setFilePath(data.getUrl());
                fileAttrRep.setFileSize(data.getSize());
                fileAttrRep.setFileType(data.getExt());
                files.add(fileAttrRep);
            }
            ToolRevisionReq toolRevisionReq = revisionReq.getToolRevisionReq();
            toolRevisionReq.setFiles(files);
            toolRevisionReq.setObjectType(new ToolEntity().getObjectType());
            toolRevisionReq.setLeftObject(leftObject);
            toolRevisionReq.setLeftObjectType(leftObjectType);
            WorkspaceObjectResp insert = this.save(toolRevisionReq);
            if (CollectionUtils.isNotEmpty(revisionReq.getToolRevisionReq().getFiles())) {
                this.saveAttachment(revisionReq.getToolRevisionReq().getFiles(), EntityUtil.getRevision(new ToolEntity().getObjectType()),insert.getRightObject());
            }
            List<ExtraPropertyDataReq> extraPropertyDataReqs = revisionReq.getExtraPropertyDataReq();
            iExtraPropertyDataDomainService.insert(extraPropertyDataReqs,insert);
            atomicInteger.incrementAndGet();
        });
    }



    /**
     * 解析excel 获取集合  失败数 成功数  失败秒速
     * @param file
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public List<LibraryAttributeToolReq> excelImportVerify(MultipartFile file){
        Map<String, AuxiliaryMaterialRevisionImportReq> auxiliaryMaterialRevisionImportReqHashMap = new HashMap<>();
        Map<String, MoreDictEntryVo> moreDictEntryVoMap = new HashMap<>();
        MoreDictEntryReq moreDictEntryReq = new MoreDictEntryReq();
        List<String> appCodes = new ArrayList<>(3);
        appCodes.add(AppNameEnum.LIBRARY.getCode());
        List<String> dictTypes = new ArrayList<>();
        dictTypes.add(DictConstant.TOOL_EXTRADATA);
        moreDictEntryReq.setAppCodes(appCodes);
        moreDictEntryReq.setDictTypes(dictTypes);
        List<MoreDictEntryGroupVo> codeValueList = dictUtil.getMoreDictEntryVo(moreDictEntryReq);
        this.customField(codeValueList,auxiliaryMaterialRevisionImportReqHashMap,moreDictEntryVoMap);
        super.setMap(moreDictEntryVoMap);
        super.setNumber("序号");
        super.setDictUtil(dictUtil);
        super.setSelectMap(auxiliaryMaterialRevisionImportReqHashMap);
        return super.analysis(file);
    }

}
