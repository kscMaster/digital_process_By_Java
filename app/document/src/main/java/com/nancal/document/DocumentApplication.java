package com.nancal.document;

import cn.hutool.extra.spring.SpringUtil;
import com.nancal.service.service.IApiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Import(SpringUtil.class)
@EnableJpaAuditing
@SpringBootApplication(scanBasePackages = "com.nancal.*", exclude = RedisRepositoriesAutoConfiguration.class)
@EntityScan("com.nancal.model.*")
@EnableJpaRepositories("com.nancal.service.*")
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.nancal.remote.*")
@RestController
public class DocumentApplication {

    public static void main(String[] args) {
        SpringApplication.run(DocumentApplication.class, args);
        log.info("**技术文档管理 服务启动成功**");
    }

    @GetMapping("/health")
    public String health() {
        return "20220328231656";
    }

    @GetMapping("/api")
    public void api(){
        SpringUtil.getBean(IApiService.class).api();
    }

}