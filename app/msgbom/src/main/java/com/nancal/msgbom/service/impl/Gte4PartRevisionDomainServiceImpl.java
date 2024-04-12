package com.nancal.msgbom.service.impl;


import cn.hutool.extra.spring.SpringUtil;
import com.nancal.api.model.*;
import com.nancal.api.utils.EntityUtil;
import com.nancal.api.utils.TranslateUtil;
import com.nancal.common.annotation.TimeLog;
import com.nancal.common.base.IdRequest;
import com.nancal.common.enums.ErrorCode;
import com.nancal.common.exception.ServiceException;
import com.nancal.common.utils.BeanUtil;
import com.nancal.model.entity.BOMNodeEntity;
import com.nancal.model.entity.WorkspaceObjectEntity;
import com.nancal.service.factory.ItemFactory;
import com.nancal.service.service.IGte4PartRevisionDomainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;


@Service
public class Gte4PartRevisionDomainServiceImpl implements IGte4PartRevisionDomainService {
}
