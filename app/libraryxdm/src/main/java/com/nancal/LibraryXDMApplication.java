package com.nancal;

import cn.hutool.extra.spring.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Import(SpringUtil.class)
//@EnableJpaAuditing
@SpringBootApplication(exclude = {RedisRepositoriesAutoConfiguration.class, DataSourceAutoConfiguration.class})
@ComponentScan(basePackages = {"com.nancal.*"})
//@EntityScan("com.nancal.model.*")
//@EnableJpaRepositories("com.nancal.service.*")
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.nancal.remote.*")
@RestController
@EnableRetry
public class LibraryXDMApplication {
    public static void main(String[] args) {
        SpringApplication.run(LibraryXDMApplication.class, args);
        log.info("**XDM资源库 服务启动成功**");
    }

    @GetMapping("/health")
    public String health() {
        return "20220328231656";
    }
}

