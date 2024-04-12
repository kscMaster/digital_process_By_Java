package com.nancal.library.controller;


import com.nancal.api.utils.DictUtil;
import com.nancal.common.base.Response;
import com.nancal.remote.vo.DictItemVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/processInstance")
@Api(tags = "送审、发布流程")
public class ProcessInstanceController {

    @Autowired
    private DictUtil dictUtil;

    @ApiOperation(value = "流程列表")
    @GetMapping(value = "/getProcessListByObjectType")
    public Response<List<DictItemVo>> getProcessListByObjectType(String objectType) {
        return Response.of(dictUtil.getProcessListByObjectType(objectType));
    }

}
