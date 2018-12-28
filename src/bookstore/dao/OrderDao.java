package bookstore.dao;

import bookstore.domain.Order;

import java.util.List;

public interface OrderDao {
    void save(Order order);

    Order findByNum(String ordernum);

    void update(Order order);

    List<Order> findByCustomerId(String id);
}
