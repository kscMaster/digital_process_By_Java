package com.nancal.api.model;

import cn.hutool.core.date.DateUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "DatasetFileRL 数据集文件关系 的请求")
public class EncoderReq extends WorkspaceObjectReq {

    @ApiModelProperty(value = "对象类型，不允许为空",required = true,example = "Item")
    @NotEmpty(message = "对象类型不能为空")
    private String objectType;

    @ApiModelProperty("前缀，为空时为不用前缀，只有流水码,例如：{{yyyyMMHH}}")
    private String prefix;

    @ApiModelProperty("流水码长度，为空时不在前补位0")
    private Integer length = 1;

    @ApiModelProperty(value = "申请编码个数,用于批量申请编码，默认为1",example = "3")
    private int size = 1;

    @ApiModelProperty(value = "步长，默认为1",example = "1")
    private int step = 1;

    @ApiModelProperty("流水开始码,通常首次申请该类型该前缀时有效，之后当该值大于最大编码时有效")
    private Long startSerial = 0L;

    @ApiModelProperty("流水结束码, 仅在首次申请该类型该前缀时有效,默认为所传长度个9组成，如所传长度为2时，该值默认为99")
    private Long endSerial;

    public EncoderReq(String objectType,Integer length) {
        this.objectType = objectType;
        this.prefix = DateUtil.format(LocalDateTime.now(), "yyyyMMdd");
        this.length = length;
    }

}
