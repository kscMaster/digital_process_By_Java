package com.nancal.inspectionlibrary.service.impl;

import cn.hutool.core.util.StrUtil;
import com.nancal.api.model.*;
import com.nancal.api.model.dataset.FileAttrReq;
import com.nancal.api.utils.DictUtil;
import com.nancal.api.utils.EntityUtil;
import com.nancal.api.utils.FileUtils;
import com.nancal.api.utils.excel.analysis.ExcelPoiWordAnalysis;
import com.nancal.api.utils.excel.verify.BaseLibraryVerify;
import com.nancal.common.base.IdRequest;
import com.nancal.common.base.Response;
import com.nancal.common.base.TableRequest;
import com.nancal.common.base.TableResponse;
import com.nancal.common.enums.ErrorCode;
import com.nancal.common.exception.ServiceException;
import com.nancal.inspectionlibrary.service.IItemDomainServiceAdaptor;
import com.nancal.model.entity.Gte4InspectionRevisionEntity;
import com.nancal.model.entity.QGte4InspectionRevisionEntity;
import com.nancal.remote.service.RemoteLezaoIntegrationService;
import com.nancal.remote.vo.IntegrationFileVo;
import com.nancal.remote.vo.LezaoResult;
import com.nancal.service.service.IDatasetDomainService;
import com.nancal.service.service.IGte4InspectionDomainService;
import com.nancal.service.service.IItemRevisionDomainService;
import com.nancal.service.service.ILibraryFolderDomainService;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import net.lingala.zip4j.exception.ZipException;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
@Slf4j
public class Gte4InspectionDomainServiceImpl extends ExcelPoiWordAnalysis<Gte4InspectionRevisionResp> implements IGte4InspectionDomainService, IItemDomainServiceAdaptor {
    @Qualifier("attachmentDomainServiceImpl")
    @Autowired
    IDatasetDomainService service;
    @Autowired
    private DictUtil dictUtil;
    @Autowired
    private JPAQueryFactory jpaQueryFactory;

    @Autowired
    private ILibraryFolderDomainService iLibraryFolderDomainService;

    @Autowired
    private IItemRevisionDomainService iItemRevisionDomainService;

    @Autowired
    private RemoteLezaoIntegrationService remoteLezaoIntegrationService;




    @Override
    public WorkspaceObjectResp save(BusinessObjectReq req) {
        Gte4InspectionRevisionReq gte4InspectionRevisionReq = (Gte4InspectionRevisionReq)req;
        //校验getGte4ID()是否存在
        if (StrUtil.isNotBlank(gte4InspectionRevisionReq.getGte4ID())){
            QGte4InspectionRevisionEntity qGte4InspectionRevisionEntity = QGte4InspectionRevisionEntity.gte4InspectionRevisionEntity;
            BooleanBuilder where = new BooleanBuilder();
            where.and(qGte4InspectionRevisionEntity.gte4ID.eq(gte4InspectionRevisionReq.getGte4ID()));
            where.and(qGte4InspectionRevisionEntity.delFlag.isFalse());
            if ( jpaQueryFactory.selectFrom(qGte4InspectionRevisionEntity).where(where).fetchCount()>0){
                throw new ServiceException(ErrorCode.E_10,"代号已存在" );
            }
        }
        //保存数据
        WorkspaceObjectResp save = IItemDomainServiceAdaptor.super.save(req);
        //保存附件
        if (CollectionUtils.isNotEmpty(gte4InspectionRevisionReq.getFiles())){
            IGte4InspectionDomainService.super.saveAttachment(gte4InspectionRevisionReq.getFiles(),EntityUtil.getRevision(EntityUtil.getObjectType()),save.getRightObject());
        }
        return save;
    }


    @Override
    public void treeExport(TableRequest<ItemRevisionReq> req, HttpServletResponse response){
        List<String> idrequests = req.getData().getIdrequests();
        List<ItemRevisionResp> list = new ArrayList<>();
        idrequests.forEach(uid->{
            IdRequest idRequest = new IdRequest();
            idRequest.setUid(uid);
            idRequest.setObjectType("Gte4InspectionRevision");
            ItemRevisionResp workspaceObjectResp = (ItemRevisionResp)iItemRevisionDomainService.getObject(idRequest);
            list.add(workspaceObjectResp);
        });
        String fileName = iLibraryFolderDomainService.treeLibraryName(req.getData().getUid(), req.getData().getFileName());
        fileName = fileName.replaceAll("型号:","").replaceAll("批次:","").replaceAll("专业:","");
        if (idrequests.isEmpty()){
            List<String> uids = iLibraryFolderDomainService.treeLibrary(req.getData().getUid());
            List<WorkspaceObjectResp> dataList = new ArrayList<>();
            uids.forEach(uis ->{
                req.getData().setUid(uis);
                dataList.addAll(pagePlus(req).getData());
            });

            export(req.getData().getLibraryList(),dataList,dictUtil,fileName,response);
            return;
        }
        export(req.getData().getLibraryList(),list,dictUtil,fileName,response);
    }


    @Override
    public void likeExport(TableRequest<Gte4InspectionRevisionReq> req, HttpServletResponse response){
        List<String> idrequests = req.getData().getIdrequests();
        List<ItemRevisionResp> list = new ArrayList<>();
        idrequests.forEach(uid->{
            IdRequest idRequest = new IdRequest();
            idRequest.setUid(uid);
            idRequest.setObjectType("Gte4InspectionRevision");
            ItemRevisionResp workspaceObjectResp = (ItemRevisionResp)iItemRevisionDomainService.getObject(idRequest);
            list.add(workspaceObjectResp);
        });
        String fileName = iLibraryFolderDomainService.treeLibraryName(req.getData().getUid(), req.getData().getFileName());
        fileName = fileName.replaceAll("型号:","").replaceAll("批次:","").replaceAll("专业:","");
        if (idrequests.isEmpty()) {
            TableResponse<WorkspaceObjectResp> resp = iItemRevisionDomainService.pageAndFiles(req);
            export(req.getData().getLibraryList(), resp.getData(), dictUtil,fileName,response);
            return;
        }
        export(req.getData().getLibraryList(), list, dictUtil,fileName,response);
    }


    /**
     * 解析excel验证
     * @param file excel文件
     * @return
     */
    @Override
    public Response<Gte4ImportRevisionResp> importExcelVerify(MultipartFile file) {
        Map<String,Object> statisticsMap = new HashMap<>(3);
        List<Map<String, Object>> param = new ArrayList<>();
        excelImportVerify(statisticsMap,param, file,"","",false);
        Gte4ImportRevisionResp gte4ImportRevisionResp = new Gte4ImportRevisionResp();
        gte4ImportRevisionResp.setOkNum((Integer)statisticsMap.get("okNum"));
        gte4ImportRevisionResp.setFailNum((Integer)statisticsMap.get("failNum"));
        gte4ImportRevisionResp.setMsg((String)statisticsMap.get("errorMsg"));
        gte4ImportRevisionResp.setAllNum((Integer)statisticsMap.get("failNum")+(Integer)statisticsMap.get("okNum"));
        return Response.of(gte4ImportRevisionResp);
    }


    /**
     * 步骤：
     *     1.获取文件名
     *     2.MultipartFile 转换成 File
     *     3.File 解压成文件设置项目临时路径保存
     *     4.获取文件夹下面的文件集合
     *     5.判断为文件校验参数
     *     6.错误数为0执行入库操作 判断附件不为空 读取附件转MultipartFile上传
     *     7.ID和版本重复更新 否则 新增
     *
     * @param zipFile  zip包
     * @param leftObject 专业id
     * @return
     * @throws ZipException
     * @throws UnsupportedEncodingException
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response<Gte4ImportRevisionResp> importExcel(MultipartFile zipFile,String leftObject) throws ZipException, UnsupportedEncodingException {
        int index = zipFile.getOriginalFilename().lastIndexOf(".");
        String fileName = zipFile.getOriginalFilename().substring(0, index);
        File multipartFile = FileUtils.getMultipartFile(zipFile);
        // 临时路径
        String path = System.getProperty("user.dir");
        FileUtils.unzipFiles(path,multipartFile);
        File documentFile = new File(path +File.separator+ fileName);
        final File[] files = documentFile.listFiles();
        for (File file: files){
            if (file.isFile()){
                Map<String,Object> statisticsMap = new HashMap<>(3);
                List<Map<String, Object>> param = new ArrayList<>();
                List<Gte4InspectionRevisionResp> analysis =  excelImportVerify(statisticsMap, param, FileUtils.getMultipartFile(file), file.getName(), path + File.separator + fileName, true);;
                Integer failNum = (Integer)statisticsMap.get("failNum");
                if (failNum == 0 && StringUtils.isNotBlank(leftObject)) {
                    batchImport( analysis,path +File.separator+ fileName,leftObject);
                }else {
                    Gte4ImportRevisionResp gte4ImportRevisionResp = new Gte4ImportRevisionResp();
                    gte4ImportRevisionResp.setOkNum((Integer)statisticsMap.get("okNum"));
                    gte4ImportRevisionResp.setFailNum((Integer)statisticsMap.get("failNum"));
                    gte4ImportRevisionResp.setMsg((String)statisticsMap.get("errorMsg"));
                    gte4ImportRevisionResp.setAllNum((Integer)statisticsMap.get("failNum")+(Integer)statisticsMap.get("okNum"));
                    return Response.of(gte4ImportRevisionResp);
                }
            }
        }
        multipartFile.delete();
        documentFile.delete();
        return Response.of();
    }




    /**
     * 解析excel 获取集合  失败数 成功数  失败秒速
     * @param statisticsMap
     * @param param
     * @param file
     * @return
     */
    public List<Gte4InspectionRevisionResp> excelImportVerify(Map<String,Object> statisticsMap,List<Map<String, Object>> param,MultipartFile file,String fileName,String filePath,Boolean fileIsExist){
        statisticsMap.put("okNum",0);
        statisticsMap.put("failNum",0);
        statisticsMap.put("errorMsg",null);
        BaseLibraryVerify baseLibraryVerify = new BaseLibraryVerify();
        baseLibraryVerify.setStatisticsMap(statisticsMap);
        baseLibraryVerify.setParam(param);
        baseLibraryVerify.setDictUtil(dictUtil);
        baseLibraryVerify.setFilePath(filePath);
        baseLibraryVerify.setFileName(fileName);
        baseLibraryVerify.setFileIsExist(fileIsExist);
        baseLibraryVerify.setJpaQueryFactory(jpaQueryFactory);
        super.setDictUtil(dictUtil);
        super.setNumber("序号");
        super.setCheck(baseLibraryVerify);
        return super.analysis(file);
    }

    /**
     *  入库操作
     * @param analysis
     * @param path
     */
    @Transactional(rollbackFor = Exception.class)
    public void batchImport(List<Gte4InspectionRevisionResp> analysis,String path,String leftObject){
        analysis.forEach(grr -> {
            // 附件上传
            List<FileAttrReq> files = new ArrayList();
            if (null != grr.getGte4Attaches()) {
                File gte4Attaches = new File(path+ File.separator + grr.getGte4Attaches());
                LezaoResult<IntegrationFileVo> lzdigit = remoteLezaoIntegrationService.upload(FileUtils.getMultipartFile(gte4Attaches), "lzdigit");
                FileAttrReq fileAttrRep = new FileAttrReq();
                final IntegrationFileVo data = lzdigit.getData();
                fileAttrRep.setBucketName(data.getGroup());
                fileAttrRep.setFilePath(data.getUrl());
                fileAttrRep.setFileSize(data.getSize());
                fileAttrRep.setFileType(data.getExt());
                files.add(fileAttrRep);
            }
            // 判断新增还是更新
            Gte4InspectionRevisionEntity idIsExist = isIDIsExist(grr.getItemId(), grr.getRevisionId());
            Gte4InspectionRevisionReq businessObjectReq = new Gte4InspectionRevisionReq();
            if (null == idIsExist){
                //保存数据
                grr.setLeftObject(leftObject);
                grr.setLeftObjectType("LibraryFolder");
                BeanUtils.copyProperties(grr,businessObjectReq);
                businessObjectReq.setFiles(files);
                this.save(businessObjectReq);
            }else {
                BeanUtils.copyProperties(idIsExist,businessObjectReq);
                iItemRevisionDomainService.updatePlus(businessObjectReq);
            }
        });
    }


    /**
     * ID重复校验
     * @param itemId ID
     * @param revisionId 版本
     * @return
     */
    public Gte4InspectionRevisionEntity isIDIsExist(String itemId,String revisionId){
        QGte4InspectionRevisionEntity qGte4InspectionRevisionEntity = QGte4InspectionRevisionEntity.gte4InspectionRevisionEntity;
        BooleanBuilder where = new BooleanBuilder();
        if (StringUtils.isNotBlank(itemId) && StringUtils.isNotBlank(revisionId)) {
            where.and(qGte4InspectionRevisionEntity.gte4BelongItemId.eq(itemId));
            where.and(qGte4InspectionRevisionEntity.revisionId.eq(revisionId));
            where.and(qGte4InspectionRevisionEntity.delFlag.isFalse());
            List<Gte4InspectionRevisionEntity> fetch = jpaQueryFactory.selectFrom(qGte4InspectionRevisionEntity).where(where).fetch();
            if (!fetch.isEmpty()){
                return fetch.get(0);
            }
        }

        return null;
    }

}