package com.chf.lightjob.service;

import java.util.Date;

/**
 * @description
 * @author: davy
 * @create: 2022-01-29 21:05
 */
public interface DatabaseTimeService {

    Date currentTime();

    long currentTimeMillis();
}
