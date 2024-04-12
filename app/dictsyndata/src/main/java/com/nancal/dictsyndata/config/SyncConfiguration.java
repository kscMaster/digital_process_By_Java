package com.nancal.dictsyndata.config;

import com.nancal.dictsyndata.model.SyncDictionariesRep;
import com.nancal.dictsyndata.service.DictionariesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author hewei
 * @date 2022/7/20 15:34
 * @Description
 */
@Configuration
public class SyncConfiguration  {


    @Autowired
    private DictionariesService dictionariesService;

    @Value("${tenant.current}")
    private String currentTenant;


//    @Bean
    public void syncData(){
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
    }
}
