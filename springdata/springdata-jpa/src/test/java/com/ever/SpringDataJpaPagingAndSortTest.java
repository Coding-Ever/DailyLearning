package com.ever;

import com.ever.config.SpringDataJPAConfig;
import com.ever.pojo.Customer;
import com.ever.repositories.CustomerRespository;
import javafx.scene.control.TableColumn;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Optional;

@ContextConfiguration(classes = SpringDataJPAConfig.class)  // 这种是使用Config配置类形式
@RunWith(SpringJUnit4ClassRunner.class)
public class SpringDataJpaPagingAndSortTest {
    @Autowired
    CustomerRespository customerRespository;

    @Test
    public void testPaging(){
        Page<Customer> page = customerRespository.findAll(PageRequest.of(0, 2));
        System.out.println(page.getTotalPages());
        System.out.println(page.getTotalElements());
        System.out.println(page.getContent());
    }

    /*硬编码的方式，如果属性名修改，则这里参数也要进行修改，不安全，不推荐*/
    @Test
    public void testSort(){
        Sort sort = Sort.by("custId").descending();
        Iterable<Customer> all = customerRespository.findAll(sort);
        System.out.println(all);
    }

    /*软编码的方式，安全，推荐使用这种方式*/
    @Test
    public void testSortTypeSafe(){
        Sort.TypedSort<Customer> sortType = Sort.sort(Customer.class);
        Sort sort = sortType.by(Customer::getCustId).descending();
        /*多个排序规则，可在后面添加and()*/
        //Sort sort = sortType.by(Customer::getCustId).descending().and(sortType.by(Customer::getCustName));
        Iterable<Customer> all = customerRespository.findAll(sort);
        System.out.println(all);
    }

    @Test
    public void testPagingAndSort(){
        /*先设置排序规则*/
        Sort.TypedSort<Customer> sortType = Sort.sort(Customer.class);
        Sort sort = sortType.by(Customer::getCustId).descending();
        /*在设置分页*/
        Page<Customer> page = customerRespository.findAll(PageRequest.of(0, 2, sort));
        System.out.println(page.getContent());
    }
}
