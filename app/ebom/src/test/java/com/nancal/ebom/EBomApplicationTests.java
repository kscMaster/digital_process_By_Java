//package com.nancal.ebom;
//
//
//import com.nancal.ebom.api.model.R006PartAuxMaterialRevisionReq;
//import com.nancal.ebom.service.service.IItemDomainService;
//import com.nancal.ebom.service.service.IR006AuxMaterialDomainService;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.mock.web.MockHttpServletRequest;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.web.context.request.RequestAttributes;
//import org.springframework.web.context.request.RequestContextHolder;
//import org.springframework.web.context.request.ServletRequestAttributes;
//
//import javax.servlet.ServletRequest;
//import javax.servlet.http.HttpServletRequest;
//
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@RunWith(SpringRunner.class)
//public class EBomApplicationTests {
//
//
//    @Autowired
//    private IR006AuxMaterialDomainService ir006AuxMaterialDomainService;
//
//    private String token = "Bearer 5m1vmfr1mlmp";
//
//    @Test
//    public void test(){
//        R006PartAuxMaterialRevisionReq r006PartAuxMaterialRevisionReq = new R006PartAuxMaterialRevisionReq();
//
//        r006PartAuxMaterialRevisionReq.setObjectDesc("对象描述");
//        r006PartAuxMaterialRevisionReq.setObjectName("对象名称");
//        r006PartAuxMaterialRevisionReq.setR006SingleShotDose("r006SingleShotDose");
//        r006PartAuxMaterialRevisionReq.setR006TechCondition("r006TechCondition");
//        r006PartAuxMaterialRevisionReq.setR006TradeMark("r006TradeMark");
//        r006PartAuxMaterialRevisionReq.setObjectType("R006PartAuxMaterial");
//        r006PartAuxMaterialRevisionReq.setItemId("2212345454");
//
//        HttpServletRequest request = new MockHttpServletRequest();
//        //用户token添加用户信息
//        ((MockHttpServletRequest) request).addHeader("Authorization",token);
//
//        ((MockHttpServletRequest) request).setRequestURI("/R006PartAuxMaterial/create");
//
//        RequestAttributes requestAttributes = new ServletRequestAttributes(request);
//
//        RequestContextHolder.setRequestAttributes(requestAttributes);
//
//        ir006AuxMaterialDomainService.save(r006PartAuxMaterialRevisionReq);
//
//
//    }
//
//
//}
