package com.nancal.service.bo;

import cn.hutool.core.lang.Pair;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.nancal.api.utils.EntityUtil;
import com.nancal.common.enums.AppNameEnum;
import com.nancal.model.entity.BOMNodeEntity;
import com.nancal.model.entity.BOMViewEntity;
import com.nancal.model.entity.WorkspaceObjectEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Arrays;
import java.util.List;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "BOMView BOM视图 的BO")
public class BOMView extends WorkspaceObject {
    @ApiModelProperty(value = "是否精确")
    private Boolean precise;

    @ApiModelProperty(value = "视图类型")
    private String viewType;

    public boolean checkPrecise(AppNameEnum appNameEnum){
        List<Pair<String, Object>> params = Arrays.asList(Pair.of(
                BOMViewEntity.VIEW_TYPE, appNameEnum.name())
        );
        WorkspaceObjectEntity entity = EntityUtil.getDynamicEqQuery(new BOMViewEntity().getObjectType(), params).fetchFirst();
        if(ObjectUtil.isNull(entity)){
            return false;
        }
        BOMViewEntity bomView = (BOMViewEntity)entity;
        return bomView.getIsPrecise();
    }

}
