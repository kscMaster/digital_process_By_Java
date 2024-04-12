package com.nancal.remote.service;


import cn.hutool.json.JSONObject;
import com.nancal.common.base.Response;
import com.nancal.common.base.TableResponse;
import com.nancal.remote.to.FlowableTableTo;
import com.nancal.remote.to.ProcessPageQueryTo;
import com.nancal.remote.to.StartFlowableTo;
import com.nancal.remote.to.TaskFlowableTo;
import com.nancal.remote.vo.NodeInfoVo;
import com.nancal.remote.vo.ProcessTaskNodeVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/***
 * 流程中心远程调用接口
 *
 * @author 徐鹏军
 * @date 2022/3/29 23:25
 */
@FeignClient(contextId = "remoteLzosFlowableService", name = "wf"
//        ,url = "http://120.46.143.248/api/wf"
)
public interface RemoteLezaoFlowableService {

    /**
     * 启动流程
     */
    @PostMapping("/runtime/process-instance")
    Response<String> startFlowable(@RequestBody @Validated StartFlowableTo vo);

    /**
     * 任务处理
     */
    @PostMapping("/runtime/task")
    JSONObject taskFlowable(@RequestBody @Validated TaskFlowableTo vo);

    /**
     * 获取任务列表
     */
    @PostMapping("/runtime/tasks")
    JSONObject taskFlowableList(FlowableTableTo flowabletable);

    /**
     * 获取流程节点信息
     *
     * @param modelKey 流程标识key
     * @return Response<List < NodeInfoVo>>
     */
    @GetMapping("/runtime/process-instances/nodes/{modelKey}")
    Response<List<NodeInfoVo>> getProcessNode(@PathVariable(value = "modelKey") String modelKey);

    /**
     * 分页查询流程节点信息
     *
     * @param processInstanceId  流程实例id
     * @param processPageQueryTo 分页查询条件
     * @return Response<List < ProcessTaskNodeVo>>
     */
    @PostMapping("/history/tasks/{processInstanceId}/page")
    TableResponse<ProcessTaskNodeVo> getProcessNodePage(@PathVariable(value = "processInstanceId") String processInstanceId, @RequestBody @Validated ProcessPageQueryTo processPageQueryTo);

    /**
     * 启动流程并自动办理任务
     */
    @PostMapping("/runtime/process-instance/task")
    Response<String> startFlowableAndDealTask(@RequestBody @Validated StartFlowableTo vo);

}
