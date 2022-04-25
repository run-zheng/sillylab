package com.sillylab.transaction.template;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/**
 * 事务执行服务
 *
 * @author zhengrun
 * @date 2022/2/18
 */
public class TransactionExecutorTemplate{

    /**
     * 执行默认的事务级别
     * @param executor executor
     */
    @Transactional(rollbackFor = Exception.class)
    public void executeRequired(ITransactionExecutor executor) {
        Assert.notNull(executor, "事务执行器不能为空");
        executor.execute();
    }

    /**
     * 开新事务
     * @param executor executor
     */
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public void executeRequiresNew(ITransactionExecutor executor) {
        Assert.notNull(executor, "事务执行器不能为空");
        executor.execute();
    }

    /**
     * 有返回值的事务，执行默认的事务级别
     * @param executor executor
     * @return 结果
     */
    @Transactional(rollbackFor = Exception.class)
    public <T> T executeRequired(ITransactionExecutorWithResult<T> executor) {
        Assert.notNull(executor, "事务执行器不能为空");
        return executor.execute();
    }

    /**
     * 有返回值的事务 开新事务
     * @param executor executor
     * @return 结果
     */
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public <T> T executeRequiresNew(ITransactionExecutorWithResult<T> executor) {
        Assert.notNull(executor, "事务执行器不能为空");
        return executor.execute();
    }

    /**
     * 链式起点
     *
     * @param executor
     * @return
     */
    public static ITransactionExecutor of(ITransactionExecutor executor){
        return new ITransactionExecutor() {
            private ITransactionExecutor executorChain = executor;
            @Override
            public void execute() {
                this.executorChain.execute();
            }

            @Override
            public ITransactionExecutor andThen(ITransactionExecutor executor) {
                return (this.executorChain = executorChain.andThen(executor));
            }
        };
    }
}
