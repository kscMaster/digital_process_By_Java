package com.nancal.remote.vo;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.collections.map.HashedMap;

import javax.persistence.Transient;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author: Wang Hui
 * @create: 2022-05-04 21:21
 * @description: 分页查询流程实例的节点信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProcessTaskNodeVo  implements Serializable {

    private static final long serialVersionUID = -7855288837786295009L;

    @ApiModelProperty(value = "任务委托人")
    private String assignee;

    @ApiModelProperty(value = "业务标识")
    private String businessKey;

    @ApiModelProperty(value = "创建时间")
    private String createTime;

    @ApiModelProperty(value = "结束时间")
    private String endTime;

    @ApiModelProperty(value = "任务名称")
    private String name;

    @ApiModelProperty(value = "流程实例id")
    private String processInstanceId;

    @ApiModelProperty(value = "流程实例变量")
    private Map<String, Object> processVariables;

    @ApiModelProperty(value = "任务id")
    private String taskId;

    @ApiModelProperty(value = "流程任务变量")
    private Map<String, Object> taskLocalVariables;

    @ApiModelProperty(value = "意见")
    private String comment;

    @ApiModelProperty(value = "附件")
    private List<Map<String, Object>> attachmentVOS;
    @ApiModelProperty(value = "额外自定义参数", hidden = true)
    @Transient
    protected Map extraMap;

    @JsonAnyGetter
    public Map getExtraMap() {
        return extraMap;
    }

    public void putExtraProperty(Object name, Object value) {
        if (name == null || value == null) {
            return ;
        }
        if (extraMap == null) {
            extraMap = new HashedMap();
        }
        extraMap.put(name, value);
        return ;
    }
    @JsonIgnore
    public <T> T getExtraProperty(Object name, T defaultValue) {
        T value = getExtraProperty(name);
        return value == null ? defaultValue : value;
    }
    @JsonIgnore
    public <T> T getExtraProperty(Object name) {
        return (name == null || extraMap == null) ? null : (T) extraMap.get(name);
    }

    @JsonIgnore
    public <T> T getExtraPropertyDesc(Object name) {
        return (name == null || extraMap == null) ? null : (T) extraMap.get(name+"Desc");
    }

}
