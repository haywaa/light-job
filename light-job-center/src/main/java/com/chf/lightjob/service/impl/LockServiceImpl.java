package com.chf.lightjob.service.impl;

import java.lang.reflect.UndeclaredThrowableException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.chf.lightjob.dal.mapper.LightJobLockMapper;
import com.chf.lightjob.service.LockService;

/**
 * @description
 * @author: davy
 * @create: 2022-01-29 19:58
 */
@Service
public class LockServiceImpl implements LockService {

    @Autowired
    private LightJobLockMapper lightJobLockMapper;

    @Autowired
    private PlatformTransactionManager transactionManager;

    private TransactionDefinition transactionDefinition;

    public LockServiceImpl() {
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setName("LightJobLockTxName");
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_NOT_SUPPORTED);
        this.transactionDefinition = def;
    }

    @Override
    public <T> T lock(String lockName, Callback<T> callback) {
        lightJobLockMapper.lockByName(lockName);
        // 1.通过事务管理器开启事务
        TransactionStatus status = this.transactionManager.getTransaction(transactionDefinition);
        T result;
        try {
            // 2.执行传入的业务逻辑
            result = callback.execute();
            // 3.正常执行完成的话，提交事务
            this.transactionManager.commit(status);
            return result;
        }
        catch (RuntimeException | Error ex) {
            // 出现异常，进行回滚
            this.transactionManager.rollback(status);
            throw ex;
        } catch (Throwable ex) {
            // 出现异常，进行回滚
            this.transactionManager.rollback(status);
            throw new UndeclaredThrowableException(ex);
        }
    }
}
