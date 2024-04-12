package com.nancal.model.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import javax.persistence.*;
import java.io.Serializable;


@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Data
@SuperBuilder
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "gte4tech_document_revision")
@ApiModel(value = "Gte4TechDocumentRevision 技术文档版本 的Entity")
public  class Gte4TechDocumentRevisionEntity  extends DocumentRevisionEntity  implements Serializable{
    @ApiModelProperty(value="文件类型",required=false)
    @Column(name = "gte4file_type", length = 128)
    private String gte4FileType;
    @ApiModelProperty(value="初始型号",required=false)
    @Column(name = "gte4init_model", length = 64)
    private String gte4InitModel;
    @ApiModelProperty(value="阶段标识",required=false)
    @Column(name = "gte4phase", length = 64)
    private String gte4Phase;
    @ApiModelProperty(value="设计批次",required=false)
    @Column(name = "gte4dzn_bn", length = 64)
    private String gte4DznBn;
    @ApiModelProperty(value="文件号",required=false)
    @Column(name = "gte4file_no", length = 64)
    private String gte4FileNo;
    @ApiModelProperty(value="设计者",required=false)
    @Column(name = "gte4designer", length = 128)
    private String gte4Designer;

    @Override
    public String getObjectType(){
        return "Gte4TechDocumentRevision";
    }
}