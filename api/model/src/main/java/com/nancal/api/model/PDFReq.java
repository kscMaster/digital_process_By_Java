package com.nancal.api.model;


import cn.hutool.core.util.StrUtil;
import com.nancal.api.model.dataset.FileAttrReq;
import com.nancal.common.base.AddGroup;
import com.nancal.common.base.UpdateGroup;
import com.nancal.model.entity.PDFEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;

@Data
@ApiModel(value = "PDF PDF数据集 的请求")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class PDFReq extends DatasetReq {

    @ApiModelProperty(value = "当前节点id", required = true)
    @NotBlank(message = "节点id不能为空",groups = {UpdateGroup.class})
    private String uid;

    @NotBlank(message = "左对象类型不能为空",groups = {AddGroup.class, UpdateGroup.class})
    @Length(max = 64, message = "左对象类型超过了最大长度限制",groups = {AddGroup.class, UpdateGroup.class})
    @ApiModelProperty(value = "左对象对象类型", required = true)
    private String leftObjectType;

    @NotBlank(message = "左对象对象id不能为空",groups = {AddGroup.class, UpdateGroup.class})
    @Length(max = 64, message = "左对象对象id超过了最大长度限制",groups = {AddGroup.class, UpdateGroup.class})
    @ApiModelProperty(value = "左对象对象id", required = true)
    private String leftObject;

    @NotBlank(message = "名称不能为空",groups = {AddGroup.class, UpdateGroup.class})
    @Length(max = 128, message = "名称超过最大长度128",groups = {AddGroup.class, UpdateGroup.class})
    @ApiModelProperty(value = "名称", required = true)
    private String objectName;

    @Length(max = 512, message = "描述超过最大长度512",groups = {AddGroup.class, UpdateGroup.class})
    @ApiModelProperty(value = "描述", required = true)
    private String objectDesc;

    @NotBlank(message = "密级不能为空",groups = {AddGroup.class, UpdateGroup.class})
    @ApiModelProperty(value = "密级", required = true)
    private String secretLevel;

    @ApiModelProperty(value = "文件数据，数组形式传参",required = true)
    @Valid
    @NotNull(message = "文件不能为空")
    private List<FileAttrReq> files;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PDFEntity)) {
            return false;
        }
        PDFEntity that = (PDFEntity) o;
        return  StrUtil.equals(StrUtil.blankToDefault(objectName,StrUtil.EMPTY),that.getObjectName())
                && StrUtil.equals(StrUtil.blankToDefault(objectDesc,StrUtil.EMPTY),that.getObjectDesc())
                && StrUtil.equals(StrUtil.blankToDefault(secretLevel,StrUtil.EMPTY),that.getSecretLevel())
                ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(),objectName,objectDesc,secretLevel);
    }
}