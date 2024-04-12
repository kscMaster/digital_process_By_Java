package com.nancal.changeorder.controller;

import com.nancal.api.model.WorkspaceObjectResp;
import com.nancal.common.base.IdRequest;
import com.nancal.common.base.TableRequest;
import com.nancal.common.base.TableResponse;
import com.nancal.service.service.IGte4DistOrgEntryDomainService;
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
@RequestMapping("/Gte4DistOrgEntry")
@Api(tags = "发放单位")
public class Gte4DistOrgEntryController {

    @Autowired
    private IGte4DistOrgEntryDomainService service;


    @ApiOperation(value = "发放单位列表")
    @PostMapping(value = "/getList")
    public TableResponse<WorkspaceObjectResp> getList(@RequestBody @Validated TableRequest<IdRequest> req) {
        return service.getList(req);
    }


}
