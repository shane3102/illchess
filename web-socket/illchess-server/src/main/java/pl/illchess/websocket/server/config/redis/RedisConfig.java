package pl.illchess.websocket.server.config.redis;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@EnableRedisRepositories
public class RedisConfig {

    @Bean
    public JedisConnectionFactory connectionFactory() {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        configuration.setHostName("localhost");
        configuration.setPort(6379);
        return new JedisConnectionFactory(configuration);
    }

    @Bean
    public RedisTemplate<String, Object> boardRedisTemplate() {
        RedisTemplate<String, Object> boardTemplate = new RedisTemplate<>();
        boardTemplate.setConnectionFactory(connectionFactory());

        boardTemplate.setKeySerializer(new StringRedisSerializer());
        boardTemplate.setHashKeySerializer(new StringRedisSerializer());

        boardTemplate.setValueSerializer(new JdkSerializationRedisSerializer());

        boardTemplate.setEnableTransactionSupport(true);
        boardTemplate.afterPropertiesSet();

        return boardTemplate;
    }

}
