package com.yellowsunn.springcloudvaultexample.config

import com.zaxxer.hikari.HikariDataSource
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.context.annotation.Profile
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource
import org.springframework.transaction.support.TransactionSynchronizationManager
import javax.sql.DataSource

@Profile("prod")
@Configuration
class ProdDataSourceConfig {
    companion object {
        private const val MASTER = "MASTER"
        private const val SLAVE = "SLAVE"
    }

    @Primary
    @Bean
    fun dataSource(): DataSource {
        val routingDataSource = routingDataSource(masterDataSource(), slaveDataSource())
        return LazyConnectionDataSourceProxy(routingDataSource)
    }

    @Bean
    fun routingDataSource(
        masterDataSource: DataSource,
        slaveDataSource: DataSource,
    ): DataSource {
        val dataSourceMap: Map<Any, Any> = mapOf(
            MASTER to masterDataSource,
            SLAVE to slaveDataSource,
        )

        return object : AbstractRoutingDataSource() {
            override fun determineCurrentLookupKey(): Any {
                val isReadOnly: Boolean = TransactionSynchronizationManager.isCurrentTransactionReadOnly()
                return if (isReadOnly) {
                    SLAVE
                } else {
                    MASTER
                }
            }
        }.apply {
            setTargetDataSources(dataSourceMap)
            setDefaultTargetDataSource(masterDataSource)
        }
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.master.hikari")
    fun masterDataSource(): DataSource {
        return DataSourceBuilder.create()
            .type(HikariDataSource::class.java)
            .build()
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.slave.hikari")
    fun slaveDataSource(): DataSource {
        return DataSourceBuilder.create()
            .type(HikariDataSource::class.java)
            .build()
    }
}
