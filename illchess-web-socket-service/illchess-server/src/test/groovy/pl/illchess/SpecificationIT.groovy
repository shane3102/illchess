package pl.illchess

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.SpyBean
import org.springframework.http.MediaType
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.RabbitMQContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.spock.Testcontainers
import org.testcontainers.utility.DockerImageName
import pl.illchess.application.commons.command.out.PublishEvent
import pl.illchess.websocket.server.IllChessWebSocketApplication
import spock.lang.Specification

import java.nio.charset.StandardCharsets

@SpringBootTest(classes = IllChessWebSocketApplication.class)
@Testcontainers
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@AutoConfigureWebMvc
@ContextConfiguration
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
abstract class SpecificationIT extends Specification {

    private static final def REDIS_PORT = 6379
    private static final def RABBITMQ_PORT = 5672

    public static final OBJECT_MAPPER = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .setSerializationInclusion(JsonInclude.Include.NON_NULL)

    @Autowired
    protected MockMvc mockMvc

    @SpyBean
    protected RabbitTemplate rabbitTemplate

    def setupSpec() {
        redis.start()
        rabbitMq.start()
    }

    @DynamicPropertySource
    static void configure(DynamicPropertyRegistry registry) {
        registry.add("redis.hostname", () -> redis.host)
        registry.add("redis.port", () -> redis.getMappedPort(REDIS_PORT).toString())
        registry.add("spring.rabbitmq.host", () -> rabbitMq.host)
        registry.add("spring.rabbitmq.port", () -> rabbitMq.getMappedPort(RABBITMQ_PORT).toString())
        registry.add("spring.rabbitmq.username", () -> "guest")
        registry.add("spring.rabbitmq.password", () -> "guest")
    }

    @Container
    static redis = new GenericContainer<>(DockerImageName.parse("redis:5.0.3-alpine")).withExposedPorts(REDIS_PORT)

    @Container
    static rabbitMq = new RabbitMQContainer(DockerImageName.parse("rabbitmq:3-management"))

    protected MockHttpServletResponse putJson(
            String url,
            Object request,
            Object... pathVariables = {}
    ) {
        return mockMvc.perform(MockMvcRequestBuilders.put(url, pathVariables)
                .contentType(MediaType.APPLICATION_JSON)
                .content(OBJECT_MAPPER.writeValueAsString(request)))
                .andReturn()
                .response
    }

    protected MockHttpServletResponse getJson(
            String url,
            Object... pathVariable
    ) {
        def requestBuilder = MockMvcRequestBuilders.get(url, pathVariable)
        return mockMvc.perform(requestBuilder)
                .andReturn()
                .response
    }

    protected <T> T parseJson(
            MockHttpServletResponse responseResult,
            Class<T> clazz
    ) {
        return OBJECT_MAPPER.readValue(responseResult.contentAsString, clazz)
    }

    protected <T> T parseJson(
            MockHttpServletResponse responseResult,
            TypeReference<T> typeReference
    ) {
        def contentToParse = responseResult.getContentAsString(StandardCharsets.UTF_8)
        assert !contentToParse.blank

        return OBJECT_MAPPER.readValue(
                contentToParse,
                typeReference
        )
    }

    protected static String generateRandomName() {
        return "name-${UUID.randomUUID()}"
    }
}
