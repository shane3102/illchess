package pl.illchess.player_info.server.dev

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager
import org.testcontainers.containers.MySQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers


@Testcontainers
open class SpecificationResource : QuarkusTestResourceLifecycleManager {

    companion object {

        @Container
        private val MY_SQL_CONTAINER = MySQLContainer("mysql")
            .withDatabaseName("player_info_db")
            .withUsername("admin")
            .withPassword("admin")
    }

    override fun start(): MutableMap<String, String> {

        MY_SQL_CONTAINER.start()

        val conf = HashMap<String, String>()
        conf["quarkus.datasource.jdbc.url"] = MY_SQL_CONTAINER.jdbcUrl
        conf["quarkus.datasource.username"] = MY_SQL_CONTAINER.username
        conf["quarkus.datasource.password"] = MY_SQL_CONTAINER.password
        return conf
    }

    override fun stop() {
        MY_SQL_CONTAINER.stop()
    }

}