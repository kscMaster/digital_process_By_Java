package com.nancal.remote.to;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "消息发送")
public class ItMessageTo {

    @ApiModelProperty(value = "应用名称")
    private String applicationName = "os";
    @ApiModelProperty(value = "'1:站内信   2:邮件   3:app推送   4:短信'")
    private String msgType;
    @ApiModelProperty(value = "站内信消息类型'1：产品消息   2：运维消息  3：活动消息   4 服务消息'")
    private String mailType;

    @ApiModelProperty(value = "标题")
    private String title;
    @ApiModelProperty(value = "主题")
    private String body;
    @ApiModelProperty(value = "用户id组合")
    private List<String> userIds;
    @ApiModelProperty(value = "发送渠道 1：站内信   2：短信  3：app  4: 微信公众号   5：邮件")
    private String channelType;

}
