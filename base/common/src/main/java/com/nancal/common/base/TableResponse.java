package com.nancal.common.base;

import com.nancal.common.enums.ErrorCode;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.List;


@Data
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class TableResponse<T> extends Message {

    @ApiModelProperty(value = "总记录数", example = "1000")
    private long total;

    @ApiModelProperty(value = "列表")
    private List<T> data;

    @Builder
    public TableResponse(long total, List<T> data) {
        super(ErrorCode.OK.code(), ErrorCode.OK.msg());
        this.total = total;
        this.data = data;
    }
}
