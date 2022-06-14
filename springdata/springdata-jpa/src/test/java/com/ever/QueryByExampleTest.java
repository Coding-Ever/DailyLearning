package com.ever;

import com.ever.config.SpringDataJPAConfig;
import com.ever.pojo.Customer;
import com.ever.repositories.CustomerQueryByExampleRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@ContextConfiguration(classes = SpringDataJPAConfig.class)  // 这种是使用Config配置类形式
@RunWith(SpringJUnit4ClassRunner.class)
public class QueryByExampleTest {
    @Autowired
    CustomerQueryByExampleRepository queryByExampleRepository;

    /*简单实例：客户名称 客户地址 动态查询*/
    @Test
    public void testFind(){
        // 查询条件
        Customer customer = new Customer();
        customer.setCustName("唐三");
        customer.setCustAddress("不良人");

        /*设置匹配器*/
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnorePaths("custName")    // 设置忽略的属性
                .withIgnoreCase("custAdderss")  // 设置忽略大小写
                .withStringMatcher(ExampleMatcher.StringMatcher.ENDING) // 对所有条件字符串进行了结尾匹配
                .withMatcher("custAddress", m -> m.endsWith().ignoreCase())     // 针对单个条件进行限制，会使withIgnoreCase()方法失效，需要单独设置ignoreCase()
                .withMatcher("custAddress", ExampleMatcher.GenericPropertyMatchers.endsWith().ignoreCase());

        // 通过Example构建查询条件
        /*注意！！1这里使用org.springframework.data.domain.Example*/
        Example<Customer> example = Example.of(customer, matcher);

        Iterable<Customer> result = queryByExampleRepository.findAll(example);
        System.out.println(result);
    }

}
