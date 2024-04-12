package com.nancal.remote.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author： Wang Hui
 * @date： 2022/5/3 12:43
 * @description： 节点信息
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NodeInfoVo implements Serializable {

    private static final long serialVersionUID = -5979355071565683820L;

    @ApiModelProperty(value = "流程名称")
    private String processName;

    @ApiModelProperty(value = "节点名称")
    private String name;

    @ApiModelProperty(value = "节点key")
    private String nodeKey;

    @ApiModelProperty(value = "子节点")
    private String child;
}
