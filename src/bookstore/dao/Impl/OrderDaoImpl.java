package bookstore.dao.Impl;

import bookstore.dao.OrderDao;
import bookstore.domain.Customer;
import bookstore.domain.Order;
import bookstore.domain.OrderItem;
import bookstore.utils.JDCBUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.sql.SQLException;
import java.util.List;

public class OrderDaoImpl implements OrderDao {

    private QueryRunner qr=new QueryRunner(JDCBUtils.getDataSource());

    //保存订单
    @Override
    public void save(Order order) {

        try {
            qr.update("insert into orders (ordernum,price,number,status,customerId) values (?,?,?,?,?)",
                    order.getOrdernum(),order.getPrice(),order.getNumber(),order.getStatus(),
                    order.getCustomer()==null?null:order.getCustomer().getId());
            List<OrderItem> items = order.getItems();
            for(OrderItem item:items){
                qr.update("insert into orderitems (id,number,price,ordernum,bookid) values (?,?,?,?,?)",
                        item.getId(),item.getNumber(),item.getPrice(),order.getOrdernum(),item.getBook()==null?null:item.getBook().getId());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Order findByNum(String ordernum) {
        try {
            Order order = qr.query("select * from orders where ordernum=?", new BeanHandler<Order>(Order.class), ordernum);
            if(order!=null){
                Customer customer = qr.query("select * from customers where id=(select customerId from orders where ordernum=?)", new BeanHandler<Customer>(Customer.class), ordernum);
                order.setCustomer(customer);
            }
            return order;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void update(Order order) {
        try {
            qr.update("update orders set price=?,number=?,status=? where ordernum=?", order.getPrice(),order.getNumber(),order.getStatus(),order.getOrdernum());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Order> findByCustomerId(String customerId) {
        try {
            List<Order> orders=qr.query("select * from orders where customerId=?  order by ordernum desc ", new BeanListHandler<Order>(Order.class),customerId);
            if(orders!=null){
                Customer customer=qr.query("select * from customers where id=? ",new BeanHandler<Customer>(Customer.class),customerId);
                for (Order order : orders) {
                    order.setCustomer(customer);
                }
            }
            return orders;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
