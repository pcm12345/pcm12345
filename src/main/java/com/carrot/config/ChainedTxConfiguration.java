package com.carrot.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.transaction.ChainedTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class ChainedTxConfiguration {

	@Bean(name="chained")
	@Primary
    public PlatformTransactionManager transactionManager(@Qualifier("crmTransactionManager") PlatformTransactionManager crmTransactionManager, @Qualifier("altTransactionManager") PlatformTransactionManager altTransactionManager) {
        return new ChainedTransactionManager(crmTransactionManager, altTransactionManager);
    }
	
}
