package com.nancal.model.config;


import cn.hutool.core.util.StrUtil;
import com.nancal.auth.util.UserUtils;
import com.nancal.model.entity.base.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;


@Configuration
@Component
public class AppAuditorAware implements AuditorAware<User> {

    @Autowired
    private UserUtils userUtils;

    @Override
    public Optional<User> getCurrentAuditor() {
        User user = new User();
        if(StrUtil.isNotBlank(userUtils.getCurrentUserId())){
            user.setCreationUserId(userUtils.getCurrentUserId());
            user.setCreationUsername(userUtils.getCurrentUserName());
            user.setLastUpdateUserId(userUtils.getCurrentUserId());
            user.setLastUpdateUsername(userUtils.getCurrentUserName());
        }

//        user.setCreationUserId("1");
//        user.setCreationUsername("admin");
//        user.setLastUpdateUserId("1");
//        user.setLastUpdateUsername("admin");
        return Optional.of(user);
    }
}
