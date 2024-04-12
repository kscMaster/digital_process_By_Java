package com.nancal.dictsyndata;

import cn.hutool.extra.spring.SpringUtil;
import com.nancal.common.base.Response;
import com.nancal.dictsyndata.model.LezaoDictionariesTypeResp;
import com.nancal.dictsyndata.model.SyncDictionariesRep;
import com.nancal.dictsyndata.service.DictionariesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@Import(SpringUtil.class)
@EnableJpaAuditing
@SpringBootApplication(scanBasePackages = "com.nancal.*", exclude = DataSourceAutoConfiguration.class)
@EntityScan({"com.nancal.dictsyndata.entity"})
//@EnableJpaRepositories({"com.nancal.dictsyndata.dao"})
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.nancal.remote.*"})
@RestController
@ComponentScan({"com.nancal"})
@EnableCaching
public class DictSynDataApplication {

    public static void main(String[] args) {
        SpringApplication.run(DictSynDataApplication.class, args);
        log.info("**字典同步 服务启动成功**");
    }


    @Autowired
    private DictionariesService dictionariesService;

    @Value("${tenant.current}")
    private String currentTenant;

    @Bean
    public void sync(){
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



    @GetMapping("/health")
    public String health() {
        return "20220328231656";
    }
}
