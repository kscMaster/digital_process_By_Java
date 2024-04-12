//package com.nancal.myworkspace.controller;
//
//import com.nancal.api.model.LikeReq;
//import com.nancal.api.model.LikeResp;
//import com.nancal.common.base.TableRequest;
//import com.nancal.common.base.TableResponse;
//import com.nancal.service.service.IObjectIndexDomainService;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.validation.annotation.Validated;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@Slf4j
//@RestController
//@RequestMapping("/ObjectIndex")
//@Api(tags = "模糊查询管理")
//public class ObjectIndexController {
//
//    @Qualifier("objectIndexDomainServiceImpl")
//    @Autowired
//    protected IObjectIndexDomainService service;
//
//    @ApiOperation(value = "模糊查询")
//    @PostMapping(value = "/getLike")
//    public TableResponse<LikeResp> like(@RequestBody @Validated TableRequest<LikeReq> req) {
//        return service.getByLike(req);
//    }
//}
