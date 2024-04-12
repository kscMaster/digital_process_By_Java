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
@Entity(name = "document_revision")
@ApiModel(value = "DocumentRevision 文档版本 的Entity")
public  class DocumentRevisionEntity  extends ItemRevisionEntity  implements Serializable{
    @ApiModelProperty(value="文档标题",required=false)
    @Column(name = "document_title", length = 128)
    private String documentTitle;
    @ApiModelProperty(value="文档作者",required=false)
    @Column(name = "document_author", length = 64)
    private String documentAuthor;
    @ApiModelProperty(value="文档主题",required=false)
    @Column(name = "document_subject", length = 128)
    private String documentSubject;
    @ApiModelProperty(value="文档内容",required=false)
    @Column(name = "document_content", length = 1024)
    private String documentContent;
}