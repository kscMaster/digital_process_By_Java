package com.nancal.remote.service;

import com.nancal.common.base.Response;
import com.nancal.remote.to.BOMNodeViewTo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Nancal.com Inc.
 * Copyright (c) 2021- All Rights Reserved.
 *
 * @Author yangtz
 * @Date 2021/12/8 16:13
 * @Description
 */
@FeignClient(contextId = "remoteEsopService", name = "lz624-esop"
//        , url = "http://120.46.143.248/api/lz624-esop"
)
public interface RemoteEsopService {


    @PostMapping("/ESOP/generateWI")
    Response esop(@RequestBody BOMNodeViewTo viewResp);

}
