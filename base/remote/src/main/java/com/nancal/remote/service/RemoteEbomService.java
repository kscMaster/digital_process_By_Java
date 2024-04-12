package com.nancal.remote.service;

import com.nancal.common.base.Response;
import com.nancal.remote.to.MsgBomTo;
import com.nancal.remote.vo.FindVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * Nancal.com Inc.
 * Copyright (c) 2021- All Rights Reserved.
 *
 * @Author yangtz
 * @Date 2021/12/8 16:13
 * @Description
 */
@FeignClient(contextId = "remoteMsgBomService", name = "lz624-ebom"
//        , url = "http://120.46.143.248/api/lz624-ebom"
)
public interface RemoteEbomService {


    @PostMapping("/BOMNode/find")
    Response<List<FindVo>> find(@RequestBody MsgBomTo to);

}
