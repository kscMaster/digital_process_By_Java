package com.nancal.service.factory;

import com.nancal.service.bo.ProcessInstanceRL;
import org.springframework.stereotype.Component;

@Component
public class ProcessInstanceRLFactory {
    public ProcessInstanceRL create() {
        return new ProcessInstanceRL();
    }
}