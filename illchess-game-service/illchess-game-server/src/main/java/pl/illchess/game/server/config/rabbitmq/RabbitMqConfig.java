package pl.illchess.game.server.config.rabbitmq;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static pl.illchess.game.adapter.game.command.in.rabbitmq.BoardCommandRabbitMqListener.OBTAIN_GAME_FAILURE_QUEUE;
import static pl.illchess.game.adapter.game.command.in.rabbitmq.BoardCommandRabbitMqListener.OBTAIN_GAME_SUCCESS_QUEUE;
import static pl.illchess.game.adapter.game.query.in.rabbitmq.GameInfoRabbitMqSupplier.OBTAIN_GAME_QUEUE;

@Configuration
public class RabbitMqConfig {

    @Bean(value = OBTAIN_GAME_QUEUE)
    Queue obtainGameQueue() {
        return new Queue(OBTAIN_GAME_QUEUE);
    }

    @Bean(value = OBTAIN_GAME_SUCCESS_QUEUE)
    Queue obtainGameSuccessQueue() {
        return new Queue(OBTAIN_GAME_SUCCESS_QUEUE);
    }

    @Bean
    TopicExchange obtainGameSuccessExchange() {
        // TODO move all properties related to rabbit to configuration (and further to .env file)
        return new TopicExchange(OBTAIN_GAME_SUCCESS_QUEUE);
    }

    @Bean
    Binding bindingObtainGameSuccess() {
        return BindingBuilder.bind(obtainGameSuccessQueue()).to(obtainGameSuccessExchange()).with("#");
    }

    @Bean(value = OBTAIN_GAME_FAILURE_QUEUE)
    Queue obtainGameFailureQueue() {
        return new Queue(OBTAIN_GAME_FAILURE_QUEUE);
    }

    @Bean
    TopicExchange obtainGameFailureExchange() {
        // TODO move all properties related to rabbit to configuration (and further to .env file)
        return new TopicExchange(OBTAIN_GAME_FAILURE_QUEUE);
    }

    @Bean
    Binding bindingObtainGameFailure() {
        return BindingBuilder.bind(obtainGameFailureQueue()).to(obtainGameFailureExchange()).with("#");
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return new Jackson2JsonMessageConverter(objectMapper);
    }
}
