package cn.chf.lightjob.consumer;

import java.util.Date;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import cn.chf.lightjob.model.TaskContent;

/**
 * @description
 * @author: davy
 * @create: 2022-01-28 19:45
 */
public class TaskConsumer {

    private ThreadPoolExecutor threadPool = new ThreadPoolExecutor(
                10,
                50,
                60L,
                TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(100),
            r -> new Thread(r, "light-job-executor-" + r.hashCode()));

    public void consumeTask(TaskContent taskContent, String msgId) {
        String localIp = null;
        String lockKeyMsgId = "lj_" + msgId;
        lockCallback(lockKeyMsgId, localIp, locked -> {
            if (!locked) {
                // 调度中心会判断是该过期重试还是如何处理
                return;
            }

            try {
                if (taskContent.getExpireTime().before(new Date())) {
                    return;
                }

                // 1. 加入本地异步执行队列
                // 2. 回调调度中心，标记任务开始执行，并记录当前执行者。 超时策略依赖当前执行者的心跳。
                // ※ MQ标记已经成功前，必须将任务推荐到执行中，否者超时重试等可能判断出错
                if (!markStartProcess(taskContent.getTaskId(), taskContent.getTriggerIndex(), localIp)) {
                    return;
                }


                threadPool.submit(() -> {
                    try {
                        // 3. 回调调度中心，标记执行成功
                    } catch (Throwable t) {
                        // 回调调度中心，标记任务执行失败。是否重试由调度中心策略决定
                    }
                });
            } catch (RejectedExecutionException e) {
                // MQ 重试
                throw e;
            } catch (Throwable t) {
                // 回调调度中心，标记任务执行失败。是否重试由调度中心策略决定
            }
        });
    }

    private static void lockCallback(String lockKey, String lockValue, Callback callback) {

    }

    private static boolean markStartProcess(long taskId, int triggerIndex, String localIp) {
        // 调度中心triggerIndex已经推进到更高，会返回false
        return false;
    }

    private static String getLockValue(String lockKey) {
        return null;
    }

    public static interface Callback {
        void callback(boolean locked);
    }
}
