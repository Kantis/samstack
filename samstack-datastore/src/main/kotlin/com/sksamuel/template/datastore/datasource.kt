package com.sksamuel.template.datastore

import io.micrometer.core.instrument.MeterRegistry
import io.r2dbc.pool.ConnectionPool
import io.r2dbc.pool.ConnectionPoolConfiguration
import io.r2dbc.pool.PoolingConnectionFactoryProvider.MAX_IDLE_TIME
import io.r2dbc.pool.PoolingConnectionFactoryProvider.MAX_LIFE_TIME
import io.r2dbc.pool.PoolingConnectionFactoryProvider.MAX_SIZE
import io.r2dbc.pool.PoolingConnectionFactoryProvider.MIN_IDLE
import io.r2dbc.pool.PoolingConnectionFactoryProvider.POOL_NAME
import io.r2dbc.spi.ConnectionFactories
import io.r2dbc.spi.ConnectionFactoryOptions
import io.r2dbc.spi.ConnectionFactoryOptions.DATABASE
import io.r2dbc.spi.ConnectionFactoryOptions.DRIVER
import io.r2dbc.spi.ConnectionFactoryOptions.HOST
import io.r2dbc.spi.ConnectionFactoryOptions.PASSWORD
import io.r2dbc.spi.ConnectionFactoryOptions.PORT
import io.r2dbc.spi.ConnectionFactoryOptions.PROTOCOL
import io.r2dbc.spi.ConnectionFactoryOptions.USER
import java.time.Duration


data class DatabaseConfig(
   val hostname: String,
   val port: Int,
   val database: String,
   val username: String,
   val password: String,
   val maximumPoolSize: Int = 8,
   val minimumIdle: Int = 2,
   val idleTimeout: Long? = null,
   val maxLifetime: Long? = null,
   val poolName: String? = null,
)

/**
 * Creates the [HikariDataSource] connection pool.
 * Can be removed if you are not using a database in this application.
 */
fun createDataSource(config: DatabaseConfig, registry: MeterRegistry?): ConnectionPool {
   val connectionFactory = ConnectionFactories.get(
      ConnectionFactoryOptions.builder().apply {
         option(DRIVER, "pool")
         option(PROTOCOL, "postgresql") // driver identifier, PROTOCOL is delegated as DRIVER by the pool.
         option(HOST, config.hostname)
         option(PORT, config.port)
         option(USER, config.username)
         option(PASSWORD, config.password)
         option(DATABASE, config.database)
         option(MAX_SIZE, config.maximumPoolSize)
         option(MIN_IDLE, config.minimumIdle)
         config.maxLifetime?.let {
            option(MAX_LIFE_TIME, Duration.ofSeconds(it))
         }

         config.idleTimeout?.let {
            option(MAX_IDLE_TIME, Duration.ofSeconds(it))
         }

         config.poolName?.let {
            option(POOL_NAME, it)
         }
      }.build()
   )

   val configuration = ConnectionPoolConfiguration.builder(connectionFactory)
      .maxIdleTime(Duration.ofMillis(1000))
      .maxSize(20)
      .build()

   return ConnectionPool(configuration)
}
