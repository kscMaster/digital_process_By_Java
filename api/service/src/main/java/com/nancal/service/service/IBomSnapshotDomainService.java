package com.nancal.service.service;


import java.util.List;
import java.util.Objects;
import java.util.Collections;
import cn.hutool.json.JSONUtil;
import com.querydsl.core.types.Ops;
import java.util.stream.Collectors;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ArrayUtil;
import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQuery;
import cn.hutool.core.util.ReflectUtil;
import javax.persistence.EntityManager;
import cn.hutool.extra.spring.SpringUtil;
import org.apache.commons.lang3.tuple.Triple;
import com.querydsl.core.types.OrderSpecifier;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

public interface IBomSnapshotDomainService extends IWorkspaceObjectDomainService {
}
