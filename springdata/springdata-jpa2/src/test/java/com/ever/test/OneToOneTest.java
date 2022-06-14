package com.ever.test;

import com.ever.config.SpringDataJPAConfig;
import com.ever.pojo.Account;
import com.ever.pojo.Customer;
import com.ever.repositories.CustomerRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@ContextConfiguration(classes = SpringDataJPAConfig.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class OneToOneTest {

    @Autowired
    CustomerRepository customerRepository;


    @Test
    public void testInsert(){
        // 初始化
        Account account = new Account();
        account.setUsername("xiaoyan");
        account.setPassword("萧家");

        Customer customer = new Customer();
        customer.setCustId(2L);
        customer.setCustName("萧炎");
        customer.setCustAddress("斗破苍穹");
        customer.setAccount(null);

        customerRepository.save(customer);
    }

    @Test
    public void testFind(){
        System.out.println(customerRepository.findById(2L));
    }

    @Test
    /*为什么懒加载需要配置事务：关联操作分两步查询
    * 当我们通过customerRepository调用完查询操作，session就会立即关闭，一旦session关闭就不能继续查询
    * 加了事务之后，就能让session直到事务方法执行完毕后才会关闭*/
    @Transactional(readOnly = true)
    public void testFindLazy(){
        System.out.println(customerRepository.findById(2L));
    }

    @Test
    public void testDelete(){
        customerRepository.deleteById(1L);
    }

    @Test
    public void testUpdate(){

        customerRepository.deleteById(1L);
    }

    /*关联插入*/
    @Test
    public void testRelateInsert(){
        // 初始化
        Account account = new Account();
        account.setUsername("tangsan");
        account.setPassword("tangmen");

        Customer customer = new Customer();
        customer.setCustName("唐三");
        customer.setCustAddress("斗罗大陆");
        customer.setAccount(account);

        account.setCustomer(customer);

        customerRepository.save(customer);
    }
}
