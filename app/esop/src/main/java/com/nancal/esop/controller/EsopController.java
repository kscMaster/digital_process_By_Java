package com.nancal.esop.controller;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.system.SystemUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.nancal.api.model.BOPNodeViewResp;
import com.nancal.common.base.Response;
import com.nancal.esop.service.EsopGeneratorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.exec.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/ESOP")
@Api(tags = "ESOP管理")
public class EsopController {

    @Autowired
    private EsopGeneratorService esopGeneratorService;

    @ApiOperation(value = "生成ESOP")
    @PostMapping("/generateWI")
    public Response generateWI(@RequestBody BOPNodeViewResp viewResp) {
        // 开始生成ESOP
        esopGeneratorService.generateEsop(viewResp);
        return Response.of();
    }

    @ApiOperation(value = "测试Python执行")
    @PostMapping("/test1")
    public Response test1(@RequestBody List<String> ids) throws IOException {
        String pythonExePath = "C:\\Program Files\\Python38\\python";
        String scriptPath = "D:\\idea_workspace\\digital_process\\app\\esop\\src\\main\\resources\\test01.py";
//        scriptPath = "D:\\idea_workspace\\digital_process\\app\\esop\\src\\main\\resources\\test02.py";
        CommandLine cmdLine = new CommandLine(pythonExePath);
        cmdLine.addArgument(scriptPath);

        cmdLine.addArguments(ids.toArray(new String[]{}));
        DefaultExecutor executor = new DefaultExecutor();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ByteArrayOutputStream errorStream = new ByteArrayOutputStream();
        PumpStreamHandler streamHandler = new PumpStreamHandler(outputStream, errorStream);
        executor.setStreamHandler(streamHandler);
        // 执行1分钟
        ExecuteWatchdog watchdog = new ExecuteWatchdog(60 * 1000);
        try {
            executor.setWatchdog(watchdog);
            int statusCode = executor.execute(cmdLine);
            String out = outputStream.toString(CharsetUtil.GBK);
            String error = errorStream.toString(CharsetUtil.GBK);
            System.out.println("out:" + out);
            System.out.println("error:" + error);
            System.out.println("状态码：" + statusCode);
        } catch (ExecuteException e) {
            if (watchdog.killedProcess()) {
                // 被watchdog故意杀死
                log.error("线平衡算法执行超时",e);
            }
        }
        return Response.of();
    }

    public static void main(String[] args) {
        String data = "['100', '300']";
        JSONArray objects = JSON.parseArray(data);
        System.out.println(objects.toString());
        List<String> arrayLists = JSON.parseArray(data, String.class);
        System.out.println(arrayLists.toString());

        System.out.println(SystemUtil.getUserInfo().getCurrentDir());
        System.out.println(SystemUtil.getUserInfo().getHomeDir());


        File directory = new File("src/main/resources/test01.py");
        System.out.println(directory.getAbsolutePath());
    }

}
