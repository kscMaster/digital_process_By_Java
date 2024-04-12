package com.nancal.common.config;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author hewei
 * @date 2022/11/1 17:40
 * @Description
 */
@Data
public class TokenInfo {

    private String token;

    private String tenantId;

    private Object useCache;

    private MultipartFile file;

}
