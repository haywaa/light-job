package cn.chf.lightjob.service;

/**
 * @description
 * @author: davy
 * @create: 2022-01-29 19:57
 */
public interface LockService {

    <T> T lock(String lockName, Callback<T> callback);

    interface Callback<T> {
        T execute();
    }
}
