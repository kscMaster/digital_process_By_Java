package com.nancal.service.bo;

import com.nancal.api.model.IncludeRLReq;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@ApiModel(value = "LibraryIncludeRL 库包含关系 的请求")
public class LibraryIncludeRL extends IncludeRLReq {

    @Override
    public String getRelationType() {
        return "LibraryIncludeRL";
    }

    @Override
    public String getObjectType() {
        return "LibraryIncludeRL";
    }
}
