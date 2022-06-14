package com.ever;

import com.ever.config.SpringDataJPAConfig;
import com.ever.pojo.Customer;
import com.ever.repositories.CustomerRespository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Optional;

/*基于Junit4 spring的单元测试*/
//@ContextConfiguration("/spring.xml")    // 配置类注解，指定使用的配置文件，这种是使用XML配置形式
@ContextConfiguration(classes = SpringDataJPAConfig.class)  // 这种是使用Config配置类形式
@RunWith(SpringJUnit4ClassRunner.class)
public class SpringDataJpaTest {
    @Autowired
    CustomerRespository customerRespository;

    @Test
    public void testFind(){
        Optional<Customer> byId = customerRespository.findById(1L);
        System.out.println(byId.get());
    }

    @Test
    public void testAdd(){
        Customer customer = new Customer();
        customer.setCustName("小舞");
        customer.setCustAddress("斗罗大陆");

        customerRespository.save(customer);
    }

    @Test
    public void testUpdate(){
        Customer customer = new Customer();
        customer.setCustId(10L);
        customer.setCustName("戴沐白");
        customer.setCustAddress("斗罗大陆");
        /*与hibernate中的merge()方法一致，先查询再更新*/
        customerRespository.save(customer);
    }

    @Test
    public void testDelete(){
        Customer customer = new Customer();
        customer.setCustId(10L);
        customer.setCustName("戴沐白");
        customer.setCustAddress("斗罗大陆");
        /*先执行查询语句，在执行删除语句。查询之后数据为持久化状态。*/
        customerRespository.delete(customer);
    }
}
