package com.ever;

import com.ever.config.SpringDataJPAConfig;
import com.ever.pojo.Customer;
import com.ever.repositories.CustomerRespository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import java.util.List;

@ContextConfiguration(classes = SpringDataJPAConfig.class)  // 这种是使用Config配置类形式
@RunWith(SpringJUnit4ClassRunner.class)
public class JPQLTest {

    @Autowired
    CustomerRespository customerRespository;

    @Test
    public void testFind(){
        List<Customer> customer = customerRespository.findCustomerByCustName("萧炎");
        System.out.println(customer);
    }

    @Test
    public void testUpdate(){
        int result = customerRespository.updateCustomerById("肖战", 1L);
        System.out.println(result);
    }

    @Test
    public void testDelete(){
        int result = customerRespository.deleteCustomerById(9L);
        System.out.println(result);
    }

    @Test
    public void testInsert(){
        int result = customerRespository.insertCustomerBySelect(6L);
        System.out.println(result);
    }

    /*原生SQL语句查询*/
    @Test
    public void testFindBySQL(){
        List<Customer> result = customerRespository.findCustomerByCustNameBySQL("李星云");
        System.out.println(result);
    }
}
