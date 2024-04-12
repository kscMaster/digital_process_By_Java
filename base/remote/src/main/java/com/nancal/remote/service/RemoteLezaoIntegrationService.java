package com.nancal.remote.service;

import com.nancal.remote.vo.IntegrationFileVo;
import com.nancal.remote.vo.LezaoResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@FeignClient(contextId = "remoteLezaoIntegrationService",name = "mp-integration"
//    ,url = "http://120.46.143.248/api/mp-integration"
)
public interface RemoteLezaoIntegrationService {

    /***
     * 上传文件
     *
     * @param file 请求类型参数
     */
    @PostMapping(value = "/sys/upload",headers =  {"Content-Type=multipart/form-data"})
    LezaoResult<IntegrationFileVo> upload(@RequestPart(value = "file") MultipartFile file, @RequestParam(value = "group")String group);

}
