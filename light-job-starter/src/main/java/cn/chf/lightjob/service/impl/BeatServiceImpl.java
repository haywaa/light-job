package cn.chf.lightjob.service.impl;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Service;

import cn.chf.lightjob.service.BeatService;

/**
 * @description
 * @author: davy
 * @create: 2022-01-28 20:13
 */
@Service
public class BeatServiceImpl implements BeatService, DisposableBean {

    @Override
    public void destroy() throws Exception {
        // TODO 调用调度中心停止执行器
    }
}
