package com.sillylab.transaction.template;

/**
 * 事务执行器
 * @author zhengrun
 * @date 2022/2/18
 */
public interface ITransactionExecutor {

    /**
     * 事务执行逻辑
     */
    void execute();

    /**
     * 链式串联多个执行命令
     *
     * @param executor
     * @return
     */
    default ITransactionExecutor andThen(ITransactionExecutor executor){
        return () -> {
            execute();
            executor.execute();
        };
    }
}
