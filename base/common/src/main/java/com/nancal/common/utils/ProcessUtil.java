package com.nancal.common.utils;

import cn.hutool.core.text.StrSplitter;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONUtil;
import com.nancal.common.enums.ErrorCode;
import com.nancal.common.exception.ServiceException;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Data
@RefreshScope
@Component
public class ProcessUtil {
    private static final String[] START_PROCESS_PARAM = {"processKey", "businessKey", "tenantId", "userId", "processVariables"};
    private static final String[] DELETE_PROCESS_PARAM = {"processInstanceId", "deleteReason", "delType"};
    private static final String[] CANCEL_PROCESS_PARAM = {"newActivityName", "processInstanceId", "userId", "jumpReason"};

    @Value("${flowable.url:}")
    private String url;

    /***
     * 启动流程
     *
     * @param param 启动对象
     * @param processKeys 流程key
     * @author 徐鹏军
     * @date 2022/3/29 22:58
     * @return {@link String}
     */
    public static String startProcess(Object param, String processKeys) {
        //校验参数完整
        List<String> fields = Arrays.stream(ReflectUtil.getFields(param.getClass())).map(Field::getName).collect(Collectors.toList());
        boolean allContains = Arrays.stream(START_PROCESS_PARAM).allMatch(fields::contains);
        if (!allContains) {
            throw new ServiceException(ErrorCode.PARAM_INFO_LOSE);
        }
        //校验processKey不能为空
        Object processKey = ReflectUtil.getFieldValue(param, "processKey");
        if (processKey == null) {
            throw new ServiceException(ErrorCode.FAIL, "processKey不能为空!");
        }
        //校验processKey是否在processKeys中
        List<String> keys = StrSplitter.split(processKeys, ",", true, true);
        if (keys.size() > 0 && !keys.contains(processKey.toString())) {
            throw new ServiceException(ErrorCode.FAIL, "processKey错误!");
        }
        //调用flowable
        String requestUrl = SpringUtil.getBean(ProcessUtil.class).url + "/runtime/process-instance";
        return HttpRequest.post(requestUrl)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(JSONUtil.toJsonStr(param))
                .execute().body();
    }

    /***
     * 删除流程
     *
     * @param param 删除对象
     * @author 徐鹏军
     * @date 2022/3/29 22:57
     * @return {@link String}
     */
    public static String deleteProcess(Object param) {
        //校验参数完整
        List<String> fields = Arrays.stream(ReflectUtil.getFields(param.getClass())).map(Field::getName).collect(Collectors.toList());
        boolean allContains = Arrays.stream(DELETE_PROCESS_PARAM).allMatch(fields::contains);
        if (!allContains) {
            throw new ServiceException(ErrorCode.PARAM_INFO_LOSE);
        }
        Object processInstanceId = ReflectUtil.getFieldValue(param, "processInstanceId");
        Object deleteReason = ReflectUtil.getFieldValue(param, "deleteReason");
        Object delType = ReflectUtil.getFieldValue(param, "delType");
        //processInstanceId不能为空
        if (processInstanceId == null) {
            throw new ServiceException(ErrorCode.FAIL, "processInstanceId不能为空!");
        }
        //调用flowable
        Map<String, Object> params = new HashMap<>();
        params.put("deleteReason", deleteReason);
        params.put("delType", delType);

        String requestUrl = SpringUtil.getBean(ProcessUtil.class).url + "/runtime/process-instances/" + processInstanceId;
        return HttpRequest.get(requestUrl)
                .form(params)
                .execute().body();
    }

    /***
     * 取消流程
     *
     * @param param 取消对象
     * @author 徐鹏军
     * @date 2022/3/29 22:57
     * @return {@link String}
     */
    public static String cancelProcess(Object param) {
        //校验参数完整
        List<String> fields = Arrays.stream(ReflectUtil.getFields(param.getClass())).map(Field::getName).collect(Collectors.toList());
        boolean allContains = Arrays.stream(CANCEL_PROCESS_PARAM).allMatch(fields::contains);
        if (!allContains) {
            throw new ServiceException(ErrorCode.PARAM_INFO_LOSE);
        }
        Object newActivityName = ReflectUtil.getFieldValue(param, "newActivityName");
        Object processInstanceId = ReflectUtil.getFieldValue(param, "processInstanceId");
        Object userId = ReflectUtil.getFieldValue(param, "userId");
        Object jumpReason = ReflectUtil.getFieldValue(param, "jumpReason");

        //调用flowable
        Map<String, Object> params = new HashMap<>();
        params.put("newActivityName", newActivityName);
        params.put("processInstanceId", processInstanceId);
        params.put("userId", userId);
        params.put("jumpReason", jumpReason);

        String requestUrl = SpringUtil.getBean(ProcessUtil.class).url + "/runtime/activity/move";
        return HttpRequest.get(requestUrl)
                .form(params)
                .execute().body();
    }
}
