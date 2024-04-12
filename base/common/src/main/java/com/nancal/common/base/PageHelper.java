package com.nancal.common.base;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class PageHelper implements Serializable {

    public static PageRequest of(TableRequest<?> req) {
        List<Sort.Order> orders = req.getSort() == null
                ? new ArrayList<>()
                : req.getSort()
                .getOrder()
                .stream()
                .map(item -> item.isAsc()
                        ? Sort.Order.asc(item.getProperties())
                        : Sort.Order.desc(item.getProperties())).collect(Collectors.toList());
        Sort sort = Sort.by(orders);
        return PageRequest.of(req.getSkip(), req.getTake(), sort);
    }

    public static PageRequest ofReq(TableRequest<?> req) {
        List<Sort.Order> orders = req.getSort() == null
                ? new ArrayList<>()
                : req.getSort()
                .getOrder()
                .stream()
                .map(item -> item.isAsc()
                        ? Sort.Order.asc(item.getProperties())
                        : Sort.Order.desc(item.getProperties())).collect(Collectors.toList());
        Sort sort = Sort.by(orders);
        return PageRequest.of(req.getSkip() <= 0 ? 0 : req.getSkip() - 1, req.getTake(), sort);
    }

}
