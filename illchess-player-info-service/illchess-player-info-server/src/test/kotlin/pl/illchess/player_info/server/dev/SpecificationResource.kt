package pl.illchess.player_info.server.dev

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager
import org.testcontainers.containers.MySQLContainer
import org.testcontainers.containers.RabbitMQContainer
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

        @Container
        private val RABBIT_MQ_CONTAINER = RabbitMQContainer("rabbitmq:3")
    }

    override fun start(): MutableMap<String, String> {

        RABBIT_MQ_CONTAINER.start()
        MY_SQL_CONTAINER.start()

        val conf = HashMap<String, String>()
        conf["quarkus.datasource.jdbc.url"] = MY_SQL_CONTAINER.jdbcUrl
        conf["quarkus.datasource.username"] = MY_SQL_CONTAINER.username
        conf["quarkus.datasource.password"] = MY_SQL_CONTAINER.password

        conf["rabbitmq-host"] = RABBIT_MQ_CONTAINER.host
        conf["rabbitmq-port"] = RABBIT_MQ_CONTAINER.amqpPort.toString()
        conf["rabbitmq-username"] = "guest"
        conf["rabbitmq-password"] = "guest"

        return conf
    }

    override fun stop() {
        MY_SQL_CONTAINER.stop()
        RABBIT_MQ_CONTAINER.stop()
    }

}