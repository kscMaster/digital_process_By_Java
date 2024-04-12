package com.nancal.service.factory;

import com.nancal.service.bo.ProcessInstance;
import org.springframework.stereotype.Component;

@Component
public class ProcessInstanceFactory {
    public ProcessInstance create() {
        return new ProcessInstance();
    }
}
