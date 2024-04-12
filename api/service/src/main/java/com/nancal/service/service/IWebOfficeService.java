package com.nancal.service.service;

import com.nancal.api.model.WebOfficeReq;

import javax.servlet.http.HttpServletResponse;

public interface IWebOfficeService extends IWorkspaceObjectDomainService{

    /**
     * 文档协同
     * @param webOfficeReq
     * @param response
     * @throws Exception
     */
    void webOffice(WebOfficeReq webOfficeReq, HttpServletResponse response) throws Exception;

    /**
     * 文档协同回调
     * @param fileId
     * @param fileName
     * @param downloadUrl
     */
    void saveCallback(String fileId,String fileName,String downloadUrl);

    /**
     * 检查文档协同权限
     * @param webOfficeReq
     */
    void checkEdit(WebOfficeReq webOfficeReq);
}
