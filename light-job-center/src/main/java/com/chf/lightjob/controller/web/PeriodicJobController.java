package com.chf.lightjob.controller.web;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chf.lightjob.constants.ErrorCode;
import com.chf.lightjob.controller.web.intercepter.WebSessionFilter;
import com.chf.lightjob.controller.web.request.PeriodicJobAddOrUpdateReq;
import com.chf.lightjob.controller.web.request.PeriodicJobPageReq;
import com.chf.lightjob.controller.web.response.GroupResp;
import com.chf.lightjob.controller.web.response.PeriodicJobResp;
import com.chf.lightjob.dal.entity.LightJobGroupDO;
import com.chf.lightjob.dal.entity.LightJobUserDO;
import com.chf.lightjob.dal.entity.PeriodicJobDO;
import com.chf.lightjob.dal.qo.GroupQO;
import com.chf.lightjob.dal.qo.PeriodicJobQO;
import com.chf.lightjob.model.DataResult;
import com.chf.lightjob.model.PageData;
import com.chf.lightjob.scheduler.PeriodicJobScheduler;
import com.chf.lightjob.service.PeriodicJobService;
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

    @Autowired
    private PeriodicJobService periodicJobService;

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
            return DataResult.failure(ErrorCode.FAIL_CODE.getErrorNo(), "非法表达式：" + e.getMessage());
        }
        return DataResult.success(result);
    }

    @RequestMapping("/v1/addOrUpdate")
    public DataResult<Long> addOrUpdate(@Validated @RequestBody PeriodicJobAddOrUpdateReq req) {
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
        Long id = periodicJobService.addOrUpdate(periodicJobDO);
        return DataResult.success(id);
    }

    @PostMapping("/v1/queryList")
    public DataResult<PageData<PeriodicJobResp>> queryList(@Validated @RequestBody PeriodicJobPageReq req) {
        PeriodicJobQO qo = new PeriodicJobQO();
        qo.setGroupCode(req.getGroupCode());
        qo.setPageNo(Optional.ofNullable(req.getPageNo()).orElse(1));
        qo.setPageSize(Optional.ofNullable(req.getPageSize()).orElse(20));
        int count = periodicJobService.countByQuery(qo);
        if (count == 0) {
            return DataResult.success(PageData.emptyPage(count));
        }
        List<PeriodicJobDO> periodicJobDOList = periodicJobService.listByQuery(qo);
        if (CollectionUtils.isEmpty(periodicJobDOList)) {
            return DataResult.success(PageData.emptyPage(count));
        }

        List<PeriodicJobResp> respList = periodicJobDOList.stream().map(groupDO -> {
            return convertDoToResp(groupDO);
        }).collect(Collectors.toList());
        return DataResult.success(PageData.listPage(count, respList));
    }

    private static PeriodicJobResp convertDoToResp(PeriodicJobDO jobDO) {
        if (jobDO == null) {
            return null;
        }
        PeriodicJobResp periodicJobResp = new PeriodicJobResp();
        periodicJobResp.setId(jobDO.getId());
        periodicJobResp.setJobGroup(jobDO.getJobGroup());
        periodicJobResp.setJobDesc(jobDO.getJobDesc());
        periodicJobResp.setAddTime(jobDO.getAddTime());
        periodicJobResp.setUpdateTime(jobDO.getUpdateTime());
        periodicJobResp.setAuthor(jobDO.getAuthor());
        periodicJobResp.setAlarmEmail(jobDO.getAlarmEmail());
        periodicJobResp.setScheduleType(jobDO.getScheduleType());
        periodicJobResp.setScheduleConf(jobDO.getScheduleConf());
        periodicJobResp.setMisfireStrategy(jobDO.getMisfireStrategy());
        periodicJobResp.setBlockStrategy(jobDO.getBlockStrategy());
        periodicJobResp.setMaxRetryTimes(jobDO.getMaxRetryTimes());
        periodicJobResp.setExecutorHandler(jobDO.getExecutorHandler());
        periodicJobResp.setExecutorParam(jobDO.getExecutorParam());
        periodicJobResp.setExecutorTimeout(jobDO.getExecutorTimeout());
        periodicJobResp.setChildJobid(jobDO.getChildJobid());
        periodicJobResp.setStatus(jobDO.getStatus());
        periodicJobResp.setTriggerLastTime(jobDO.getTriggerLastTime());
        periodicJobResp.setTriggerNextTime(jobDO.getTriggerNextTime());
        periodicJobResp.setScheduleFailTimes(jobDO.getScheduleFailTimes());
        return periodicJobResp;
    }
}
