package com.nancal.changeorder.controller;

import com.nancal.api.model.WorkspaceObjectResp;
import com.nancal.common.base.*;
import com.nancal.service.service.IGte4NoticeOrgEntryDomainService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@Slf4j
@RestController
@RequestMapping("/Gte4NoticeOrgEntry")
@Api(tags = "通知单位")
public class Gte4NoticeOrgEntryController {

    @Autowired
    private IGte4NoticeOrgEntryDomainService service;


    @ApiOperation(value = "通知单位列表")
    @PostMapping(value = "/getList")
    public TableResponse<WorkspaceObjectResp> getList(@RequestBody @Validated TableRequest<IdRequest> req) {
        return service.getList(req);
    }


}
