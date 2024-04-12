package com.nancal.msgbom;


import com.nancal.common.base.Response;
import com.nancal.msgbom.service.impl.BOMNodeDomainServiceImpl;
import com.nancal.remote.service.RemoteLezaoPfmanageService;
import com.nancal.remote.vo.CurrentUserInfoVo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class MSGBomApplicationTests {

    @Autowired
    private RemoteLezaoPfmanageService remoteLezaoPfmanageService;

    @Autowired
    private BOMNodeDomainServiceImpl bomNodeDomainService;

    private String authToken = "Bearer 5qdnby0fnf9d";
    private String token = "5qdnby0fnf9d";

    @Test
    public void baseTest(){
        header();
        Response<CurrentUserInfoVo> userInfo = remoteLezaoPfmanageService.getUserInfo();
        System.out.println(userInfo);

    }


    private void header(){
        HttpServletRequest request = new MockHttpServletRequest();
//        //用户token添加用户信息
        ((MockHttpServletRequest) request).addHeader("Authorization",authToken);
        ((MockHttpServletRequest) request).addHeader("token",token);

//        ((MockHttpServletRequest) request).setRequestURI("/R006PartAuxMaterial/create");

        RequestAttributes requestAttributes = new ServletRequestAttributes(request);

        RequestContextHolder.setRequestAttributes(requestAttributes);

    }


    @Test
    public void taskTime(){
        header();
        bomNodeDomainService.fullTaskTime(1,"1583141051466362880","Gte4MfgStep");
    }

}
