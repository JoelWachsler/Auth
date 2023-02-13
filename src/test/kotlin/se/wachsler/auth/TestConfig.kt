package se.wachsler.auth

import io.r2dbc.h2.H2ConnectionConfiguration
import io.r2dbc.h2.H2ConnectionFactory
import io.r2dbc.h2.H2ConnectionOption
import io.r2dbc.spi.ConnectionFactory
import org.flywaydb.core.Flyway
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Profile

const val username = "testing"
const val password = "testing"
const val db = "testdb"

@TestConfiguration
class TestConfig {

    @Bean
    @Profile("test")
    fun connectionFactory(): ConnectionFactory {
        val config = H2ConnectionConfiguration.builder()
            .inMemory(db)
            .username(username)
            .password(password)
            .property(H2ConnectionOption.DB_CLOSE_DELAY, "-1")
            .property(H2ConnectionOption.MODE, "PostgreSQL")
            .build()

        return H2ConnectionFactory(config)
    }

    @Bean(initMethod = "migrate")
    @Profile("test")
    fun flyway(): Flyway {
        return Flyway(Flyway.configure()
            .baselineOnMigrate(true)
            .dataSource("jdbc:h2:mem:$db;DB_CLOSE_DELAY=-1", username, password))
    }
}