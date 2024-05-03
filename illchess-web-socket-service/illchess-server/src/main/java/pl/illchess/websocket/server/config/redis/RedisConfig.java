package pl.illchess.websocket.server.config.redis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import pl.illchess.adapter.board.command.out.redis.model.BoardEntity;

@Configuration
@EnableRedisRepositories
public class RedisConfig {

    @Value("${redis.hostname}")
    private String redisHost;

    @Value("${redis.port}")
    private int redisPort;

    @Bean
    public JedisConnectionFactory connectionFactory() {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        configuration.setHostName(redisHost);
        configuration.setPort(redisPort);
        return new JedisConnectionFactory(configuration);
    }

    @Bean
    public RedisTemplate<String, BoardEntity> boardRedisTemplate() {
        RedisTemplate<String, BoardEntity> boardTemplate = new RedisTemplate<>();
        boardTemplate.setConnectionFactory(connectionFactory());

        boardTemplate.setKeySerializer(new StringRedisSerializer());
        boardTemplate.setHashKeySerializer(new StringRedisSerializer());

        boardTemplate.setValueSerializer(new JdkSerializationRedisSerializer());

        boardTemplate.setEnableTransactionSupport(true);
        boardTemplate.afterPropertiesSet();

        return boardTemplate;
    }

}
