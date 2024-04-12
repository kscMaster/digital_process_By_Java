package com.nancal.dictsyndata.model;

import com.nancal.dictsyndata.model.LezaoDictionariesResp;
import lombok.Data;

import java.util.List;

/**
 * @author hewei
 * @date 2022/7/12 17:33
 * @Description
 */
@Data
public class SyncDictionariesRep {

    /**
     * 同步新增集合
     */
    private  List<LezaoDictionariesTypeResp>  insertDictTypeList;


    /**
     * 同步修改集合
     */
    private List<LezaoDictionariesTypeResp>  updateDictTypeList;

    /**
     * 同步删除集合
     */
    private List<LezaoDictionariesTypeResp>  deleteDictTypeList;

}
