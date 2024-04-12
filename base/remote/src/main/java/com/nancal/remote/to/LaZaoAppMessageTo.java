package com.nancal.remote.to;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class LaZaoAppMessageTo implements Serializable {

    @ApiModelProperty(value = "应用id", notes = "最大长度20", required = true)
    private String appCode;

    @ApiModelProperty(value = "应用名称", notes = "最大长度20")
    private String appName;

    @ApiModelProperty(value = "消息标题", notes = "最大长度20")
    private String messageTitle;

    @ApiModelProperty(value = "消息内容", notes = "最大长度20")
    private String messageContent;

    @ApiModelProperty(value = "消息分类（1、应用消息(站内消息)2、系统消息）", notes = "最大长度20")
    private Integer messageClassify;

    @ApiModelProperty(value = "租户ID", notes = "最大长度20")
    private String tenantId;

    @ApiModelProperty(value = "消息发送者", notes = "最大长度20")
    private String messageSender;

    @ApiModelProperty(value = "消息发送者名称", notes = "最大长度20")
    private String messageSenderName;

    @ApiModelProperty(value = "消息接收者", notes = "最大长度20")
    private String messageReceiver;

    @ApiModelProperty(value = "消息接受者名称", notes = "最大长度20")
    private String messageReceiverName;

    @ApiModelProperty(value = "ID", example = "0")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    @ApiModelProperty(value = "创建时间", example = "1994-03-07 12:34:56")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    protected LocalDateTime createAt;

    @ApiModelProperty(value = "更新时间", example = "1994-03-07 12:34:56")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    protected LocalDateTime updateAt;

    @ApiModelProperty(value = "创建者名称")
    protected String createUserName;

    @ApiModelProperty(value = "更新者名称")
    protected String updateUserName;

    @ApiModelProperty(value = "创建者id")
    protected String createUserId;

    @ApiModelProperty(value = "更新者id")
    protected String updateUserId;

    @ApiModelProperty(value = "八个状态", notes = "最大长度20")
    private Integer status;

    @ApiModelProperty(value = "0代表通过1代表没通过", notes = "最大长度20")
    private String passNotPass;

    @ApiModelProperty(value = "当前用户名")
    private String currentUserName;

}
