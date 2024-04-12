package com.nancal.remote.service;

import com.nancal.common.base.Response;
import com.nancal.remote.to.BOMNodeViewTo;
import com.nancal.remote.to.ItMessageTo;
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
@FeignClient(contextId = "remoteItMsgSystemService", name = "it-msg-system"
//        , url = "http://120.46.143.248/api/it-msg-system"
)
public interface RemoteItMsgSystemService {

    /**
     * 发送站内消息
     * @param itMessageTo
     * @return
     */
    @PostMapping("/message/add")
    Response messageAdd(@RequestBody ItMessageTo itMessageTo);

}
