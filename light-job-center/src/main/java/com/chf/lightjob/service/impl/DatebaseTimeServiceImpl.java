package com.chf.lightjob.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chf.lightjob.dal.mapper.LightJobLockMapper;
import com.chf.lightjob.service.DatabaseTimeService;

/**
 * @description
 * @author: davy
 * @create: 2022-01-29 21:06
 */
@Service
public class DatebaseTimeServiceImpl implements DatabaseTimeService, InitializingBean {

    @Autowired
    private LightJobLockMapper lightJobLockMapper;

    private long timeOffset = 0;

    @Override
    public void afterPropertiesSet() {
        List<Long> arr = new ArrayList<>(5);
        arr.add(databaseTimeOffset());
        arr.add(databaseTimeOffset());
        arr.add(databaseTimeOffset());
        arr.add(databaseTimeOffset());
        arr.add(databaseTimeOffset());
        long minOffset = arr.stream().min(Long::compareTo).get();
        long maxOffset = arr.stream().max(Long::compareTo).get();
        this.timeOffset = (arr.stream().mapToLong(value -> value).sum() - minOffset - maxOffset) / (arr.size() - 2);
    }

    @Override
    public Date currentTime() {
        return new Date(System.currentTimeMillis() - timeOffset);
    }

    @Override
    public long currentTimeMillis() {
        return System.currentTimeMillis() - timeOffset;
    }

    private long databaseTimeOffset() {
        Date databaseTime = lightJobLockMapper.currentTime();
        return System.currentTimeMillis() - databaseTime.getTime();
    }
}
