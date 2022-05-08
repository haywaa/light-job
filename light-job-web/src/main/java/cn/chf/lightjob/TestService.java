//package cn.chf.lightjob;
//
//import java.util.Date;
//import java.util.List;
//
//import org.springframework.beans.factory.InitializingBean;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import cn.chf.lightjob.dal.entity.PeriodicJobDO;
//import cn.chf.lightjob.dal.mapper.PeriodicJobMapper;
//import cn.chf.lightjob.enums.MisfireStrategyEnum;
//import cn.chf.lightjob.enums.ScheduleTypeEnum;
//
//import lombok.extern.slf4j.Slf4j;
//
///**
// * @description
// * @author: davy
// * @create: 2022-01-31 00:22
// */
//@Slf4j
//@Service
//public class TestService implements InitializingBean {
//
//    @Autowired
//    private PeriodicJobMapper periodicJobMapper;
//
//    @Override
//    public void afterPropertiesSet() throws Exception {
//        //initTestData();
//        batchUpdateTest();
//        //updateTest();
//    }
//
//    void initTestData() {
//        long start = System.currentTimeMillis();
//        PeriodicJobDO periodicJobDO = new PeriodicJobDO();
//        //periodicJobDO.setId();
//        periodicJobDO.setJobGroup("test");
//        periodicJobDO.setJobDesc("test");
//        periodicJobDO.setAddTime(new Date());
//        periodicJobDO.setUpdateTime(new Date());
//        periodicJobDO.setAuthor("test");
//        periodicJobDO.setAlarmEmail(null);
//        periodicJobDO.setScheduleType(ScheduleTypeEnum.CRON.name());
//        periodicJobDO.setScheduleConf(null);
//        periodicJobDO.setMisfireStrategy(MisfireStrategyEnum.DO_NOTHING.name());
//        periodicJobDO.setExecutorRouteStrategy(null);
//        periodicJobDO.setExecutorHandler(null);
//        periodicJobDO.setExecutorParam(null);
//        periodicJobDO.setBlockStrategy(null);
//        periodicJobDO.setExecutorTimeout(20);
//        periodicJobDO.setExecutorFailRetryCount(0);
//        periodicJobDO.setChildJobid(null);
//        periodicJobDO.setStatus(1);
//        periodicJobDO.setTriggerLastTime(new Date().getTime());
//        periodicJobDO.setTriggerNextTime(new Date().getTime());
//        periodicJobDO.setScheduleFailTimes(0);
//        for (int i = 0; i < 10000; i++) {
//            periodicJobMapper.addJob(periodicJobDO);
//        }
//        long cost = System.currentTimeMillis() - start;
//        log.info("initTestData cost: {}", cost);
//    }
//
//    void batchUpdateTest() {
//        List<PeriodicJobDO> allJob = periodicJobMapper.queryAll();
//        for (PeriodicJobDO periodicJobDO : allJob) {
//            periodicJobDO.setStatus(periodicJobDO.getId() % 2 == 0 ? 0 : 1);
//        }
//        long start = System.currentTimeMillis();
//        periodicJobMapper.batchUpdateJob(allJob);
//        long cost = System.currentTimeMillis() - start;
//        log.info("batchUpdateTest cost: {}", cost);
//    }
//
//    void updateTest() {
//        List<PeriodicJobDO> allJob = periodicJobMapper.queryAll();
//        long start = System.currentTimeMillis();
//        for (PeriodicJobDO periodicJobDO : allJob) {
//            periodicJobMapper.updateById(periodicJobDO);
//        }
//        long cost = System.currentTimeMillis() - start;
//        log.info("updateTest cost: {}", cost);
//    }
//}
