package cn.chf.lightjob.api;//package cn.chf.lightjob.service;
//
//import cn.chf.lightjob.model.CompensateTaskCreateRequest;
//import cn.chf.lightjob.model.DelayTaskCreateRequest;
//
///**
// * @description
// * @author: davy
// * @create: 2022-02-09 23:26
// */
//public interface CompensateTaskService {
//
//    <T> T execute(MainRunner<T> mainRunner, FollowRunner asyncRunner, CompensateTaskCreateRequest request);
//
//    <T> T execute(MainRunner<T> mainRunner, FollowRunner followRunner, FollowRunAsync needAsync, CompensateTaskCreateRequest request);
//
//    @FunctionalInterface
//    interface MainRunner<T> {
//        T execute();
//    };
//
//    @FunctionalInterface
//    interface FollowRunner {
//        void execute();
//    }
//
//    @FunctionalInterface
//    interface FollowRunAsync {
//        boolean needAsync();
//    }
//}
