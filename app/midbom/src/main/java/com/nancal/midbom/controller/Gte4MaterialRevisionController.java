package com.nancal.midbom.controller;

import com.nancal.api.model.Gte4MaterialRevisionReq;
import com.nancal.api.model.WorkspaceObjectResp;
import com.nancal.common.base.*;
import com.nancal.service.service.IGte4MaterialRevisionDomainService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequestMapping("/Gte4MaterialRevision")
@Api(tags = "物料版本管理")
public class Gte4MaterialRevisionController {

    @Qualifier(value = "gte4MaterialRevisionDomainServiceImpl")
    @Autowired
    private IGte4MaterialRevisionDomainService service;


    @ApiOperation(value = "物料版本模糊查询")
    @PostMapping(value = "/getByLike")
    public TableResponse<WorkspaceObjectResp> getByLike(@RequestBody @Validated(LikeGroup.class) TableRequest<Gte4MaterialRevisionReq> req) {
        return service.multiPageLike(req);
    }

}
