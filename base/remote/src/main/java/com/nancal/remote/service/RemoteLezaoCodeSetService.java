package com.nancal.remote.service;

import com.nancal.remote.to.LezaoCodingTo;
import com.nancal.remote.vo.LezaoResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(contextId = "remoteLezaoDictService",name = "lezao-code-service"
//    ,url = "http://120.46.143.248/api/lezao-code-service"
)
public interface RemoteLezaoCodeSetService {

    /***
     * 根据code规则查询code编码
     *
     * @param to 请求类型参数
     */
    @PostMapping("/lezaoCodingRules/getSpecificationCodeList")
    LezaoResult<List<String>> getSpecificationCodeList(@RequestBody LezaoCodingTo to);


}
