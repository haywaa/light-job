package com.chf.lightjob.scheduler;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.chf.lightjob.service.LockService;

/**
 * @description
 * @author: davy
 * @create: 2022-01-29 19:44
 */
@Component
public class PendingJobScheduler {

    @Autowired
    private LockService lockService;

    private Thread scheduleThread;

    @PostConstruct
    public void start() {
        scheduleThread = new Thread(() -> {
            while(true) {
                try {
                    Thread.sleep(0);
                    lockService.lock("pending_job_schedule", () -> {
                        scanScheduleJob();
                        return null;
                    });
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                } catch (Throwable t) {
                    // TODO
                }
            }
        });
        scheduleThread.start();
    }

    private void scanScheduleJob() {

    }
}
