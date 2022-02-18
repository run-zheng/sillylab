package com.sillylab.transaction.template;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * 启用TransactionExecutorTemplate的自动配置
 * @author zhengrun
 * @date 2022/2/18
 */
@Configuration
@ConditionalOnClass({PlatformTransactionManager.class})
public class TransactionExecutorTemplateConfig {

    @Bean("transactionExecutorTemplate")
    public TransactionExecutorTemplate transactionExecutorTemplate(){
        return new TransactionExecutorTemplate();
    }
}
