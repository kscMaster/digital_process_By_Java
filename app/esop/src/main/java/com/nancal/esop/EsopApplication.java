package com.nancal.esop;

import cn.hutool.extra.spring.SpringUtil;
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
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Import(SpringUtil.class)
@EnableJpaAuditing
@SpringBootApplication(scanBasePackages = "com.nancal.*", exclude = RedisRepositoriesAutoConfiguration.class)
@EntityScan("com.nancal.esop.entity")
@EnableJpaRepositories("com.nancal.esop.repository")
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.nancal.remote.*")
@RestController
@EnableRetry
public class EsopApplication {
    public static void main(String[] args) {
        SpringApplication.run(EsopApplication.class, args);
        log.info("**Esop 服务启动成功**");
    }

    @GetMapping("/health")
    public String health() {
        return "20220328231656";
    }
}

