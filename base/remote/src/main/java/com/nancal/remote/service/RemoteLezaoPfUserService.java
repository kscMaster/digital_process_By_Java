//package com.nancal.remote.service;
//
//import com.nancal.common.base.Response;
//import com.nancal.remote.to.RoleDetailParamTo;
//import com.nancal.remote.vo.LeZaoApplicationRoleVo;
//import org.springframework.cloud.openfeign.FeignClient;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//
///**
// * @ClassName RemoteLezaoPfUserService
// * @Description 用户信息远程调用
// * @Author fuming
// * @Date 2022/2/23 15:01
// * @Version 1.0
// **/
//@FeignClient(contextId = "RemoteLezaoUserService", name = "lzos-user-manager"
//        ,url = "http://192.168.5.213/api/lzos-user-manager"
// )
//public interface RemoteLezaoPfUserService {
//
//    /***
//     * 查看角色详情，包括角色下用户
//     *
//     * @param roleDetailParamTo 请求参数
//     * @author 徐鹏军
//     * @date 2022/3/29 23:27
//     * @return {@link Response<LeZaoApplicationRoleVo>}
//     */
//    @PostMapping("/rolePermission/roles/detail")
//    Response<LeZaoApplicationRoleVo> findRoleDetail(@RequestBody RoleDetailParamTo roleDetailParamTo);
//
//}
