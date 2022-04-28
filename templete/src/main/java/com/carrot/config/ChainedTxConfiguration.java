package com.carrot.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.transaction.ChainedTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

//@Configuration
public class ChainedTxConfiguration {

	@Bean(name="chained")
	//@Primary
    public PlatformTransactionManager transactionManager(@Qualifier("kuderTransactionManager") PlatformTransactionManager kuderTransactionManager, @Qualifier("mailTransactionManager") PlatformTransactionManager mailTransactionManager) {
        return new ChainedTransactionManager(kuderTransactionManager, mailTransactionManager);
    }
	
}
