package com.nancal.service.service;


import cn.hutool.core.lang.Pair;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.nancal.api.utils.EntityUtil;
import com.nancal.common.enums.ErrorCode;
import com.nancal.common.exception.ServiceException;
import com.nancal.common.utils.ExpressUtil;
import com.nancal.model.entity.MsgTemplateEntity;
import com.nancal.remote.service.RemoteItMsgSystemService;
import com.nancal.remote.to.ItMessageTo;
import com.querydsl.jpa.impl.JPAQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public interface IMsgTemplateDomainService extends IWorkspaceObjectDomainService {

}
