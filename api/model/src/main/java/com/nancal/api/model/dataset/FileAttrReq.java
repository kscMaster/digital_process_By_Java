package com.nancal.api.model.dataset;

import cn.hutool.core.util.StrUtil;
import com.nancal.common.base.AddGroup;
import com.nancal.common.base.UpdateGroup;
import com.nancal.model.entity.FileStorageEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

@Data
public class FileAttrReq implements Serializable {

    @ApiModelProperty(value = "文件路径", required = true)
    @NotBlank(message = "文件路径不能为空",groups = {AddGroup.class, UpdateGroup.class})
    private String filePath;

    @ApiModelProperty(value = "文件类型")
    @NotBlank(message = "文件类型不能为空",groups = {AddGroup.class, UpdateGroup.class})
    private String fileType;

    @ApiModelProperty(value = "文件大小")
    @NotNull(message = "文件大小不能为空",groups = {AddGroup.class, UpdateGroup.class})
    @Range(min = 1, max = 256 * 1024 * 1024, message = "文件大小不能超过256M",groups = {AddGroup.class, UpdateGroup.class})
    private Long fileSize;

    @ApiModelProperty(value = "存储桶")
    private String bucketName;

    @ApiModelProperty(value = "文件名称")
    private String originFileName;

    @ApiModelProperty(value = "文件uid", required = false)
    private String uid;
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FileStorageEntity)) {
            return false;
        }
        FileStorageEntity that = (FileStorageEntity) o;
        return  StrUtil.equals(StrUtil.blankToDefault(filePath,StrUtil.EMPTY),that.getFilePath())
                && StrUtil.equals(StrUtil.blankToDefault(fileType,StrUtil.EMPTY),that.getType())
                && StrUtil.equals(StrUtil.blankToDefault(fileSize.toString(),StrUtil.EMPTY),that.getFileSize())
                ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(),filePath,fileType,fileSize.toString());
    }
}
