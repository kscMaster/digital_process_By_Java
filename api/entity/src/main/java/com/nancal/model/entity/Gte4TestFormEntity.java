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
@Entity(name = "gte4test_form")
@ApiModel(value = "Gte4TestForm 试验表单记录 的Entity")
public  class Gte4TestFormEntity  extends FormEntity  implements Serializable{
    @ApiModelProperty(value="开始时间",required=false,example="1994-03-07")
    @Column(name = "gte4start_date", length = 128)
    private LocalDate gte4StartDate;
    @ApiModelProperty(value="结束时间",required=false,example="1994-03-07")
    @Column(name = "gte4end_date", length = 128)
    private LocalDate gte4EndDate;
    @ApiModelProperty(value="合计运转时间",required=false)
    @Column(name = "gte4tot_op_time", length = 128)
    private String gte4TotOpTime;
    @ApiModelProperty(value="合计燃机时间",required=false)
    @Column(name = "gte4tot_turb_time", length = 128)
    private String gte4TotTurbTime;
    @ApiModelProperty(value="文件号",required=false)
    @Column(name = "gte4file_no", length = 64)
    private String gte4FileNo;
}