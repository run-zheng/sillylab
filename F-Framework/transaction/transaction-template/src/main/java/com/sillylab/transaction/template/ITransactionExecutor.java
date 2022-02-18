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
}
