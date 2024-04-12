package com.nancal.myworkspace.controller;

import com.nancal.api.model.Gte4ResponsibleRlReq;
import com.nancal.common.base.IdRequest;
import com.nancal.common.base.Response;
import com.nancal.common.utils.IdGeneratorUtil;
import com.nancal.service.service.ISynergismDomainService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "数据指派")
@Slf4j
@RestController
@RequestMapping("/Synergism")
public class SynergismController {

    @Autowired
    private ISynergismDomainService service;
    @ApiOperation(value = "指派负责人")
    @PostMapping(value = "/assignPersons")
    public Response<Boolean> assignPersons(@RequestBody @Validated Gte4ResponsibleRlReq req) {
        service.assignPersons(req);
        return (Response.of(Boolean.TRUE));
    }

    @ApiOperation(value = "放入home文件夹下")
    @PostMapping(value = "/associatedFile")
    public Response<Boolean> associatedFile(@RequestBody @Validated IdRequest id) {
        service.associatedFile(id);
        return (Response.of(Boolean.TRUE));
    }

}
