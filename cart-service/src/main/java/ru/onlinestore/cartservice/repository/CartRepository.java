package ru.onlinestore.cartservice.repository;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import ru.onlinestore.cartservice.model.Cart;

import java.time.Duration;

@Repository
public class CartRepository {

    private final RedisTemplate<String, Cart> redisTemplate;

    public CartRepository(RedisTemplate<String, Cart> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    private static final String HASH_KEY = "Carts";

    public void save(Cart cart) {
        redisTemplate.opsForHash().put(HASH_KEY, cart.getSessionId(), cart);
        redisTemplate.expire(HASH_KEY, Duration.ofHours(24)); // optional TTL
    }

    public Cart findById(String sessionId) {
        return (Cart) redisTemplate.opsForHash().get(HASH_KEY, sessionId);
    }

    public void delete(String sessionId) {
        redisTemplate.opsForHash().delete(HASH_KEY, sessionId);
    }
}