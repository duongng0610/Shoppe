package com.e_cormerce.shoppe.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.time.Duration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

@Configuration
@EnableCaching
public class CacheConfig {
  // bean quản lí cache: mặc định dùng serialize (chuyển object java => dạng byte[]) , để dễ debug
  // và giảm dung lượng ram do dạng byte tốn hơn thì sẽ parse sang json , khi method return object
  // => redis sẽ lưu object dưới dạng json
  @Bean
  public RedisCacheManager cacheManager(RedisConnectionFactory factory) {

    ObjectMapper mapper = new ObjectMapper();

    mapper.registerModule(new JavaTimeModule()); // hỗ trợ LocalDateTIme, time type trong java 8+

    mapper.activateDefaultTyping(
        mapper.getPolymorphicTypeValidator(), ObjectMapper.DefaultTyping.NON_FINAL);

    GenericJackson2JsonRedisSerializer serializer =
        new GenericJackson2JsonRedisSerializer(
            mapper); // cần truyền mapper vào để JackSon convert object sang json

    RedisCacheConfiguration config =
        RedisCacheConfiguration.defaultCacheConfig()
            .entryTtl(Duration.ofMinutes(10))
            .serializeValuesWith(
                RedisSerializationContext.SerializationPair.fromSerializer(serializer));

    return RedisCacheManager.builder(factory).cacheDefaults(config).build();
  }
}
