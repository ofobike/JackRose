package bookstore.test;

import bookstore.utils.MailUtils;
import bookstore.utils.StringUtils;
import bookstore.web.ClientServlet;
import org.junit.Test;

import java.util.UUID;

public class TestMain {
    @Test
    public void test1(){
        ClientServlet userServlet = new ClientServlet();
    }
    @Test
    public void testMail(){
        MailUtils utils = new MailUtils("18203692944@163.com", UUID.randomUUID().toString());
        utils.sendMail();
        System.out.println("Success");
    }
    @Test
    public void testString(){
        String s = StringUtils.gengraString();
        System.out.println(s);
    }

    @Test
    public void testStringWith(){
        String s = StringUtils.gengraStringWith();
        System.out.println(s);
    }
}
