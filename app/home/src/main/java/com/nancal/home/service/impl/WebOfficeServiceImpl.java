package com.nancal.home.service.impl;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Pair;
import cn.hutool.core.text.StrPool;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.nancal.api.model.WebOfficeReq;
import com.nancal.api.model.dataset.FileAttrReq;
import com.nancal.api.utils.EntityUtil;
import com.nancal.auth.util.UserUtils;
import com.nancal.common.config.WebOfficeConfig;
import com.nancal.common.enums.ErrorCode;
import com.nancal.common.enums.OperatorEnum;
import com.nancal.common.exception.ServiceException;
import com.nancal.common.exception.TipServiceException;
import com.nancal.common.utils.ResponseWriteUtil;
import com.nancal.common.utils.URLEncodeUtil;
import com.nancal.framework.common.utils.DateUtils;
import com.nancal.gpaas.sdk.weboffice.WebOfficeClient;
import com.nancal.gpaas.sdk.weboffice.client.DefaultEditorClient;
import com.nancal.gpaas.sdk.weboffice.enums.Action;
import com.nancal.gpaas.sdk.weboffice.enums.Language;
import com.nancal.gpaas.sdk.weboffice.enums.UserAuthTypeEnums;
import com.nancal.gpaas.sdk.weboffice.model.dto.v1.EditorV1Req;
import com.nancal.model.entity.DatasetEntity;
import com.nancal.model.entity.FileStorageEntity;
import com.nancal.model.entity.Gte4ResponsibleRlEntity;
import com.nancal.model.entity.WorkspaceObjectEntity;
import com.nancal.remote.vo.CurrentUserInfoVo;
import com.nancal.service.bo.DatasetFileRL;
import com.nancal.service.bo.FileStorage;
import com.nancal.service.factory.DatasetFileRLFactory;
import com.nancal.service.service.IWebOfficeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@Slf4j
public class WebOfficeServiceImpl implements IWebOfficeService {

    @Autowired
    private DefaultEditorClient defaultEditorClient;

    @Autowired
    private WebOfficeConfig webOfficeConfig;

    @Override
    public void webOffice(WebOfficeReq webOfficeReq, HttpServletResponse response) throws Exception{
        try{
            checkEdit(webOfficeReq);
        }catch (ServiceException e){
            ResponseWriteUtil.writeHtml(response,e.getMessage());
            return;
        }
        WebOfficeClient webOfficeClient = new WebOfficeClient(defaultEditorClient);
        EditorV1Req editorV1Req = new EditorV1Req();
        // 您的自定义权限验证地址 可空
        editorV1Req.setUserAuthUrl("");
        // 权限验证类型 非空
        editorV1Req.setUserAuthType(UserAuthTypeEnums.IAM.getCode());
        // 请使用Action枚举中获取 非空
        editorV1Req.setAction(Action.edit.name());
        // 请使用Type枚举中获取 非空
        editorV1Req.setType(webOfficeReq.getType());
        // 请使用Language枚举中获取 非空
        editorV1Req.setLang(Language.zh.name());
        DatasetFileRL datasetFileRL = SpringUtil.getBean(DatasetFileRLFactory.class).create();
        List<FileStorage> fileStorageList = datasetFileRL.getFileStorages(Collections.singletonList(webOfficeReq.getUid()), webOfficeReq.getObjectType());
        if(CollUtil.isEmpty(fileStorageList)){
            ResponseWriteUtil.writeHtml(response,"数据不存在");
            return;
        }
        CurrentUserInfoVo currentUser = SpringUtil.getBean(UserUtils.class).getCurrentUser();
        FileStorage fileStorage = fileStorageList.get(0);
        // 文件ID 非空
        editorV1Req.setFileId(fileStorage.getUid());
        // 文件名 非空
        editorV1Req.setFileName(fileStorage.getOriginFileName()+ StrUtil.DOT+fileStorage.getFileExt());
        // 上传时间时间戳 非空
        DateTimeFormatter dateTimeFormatter= DateTimeFormatter.ofPattern(DateUtils.DATETIME_FORMAT);
        long updateTime = DateUtils.parseDate(dateTimeFormatter.format(fileStorage.getLastUpdate()), DateUtils.DATETIME_FORMAT).getTime();
        editorV1Req.setUploadTime(updateTime+"");

        // 用户ID 非空
        editorV1Req.setUserId(currentUser.getId());
        // 用户名 非空
        editorV1Req.setUserName(currentUser.getName());
        // 文件地址
        String fileUrl = URLEncodeUtil.encodeURIComponent(Base64.encode("http:"+fileStorage.getFilePath()));
        editorV1Req.setFileUrl(fileUrl);
        // 编辑后保存文件地址
        editorV1Req.setSaveCallbackUrl(webOfficeConfig.getCallBackUrl());
        // 权限相关其他保留字 可空
        editorV1Req.setPermissions(new EditorV1Req.Permission());
        // 调用编辑器，并获取输出流
        String in = webOfficeClient.editorRender(editorV1Req);
        // 重写你的的输出流
        IOUtils.write(in.getBytes(StandardCharsets.UTF_8), response.getOutputStream());
    }

    @Override
    @Transactional
    public void saveCallback(String fileId, String fileName, String fileUrl) {
        WorkspaceObjectEntity entity = EntityUtil.getById(new FileStorageEntity().getObjectType(), fileId);
        if(ObjectUtil.isNull(entity)){
            throw new ServiceException(ErrorCode.ERROR,"对象不存在，不进行保存了");
        }
        FileStorageEntity storageEntity = (FileStorageEntity)entity;
//        fileUrl = fileUrl.substring(0,fileUrl.lastIndexOf(StrPool.SLASH))+StrPool.SLASH+
//                URLEncoder.encode(fileUrl.substring(fileUrl.lastIndexOf(StrPool.SLASH)+1), Charset.forName("utf-8"));
//        fileUrl = fileUrl.replace("+","%20");
        fileUrl = URLDecoder.decode(fileUrl,Charset.forName("utf-8"));
        String tempPath =  System.getProperty("user.dir")+ StrPool.SLASH+storageEntity.getOriginFileName()+StrPool.DOT+ FileUtil.getSuffix(fileUrl.split("\\?")[0]);
        try{
            URL httpUrl = new URL(fileUrl);
            FileUtils.copyURLToFile(httpUrl, new File(tempPath));
            FileAttrReq fileAttrReq = this.redepositFile(tempPath);
            FileStorageEntity fileStorage = (FileStorageEntity) entity;
            fileStorage.setFilePath(fileAttrReq.getFilePath());
            fileStorage.setFileSize(fileAttrReq.getFileSize()+"");
            fileStorage.setBucketName(fileAttrReq.getBucketName());
//            fileStorage.setOriginFileName(fileName);
            fileStorage.setType(fileAttrReq.getFileType());
            fileStorage.setLastUpdate(LocalDateTime.now());
            SpringUtil.getBean(EntityManager.class).merge(fileStorage);
        }catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException(ErrorCode.ERROR,"文件转存失败");
        }
    }

    @Override
    public void checkEdit(WebOfficeReq webOfficeReq) {
        Class<?> entityClass = EntityUtil.getEntityClass(webOfficeReq.getObjectType());
        if(!DatasetEntity.class.isAssignableFrom(entityClass)){
            throw new ServiceException(ErrorCode.E_12);
        }
        WorkspaceObjectEntity entity = EntityUtil.getById(webOfficeReq.getObjectType(), webOfficeReq.getUid());
        if(ObjectUtil.isNull(entity)){
            throw new ServiceException(ErrorCode.E_12);
        }
        DatasetFileRL datasetFileRL = SpringUtil.getBean(DatasetFileRLFactory.class).create();
        List<FileStorage> fileStorageList = datasetFileRL.getFileStorages(Collections.singletonList(webOfficeReq.getUid()), webOfficeReq.getObjectType());
        if(CollUtil.isEmpty(fileStorageList)){
            throw new ServiceException(ErrorCode.E_12);
        }
        CurrentUserInfoVo currentUser = SpringUtil.getBean(UserUtils.class).getCurrentUser();
        //验证是否是owner
        try {
            this.verifyAuthority(entity, OperatorEnum.Write, "updateError", entity.getObjectName());
        }catch (TipServiceException e){
            log.info("webonline异常，msg={}",e.getMessage());
            //验证是否指派
            List<Pair<String, Object>> pairs = new ArrayList<>();
            pairs.add(Pair.of(Gte4ResponsibleRlEntity.GTE4LOBJECT_TYPE,webOfficeReq.getObjectType()));
            pairs.add(Pair.of(Gte4ResponsibleRlEntity.GTE4LEFT_OBJECT,webOfficeReq.getUid()));
            pairs.add(Pair.of(Gte4ResponsibleRlEntity.DEL_FLAG,false));
            List<WorkspaceObjectEntity> entityList = EntityUtil.getDynamicEqQuery(new Gte4ResponsibleRlEntity().getObjectType(), pairs).fetch();
            if(CollUtil.isEmpty(entityList)){
                throw new ServiceException(ErrorCode.FORBIDDEN);
            }
            boolean compareRes = entityList.stream().map(Gte4ResponsibleRlEntity.class::cast).
                    anyMatch(resp -> resp.getGte4ResponsibleId().equals(currentUser.getId()));
            if(!compareRes){
                throw new ServiceException(ErrorCode.FORBIDDEN);
            }
        }
    }

}
