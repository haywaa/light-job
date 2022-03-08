package com.chf.lightjob.controller.web;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.chf.lightjob.constants.ResultCode;
import com.chf.lightjob.controller.web.intercepter.WebSessionFilter;
import com.chf.lightjob.controller.web.request.PeriodicJobAddOrUpdateReq;
import com.chf.lightjob.dal.entity.LightJobUserDO;
import com.chf.lightjob.dal.entity.PeriodicJobDO;
import com.chf.lightjob.model.DataResult;
import com.chf.lightjob.scheduler.PeriodicJobScheduler;
import com.chf.lightjob.service.UserService;

import lombok.extern.slf4j.Slf4j;

/**
 * @description
 * @author: davy
 * @create: 2022-03-07 15:42
 */
@Slf4j
@RestController
@RequestMapping("/admin/periodicjob")
public class PeriodicJobController {

    @Autowired
    private UserService userService;

    @RequestMapping("/v1//nextTriggerTime")
    public DataResult<List<String>> nextTriggerTime(String scheduleType, String scheduleConf) {

        PeriodicJobDO paramXxlJobInfo = new PeriodicJobDO();
        paramXxlJobInfo.setScheduleType(scheduleType);
        paramXxlJobInfo.setScheduleConf(scheduleConf);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<String> result = new ArrayList<>();
        try {
            Date lastTime = new Date();
            for (int i = 0; i < 5; i++) {
                lastTime = PeriodicJobScheduler.generateNextValidTime(paramXxlJobInfo, lastTime);
                if (lastTime != null) {
                    result.add(dateFormat.format(lastTime));
                } else {
                    break;
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return DataResult.failure(ResultCode.FAIL_CODE, "非法表达式：" + e.getMessage());
        }
        return DataResult.success(result);
    }

    @RequestMapping("/v1/addOrUpdate")
    public DataResult<Long> addOrUpdate(@RequestBody PeriodicJobAddOrUpdateReq req) {
        Long userId = WebSessionFilter.UserInfoResource.getUserId();
        LightJobUserDO userDO = userService.selectById(userId);
        PeriodicJobDO periodicJobDO = new PeriodicJobDO();
        periodicJobDO.setId(req.getId());
        periodicJobDO.setJobGroup(req.getJobGroup());
        periodicJobDO.setJobDesc(req.getJobDesc());
        //periodicJobDO.setAddTime();
        //periodicJobDO.setUpdateTime();
        periodicJobDO.setAuthor(req.getId() == null ? userDO.getUsercode() : null);
        //periodicJobDO.setAlarmEmail();
        periodicJobDO.setScheduleType(req.getScheduleType());
        periodicJobDO.setScheduleConf(req.getScheduleConf());
        periodicJobDO.setMisfireStrategy(req.getMisfireStrategy());
        periodicJobDO.setBlockStrategy(req.getBlockStrategy());
        periodicJobDO.setMaxRetryTimes(req.getMaxRetryTimes());
        periodicJobDO.setExecutorHandler(req.getExecutorHandler());
        periodicJobDO.setExecutorParam(req.getExecutorParam());
        periodicJobDO.setExecutorTimeout(req.getExecutorTimeout());
        //periodicJobDO.setChildJobid();
        periodicJobDO.setStatus(req.getStatus());
        //periodicJobDO.setTriggerLastTime();
        //periodicJobDO.setTriggerNextTime();
        //periodicJobDO.setScheduleFailTimes();
        // TODO
        return DataResult.success();
    }
}
