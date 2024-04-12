package com.nancal.service.bo;

import com.nancal.api.model.FolderReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@ApiModel(value = "LibraryFolder 库文件夹 的BO")
public class LibraryFolder extends FolderReq {
    @ApiModelProperty(value = "库包含哪些类型的对象，用逗号分割。如库类型中未指定，不允许入库")
    private String libraryType;

    @ApiModelProperty(value = "显示列Key")
    private String columnKey;
}
