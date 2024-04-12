package com.nancal.api.model;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.excel.util.StringUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.nancal.api.model.common.UserResp;
import com.nancal.api.utils.DictUtil;
import com.nancal.api.utils.EntityUtil;
import com.nancal.api.utils.FieldAlias;
import com.nancal.common.constants.Constant;
import com.nancal.common.constants.DictConstant;
import com.nancal.framework.common.annotations.JsonDict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Data
@ApiModel(value = "WorkspaceObject 工作对象 的响应VO")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class WorkspaceObjectResp extends BusinessObjectResp {

    @ApiModelProperty(value = "创建日期", example = "1994-03-07")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @FieldAlias(name = "编制日期")
    private LocalDateTime creationDate;

    @ApiModelProperty(value = "上次更新日期", example = "1994-03-07")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastUpdate;

    @JsonUnwrapped
    @ApiModelProperty("基本信息")
    private UserResp user;

    @ApiModelProperty(value = "所有者用户ID", required = true)
    private String ownerId;

    @ApiModelProperty(value = "所有者用户名")
    private String ownerName;

    @ApiModelProperty(value = "对象名称")
    private String objectName;

    @ApiModelProperty(value = "描述")
    private String objectDesc;

    @JsonDict(DictConstant.OBJECT_TYPE_NAME)
    @ApiModelProperty(value = "对象类型")
    private String objectType;

    @JsonDict(DictConstant.LIFECYCLE_STATE)
    @ApiModelProperty(value = "生命周期状态")
    private String lifeCycleState;

    @ApiModelProperty(value = "备注")
    private String remark;

    @JsonDict(DictConstant.SECURITY_CLASS_IFI)
    @ApiModelProperty(value = "密级")
    private String secretLevel;

    @ApiModelProperty("左对象id")
    private String leftObject;

    @ApiModelProperty("左对象类型")
    private String leftObjectType;

    @ApiModelProperty("右对象id")
    private String rightObject;

    @ApiModelProperty("右对象版本id")
    private String rightObjectRevId;

    @ApiModelProperty("右对象类型")
    private String rightObjectType;

    @JsonDict(DictConstant.SECRET_TERM)
    @ApiModelProperty("保密期限")
    private String secretTerm;

//    @ApiModelProperty("true有编辑图代号的权限，false没有权限")
//    private boolean hasDrawingCode;

//    @ApiModelProperty("bom行的id")
//    private String bomId;

    @ApiModelProperty("关联id")
    private String relationId;

    @ApiModelProperty("是否根节点")
    private boolean rootNode;

    @ApiModelProperty("文件")
    private FileStorageResp file;

    @Override
    public String getDisplayName() {
        DictUtil dictUtil = SpringUtil.getBean(DictUtil.class);
        String objectType;
        if (StringUtils.isNotBlank(getObjectType())){
            objectType = getObjectType();
        }else {
            objectType =   EntityUtil.getObjectType();
        }
        // 获取显示名称字典模板
        String displayNameDict = dictUtil.getDisplayNameDict(objectType);
        if (StrUtil.isBlank(displayNameDict)) {
            return getObjectName();
        }
        // 提取模板中所有的对象属性
        List<String> fields = ReUtil.findAll(Constant.$_PATTERN, displayNameDict, 1);
        if (CollUtil.isEmpty(fields)) {
            return getObjectName();
        }
        String result = null;
        for (String fieldName : fields) {
            // 获取模板中该属性的值
            Object fieldValue = ReflectUtil.getFieldValue(this, fieldName);
            String valueStr = Objects.isNull(fieldValue) ? StrUtil.EMPTY : fieldValue.toString();
            // 将模板中的规则替换成真实数据
            result = StrUtil.blankToDefault(result, displayNameDict)
                    .replace("${" + fieldName + "}", valueStr);
        }
        return result;
    }

}