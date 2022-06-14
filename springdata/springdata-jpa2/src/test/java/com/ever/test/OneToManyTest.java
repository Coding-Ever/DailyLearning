package com.ever.test;

import com.ever.config.SpringDataJPAConfig;
import com.ever.pojo.Customer;
import com.ever.pojo.Message;
import com.ever.repositories.CustomerRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;

@ContextConfiguration(classes = SpringDataJPAConfig.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class OneToManyTest {
    @Autowired
    CustomerRepository customerRepository;

    @Test
    public void testInsert(){
        ArrayList<Message> messages = new ArrayList<>();
        messages.add(new Message("你好~"));
        messages.add(new Message("在吗？"));

        Customer customer = new Customer();
        customer.setCustName("李星云");
        customer.setCustAddress("不良人");
        customer.setMessages(messages);

        customerRepository.save(customer);

    }
}
