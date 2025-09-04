package ru.onlinestore.orderservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.onlinestore.orderservice.model.Order;

import java.util.List;

import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    /**
     * Найти все заказы по sessionId
     */
    List<Order> findBySessionId(String sessionId);
}
