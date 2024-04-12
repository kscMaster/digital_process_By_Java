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
@Entity(name = "object_index")
@ApiModel(value = "ObjectIndex 索引对象 的Entity")
public  class ObjectIndexEntity  extends AdminEntity  implements Serializable{
    @ApiModelProperty(value="对象UID",required=false)
    @Column(name = "object_uid", length = 64)
    private String objectUid;
    @ApiModelProperty(value="编号",required=false)
    @Column(name = "item_id", length = 64)
    private String itemId;
}