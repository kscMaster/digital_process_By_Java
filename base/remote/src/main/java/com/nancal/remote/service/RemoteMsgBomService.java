package com.nancal.remote.service;

import com.nancal.common.base.Response;
import com.nancal.remote.to.MsgBomTo;
import com.nancal.remote.vo.MsgBomVo;
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
@FeignClient(contextId = "remoteMsgBomService", name = "lz624-msgbom"
//        , url = "http://120.46.143.248/api/lz624-msgbom"
)
public interface RemoteMsgBomService {


    @PostMapping("/Gte4MfgProcess/findMfgTargetRL")
    Response<MsgBomVo> findMfgTargetRL(@RequestBody MsgBomTo to);

}
