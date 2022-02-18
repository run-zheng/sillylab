package com.sillylab.transaction.template;

/**
 * 有结果的事务执行器
 * @author zhengrun
 * @date 2022/2/18
 */
public interface ITransactionExecutorWithResult<T> {

    /**
     * 事务执行逻辑
     * @return T
     */
    T execute();
}
