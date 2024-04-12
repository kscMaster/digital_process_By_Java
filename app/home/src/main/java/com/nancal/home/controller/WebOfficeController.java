package com.nancal.home.controller;

import cn.hutool.json.JSONUtil;
import com.nancal.api.model.WebOfficeReq;
import com.nancal.gpaas.sdk.weboffice.model.dto.v1.SaveCallbackV1Vo;
import com.nancal.service.service.IWebOfficeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/weboffice")
@Validated
@Api(tags = "文档协同")
@Slf4j
public class WebOfficeController {

    @Autowired
    private IWebOfficeService iWebOfficeService;

    @ApiOperation(value = "文档协同")
    @RequestMapping(value = "/online",method = RequestMethod.GET)
    public void webOffice(@Validated WebOfficeReq webOfficeReq, HttpServletResponse response) throws Exception{
        iWebOfficeService.webOffice(webOfficeReq,response);
    }

    @ApiOperation(value = "文档协同回调")
    @RequestMapping(value = "/editor/saveCallback",method = RequestMethod.POST)
    @ResponseBody
    public void saveCallback(@RequestBody @Validated SaveCallbackV1Vo saveCallbackV1Vo) {
        log.info("webOffice回调，json={}", JSONUtil.toJsonStr(saveCallbackV1Vo));
        iWebOfficeService.saveCallback(saveCallbackV1Vo.getFileId(),saveCallbackV1Vo.getFileName(),saveCallbackV1Vo.getDownloadUrl());
    }

    @ApiOperation(value = "文档协同检查")
    @ResponseBody
    @RequestMapping(value = "/checkEdit",method = RequestMethod.GET)
    public void checkEdit(@Validated WebOfficeReq webOfficeReq){
        iWebOfficeService.checkEdit(webOfficeReq);
    }

}
