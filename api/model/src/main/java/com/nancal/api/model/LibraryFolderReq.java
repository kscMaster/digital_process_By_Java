package com.nancal.api.model;

import com.nancal.common.base.AddGroup;
import com.nancal.common.base.UpdateGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

@Data
@ToString
@ApiModel(value = "LibraryFolder 库文件夹 的请求")
public class LibraryFolderReq extends FolderReq {
    @Length(max = 64,message = "类型长度超过了最大限制",groups = {AddGroup.class,UpdateGroup.class})
    @NotNull(message = "类型不能为空",groups = {AddGroup.class,UpdateGroup.class})
    @ApiModelProperty(value = "类型",required = true)
    private String libraryType;

//    @Length(max = 2048,message = "显示列key长度超过了最大限制",groups = {AddGroup.class,UpdateGroup.class})
    @NotNull(message = "显示列key不能为空",groups = {AddGroup.class,UpdateGroup.class})
    @ApiModelProperty(value = "显示列Key",required = true)
    private String columnKey;

    @ApiModelProperty(value = "资源库icon",required = true)
    private String icon;

}
