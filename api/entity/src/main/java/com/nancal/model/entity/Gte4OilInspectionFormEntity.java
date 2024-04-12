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
@Entity(name = "gte4oil_inspection_form")
@ApiModel(value = "Gte4OilInspectionForm 油品检测委托单 的Entity")
public  class Gte4OilInspectionFormEntity  extends FormEntity  implements Serializable{
    @ApiModelProperty(value="委托单位",required=false)
    @Column(name = "gte4entrust_group", length = 128)
    private String gte4EntrustGroup;
    @ApiModelProperty(value="联系电话",required=false)
    @Column(name = "gte4tel_no", length = 32)
    private String gte4TelNo;
    @ApiModelProperty(value="油样来源",required=false)
    @Column(name = "gte4oil_source", length = 128)
    private String gte4OilSource;
    @ApiModelProperty(value="课题号/任务名称",required=false)
    @Column(name = "gte4task_no", length = 64)
    private String gte4TaskNo;
    @ApiModelProperty(value="油样牌号/名称",required=false)
    @Column(name = "gte4oil_name", length = 128)
    private String gte4OilName;
    @ApiModelProperty(value="采样部位",required=false)
    @Column(name = "gte4sample_part", length = 128)
    private String gte4SamplePart;
    @ApiModelProperty(value="采样日期",required=false,example="1994-03-07")
    @Column(name = "gte4sample_date", length = 128)
    private LocalDate gte4SampleDate;
    @ApiModelProperty(value="项目负责人",required=false)
    @Column(name = "gte4sample_responsibility", length = 128)
    private String gte4SampleResponsibility;
    @ApiModelProperty(value="送样人",required=false)
    @Column(name = "gte4sender", length = 128)
    private String gte4Sender;
    @ApiModelProperty(value="送样日期",required=false)
    @Column(name = "gte4sent_date", length = 128)
    private String gte4SentDate;
    @ApiModelProperty(value="报告签收人",required=false)
    @Column(name = "gte4receiver_id", length = 128)
    private String gte4ReceiverId;
    @ApiModelProperty(value="签收日期",required=false)
    @Column(name = "gte4received_date", length = 128)
    private String gte4ReceivedDate;
    @ApiModelProperty(value="油液固体颗粒污染度-执行标准",required=false)
    @Column(name = "gte4ppimpl_std", length = 128)
    private String gte4PPImplStd;
    @ApiModelProperty(value="油液固体颗粒污染度-评级标准1",required=false)
    @Column(name = "gte4pprtg_std1", length = 128)
    private String gte4PPRtgStd1;
    @ApiModelProperty(value="油液固体颗粒污染度-评级标准1-不低于几级",required=false)
    @Column(name = "gte4pprtg_std1gt", length = 128)
    private String gte4PPRtgStd1Gt;
    @ApiModelProperty(value="油液固体颗粒污染度-评级标准2",required=false)
    @Column(name = "gte4pprtg_std2", length = 128)
    private String gte4PPRtgStd2;
    @ApiModelProperty(value="评级标准2-内容",required=false)
    @Column(name = "gte4pprtg_std2content", length = 128)
    private String gte4PPRtgStd2Content;
    @ApiModelProperty(value="评级标准2-不低于几级",required=false)
    @Column(name = "gte4pprtg_std2gt", length = 128)
    private String gte4PPRtgStd2Gt;
    @ApiModelProperty(value="评级标准3",required=false)
    @Column(name = "gte4pprtg_std3", length = 128)
    private String gte4PPRtgStd3;
    @ApiModelProperty(value="油液固体颗粒污染度-验收标准",required=false)
    @Column(name = "gte4ppaccept_std", length = 128)
    private String gte4PPAcceptStd;
    @ApiModelProperty(value="水份-执行标准",required=false)
    @Column(name = "gte4mstr_impl_std", length = 128)
    private String gte4MstrImplStd;
    @ApiModelProperty(value="水份-具体要求",required=false)
    @Column(name = "gte4mstr_req", length = 128)
    private String gte4MstrReq;
    @ApiModelProperty(value="水份-验收标准",required=false)
    @Column(name = "gte4mstr_accept_std", length = 128)
    private String gte4MstrAcceptStd;
    @ApiModelProperty(value="运动粘度-执行标准",required=false)
    @Column(name = "gte4timpl_std", length = 128)
    private String gte4TImplStd;
    @ApiModelProperty(value="运动粘度-具体要求",required=false)
    @Column(name = "gte4treq", length = 128)
    private String gte4TReq;
    @ApiModelProperty(value="运动粘度-验收标准",required=false)
    @Column(name = "gte4taccept_std", length = 128)
    private String gte4TAcceptStd;
    @ApiModelProperty(value="密度-执行标准",required=false)
    @Column(name = "gte4pimpl_std", length = 128)
    private String gte4PImplStd;
    @ApiModelProperty(value="密度-具体要求",required=false)
    @Column(name = "gte4preq", length = 128)
    private String gte4PReq;
    @ApiModelProperty(value="密度-验收标准",required=false)
    @Column(name = "gte4paccept_std", length = 128)
    private String gte4PAcceptStd;
    @ApiModelProperty(value="开口闪光点-执行标准",required=false)
    @Column(name = "gte4ocfp_impl_std", length = 128)
    private String gte4OcfpImplStd;
    @ApiModelProperty(value="开口闪光点-具体要求",required=false)
    @Column(name = "gte4ocfp_req", length = 128)
    private String gte4OcfpReq;
    @ApiModelProperty(value="开口闪光点-验收标准",required=false)
    @Column(name = "gte4ocfp_accept_std", length = 128)
    private String gte4OcfpAcceptStd;
    @ApiModelProperty(value="闭口闪光点-执行标准",required=false)
    @Column(name = "gte4ccfp_impl_std", length = 128)
    private String gte4CcfpImplStd;
    @ApiModelProperty(value="闭口闪光点-具体要求",required=false)
    @Column(name = "gte4ccfp_req", length = 128)
    private String gte4CcfpReq;
    @ApiModelProperty(value="闭口闪光点-验收标准",required=false)
    @Column(name = "gte4ccfp_accept_std", length = 128)
    private String gte4CcfpAcceptStd;
    @ApiModelProperty(value="酸值-执行标准",required=false)
    @Column(name = "gte4ia_impl_std", length = 128)
    private String gte4IaImplStd;
    @ApiModelProperty(value="酸值-具体要求",required=false)
    @Column(name = "gte4ia_req", length = 128)
    private String gte4IaReq;
    @ApiModelProperty(value="酸值-验收标准",required=false)
    @Column(name = "gte4ia_accept_std", length = 128)
    private String gte4IaAcceptStd;
    @ApiModelProperty(value="蒸馏-执行标准",required=false)
    @Column(name = "gte4distn_impl_std", length = 128)
    private String gte4DistnImplStd;
    @ApiModelProperty(value="蒸馏-具体要求",required=false)
    @Column(name = "gte4distn_req", length = 128)
    private String gte4DistnReq;
    @ApiModelProperty(value="蒸馏-验收标准",required=false)
    @Column(name = "gte4distn_accept_std", length = 128)
    private String gte4DistnAcceptStd;
    @ApiModelProperty(value="光谱-执行标准",required=false)
    @Column(name = "gte4spectrum_impl_std", length = 128)
    private String gte4SpectrumImplStd;
    @ApiModelProperty(value="光谱-具体要求",required=false)
    @Column(name = "gte4spectrum_req", length = 128)
    private String gte4SpectrumReq;
}