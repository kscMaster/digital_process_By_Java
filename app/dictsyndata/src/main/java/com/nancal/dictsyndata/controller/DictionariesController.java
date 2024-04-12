package com.nancal.dictsyndata.controller;

import com.nancal.common.base.Response;
import com.nancal.dictsyndata.model.LezaoDictionariesTypeResp;
import com.nancal.dictsyndata.model.SyncDictionariesRep;
import com.nancal.dictsyndata.service.DictionariesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @author hewei
 * @date 2022/7/12 15:21
 * @Description
 */
@RestController
@RequestMapping("/dictionaries")
public class DictionariesController {

    @Autowired
    private DictionariesService dictionariesService;


    @Value("${tenant.current}")
    private String currentTenant;

    /**
     * 计算同步数据
     * @return
     */
    @GetMapping("sync")
    public Response<String> computeSync(){
        switch (currentTenant){
            case "dev":
                SyncDictionariesRep syncDictionariesRep = dictionariesService.computeSync(dictionariesService.getDict214Data(), dictionariesService.getDict213Data());
                dictionariesService.testSync(syncDictionariesRep.getInsertDictTypeList(),syncDictionariesRep.getUpdateDictTypeList(),syncDictionariesRep.getDeleteDictTypeList());
                break;
            case "test":
                SyncDictionariesRep syncDictionariesRep1 = dictionariesService.computeSync(dictionariesService.getDict213Data(), dictionariesService.getDict214Data());
                dictionariesService.devSync(syncDictionariesRep1.getInsertDictTypeList(),syncDictionariesRep1.getUpdateDictTypeList(),syncDictionariesRep1.getDeleteDictTypeList());
                break;
            default:
                break;
        }
        return Response.of();
    }




}
