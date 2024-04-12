package com.nancal.model.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Data
@SuperBuilder
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "file_storage")
@ApiModel(value = "FileStorage 文件存储 的Entity")
public  class FileStorageEntity  extends WorkspaceObjectEntity  implements Serializable{
    @ApiModelProperty(value="原始文件名",required=false)
    @Column(name = "origin_file_name", length = 128)
    private String originFileName;
    @ApiModelProperty(value="文件后缀",required=false)
    @Column(name = "file_ext", length = 64)
    private String fileExt;
    @ApiModelProperty(value="文件路径",required=false)
    @Column(name = "file_path", length = 128)
    private String filePath;
    @ApiModelProperty(value="文件大小",required=false)
    @Column(name = "file_size", length = 64)
    private String fileSize;
    @ApiModelProperty(value="mime类型",required=false)
    @Column(name = "mime_type", length = 64)
    private String mimeType;
    @ApiModelProperty(value="文件类型",required=false)
    @Column(name = "type", length = 64)
    private String type;
    @ApiModelProperty(value="桶名称",required=false)
    @Column(name = "bucket_name", length = 128)
    private String bucketName;
    public String getObjectType() {
        return "FileStorage";
    }
}