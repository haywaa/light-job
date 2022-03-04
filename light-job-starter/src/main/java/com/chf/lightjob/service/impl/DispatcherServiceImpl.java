package com.chf.lightjob.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chf.lightjob.annotation.Executor;
import com.chf.lightjob.service.DispatcherService;

/**
 * @description
 * @author: davy
 * @create: 2022-03-04 22:22
 */
@Service
public class DispatcherServiceImpl implements DispatcherService {

    private Map<String, Executor> executorMap;

    @Autowired
    public DispatcherServiceImpl(List<Executor> executorList) {
        if (executorList == null) {
            executorMap = Collections.emptyMap();
        } else {
            executorMap = executorList.stream().collect(Collectors.toMap(executor -> {
                return executor.getClass().getDeclaredAnnotation(Executor.class).code();
            }, o -> o));
        }
    }
}
