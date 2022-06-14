package com.ever;

import com.ever.config.SpringDataJPAConfig;
import com.ever.pojo.Customer;
import com.ever.repositories.CustomerMethodNameRepository;
import com.ever.repositories.CustomerRespository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@ContextConfiguration(classes = SpringDataJPAConfig.class)  // 这种是使用Config配置类形式
@RunWith(SpringJUnit4ClassRunner.class)
public class MethodNameTest {
    @Autowired
    CustomerMethodNameRepository customerMethodNameRepository;

    @Test
    public void testFindByCustName(){
        List<Customer> customer = customerMethodNameRepository.findByCustName("肖战");
        System.out.println(customer);
    }

    @Test
    public void testExistsByCustName(){
        System.out.println(customerMethodNameRepository.existsByCustName("萧炎"));
    }

    @Test
    public void testDeleteByCustId(){
        System.out.println(customerMethodNameRepository.deleteByCustId(11L));
    }

    /*注意需要我们自己采用%实现模糊查询*/
    @Test
    public void testFindByCustomerLike(){
        List<Customer> result = customerMethodNameRepository.findByCustNameLike("唐%");
        System.out.println(result);
    }
}
