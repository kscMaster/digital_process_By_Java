package com.nancal.api.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.apache.commons.collections.map.HashedMap;

import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Map;


@ApiModel(value = "BusinessObject 业务对象 的请求")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public abstract class BusinessObjectResp implements Serializable {

    @ApiModelProperty(value = "显示名称")
    private String displayName;

    @ApiModelProperty(value = "主键id")
    private String uid;


    @ApiModelProperty(value = "额外自定义参数", hidden = true)
    @Transient
    protected Map extraMap;

    @JsonAnyGetter
    public Map getExtraMap() {
        return extraMap;
    }

    public BusinessObjectResp putExtraProperty(Object name, Object value) {
        if (name == null || value == null) {
            return this;
        }
        if (extraMap == null) {
            extraMap = new HashedMap();
        }
        extraMap.put(name, value);
        return this;
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
