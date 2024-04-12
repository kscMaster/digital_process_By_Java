package com.nancal.remote.service;

import com.nancal.common.base.Response;
import com.nancal.remote.vo.CurrentUserInfoVo;
import com.nancal.remote.to.DeptMemberTo;
import com.nancal.remote.vo.DeptVo;
import com.nancal.remote.vo.LeZaoApplicationRoleVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @ClassName RemoteLezaoPfUserService
 * @Description 用户信息远程调用
 * @Author fuming
 * @Date 2022/2/23 15:01
 * @Version 1.0
 **/
@FeignClient(contextId = "RemoteMpTenantAdminService", name = "mp-tenant-admin-api"
//        ,url = "http://120.46.143.248/api/mp-tenant-admin-api"
 )
public interface RemoteMpTenantAdminService {

    /***
     * 根据部门获取下面人员信息
     *
     * @param to 请求参数
     * @author 徐鹏军
     * @date 2022/3/29 23:27
     * @return {@link Response<LeZaoApplicationRoleVo>}
     */
    @PostMapping("/tenant/tenant-user/list")
    Response<List<CurrentUserInfoVo>> getMembersByDeptId(@RequestBody DeptMemberTo to);


    /**
     * 获取部门责任人
     * @param id 部门id
     * @return
     */
    @PostMapping("/tenant/department-info/detail/{id}")
    Response<DeptVo> getDeptManagers(@PathVariable(value = "id") String id);


}
