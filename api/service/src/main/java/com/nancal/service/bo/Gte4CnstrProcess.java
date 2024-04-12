package com.nancal.service.bo;


import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

@Data
@ApiModel(value = "Gte4CnstrProcess 施工过程卡 的BO")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Gte4CnstrProcess extends Gte4MfgProcess  implements Serializable{



    @Override
    public String getObjectType(){
        return "Gte4CnstrProcess";
    }

}