package com.nancal.api.model;


import cn.hutool.core.util.StrUtil;
import com.nancal.common.base.AddGroup;
import com.nancal.common.base.UpdateGroup;
import com.nancal.model.entity.FolderEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.util.Objects;

@Data
@ApiModel(value = "Folder 文件夹 的请求")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class FolderReq extends WorkspaceObjectReq {

    @ApiModelProperty(value = "排序顺序")
    private String sort;

    @ApiModelProperty(value = "排序准则")
    private String criteria;

    @NotBlank(message = "文件夹名称不能为空",groups = {AddGroup.class, UpdateGroup.class})
    @Length(max = 128,message = "对象名称长度超过限制",groups = {AddGroup.class, UpdateGroup.class})
    @ApiModelProperty(value = "对象名称", required = true)
    private String objectName;

    @ApiModelProperty(value = "描述",required = true)
    @Length(max = 512,message = "描述长度超过限制",groups = {AddGroup.class, UpdateGroup.class})
    private String objectDesc;

    @NotBlank(message = "uid不能为空",groups = {UpdateGroup.class})
    @Length(max = 64, message = "uid超过了最大长度限制",groups = {UpdateGroup.class})
    @ApiModelProperty(value = "主键uid",required = true)
    private String uid;

    @ApiModelProperty(value = "左对象对象类型")
    @NotBlank(message = "左对象对象类型不能为空",groups = {AddGroup.class})
    private String leftObjectType;

    @NotBlank(message = "左对象不能为空",groups = {AddGroup.class})
    @ApiModelProperty(value = "左对象对象id")
    private String leftObject;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FolderEntity)) {
            return false;
        }
        FolderEntity that = (FolderEntity) o;
        return  StrUtil.equals(StrUtil.blankToDefault(objectName,StrUtil.EMPTY),that.getObjectName())
                && StrUtil.equals(StrUtil.blankToDefault(objectDesc,StrUtil.EMPTY),that.getObjectDesc())
                ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(),objectName,objectDesc);
    }

}