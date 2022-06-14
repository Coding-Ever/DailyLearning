package com.ever;

import com.alibaba.druid.util.StringUtils;
import com.ever.config.SpringDataJPAConfig;
import com.ever.pojo.Customer;
import com.ever.pojo.QCustomer;
import com.ever.repositories.CustomerQueryDSLRepository;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@ContextConfiguration(classes = SpringDataJPAConfig.class)  // 这种是使用Config配置类形式
@RunWith(SpringJUnit4ClassRunner.class)
public class QueryDSLTest {
    @Autowired
    CustomerQueryDSLRepository queryDSLRepository;

    @Test
    public void testFind(){
        QCustomer customer = QCustomer.customer;

        // 通过Id精确查找
        // 设置条件
        BooleanExpression eq = customer.custId.eq(1L);
        // 进行查询
        System.out.println(queryDSLRepository.findOne(eq));
    }

    /*查询客户名称范围(in)
    * id > ?
    * 地址 精确*/
    @Test
    public void testFindRange(){
        QCustomer customer = QCustomer.customer;

        // 设置条件
        BooleanExpression eq = customer.custName.in("唐三", "李星云", "肖战")
                .and(customer.custId.gt(4L))    // gt()为大于
                .and(customer.custAddress.eq("不良人"));
        // 进行查询
        System.out.println(queryDSLRepository.findOne(eq));
    }

    /*常用比较函数：
    * 等于        EQ      equal                  .eq()
    * 不等于       NE      not equal              .ne()
    * 小于        LT      less than               .lt()
    * 大于        GT      greater than            .gt()
    * 小于等于     LE      less than or equal      .loe()
    * 大于等于     GE      greater than or equal   .goe()
    */

    /*动态查询*/
    @Test
    public void testSearchDynamic() {
        /*通过前端传入的参数*/
        Customer params = new Customer();
        params.setCustId(1L);
        params.setCustName("李星云，姬如雪");
        params.setCustAddress("不良人");

        QCustomer customer = QCustomer.customer;

        // 初始条件，设置为永远成立的条件
        BooleanExpression expression = customer.isNotNull().or(customer.isNotNull());

        expression = params.getCustId() > -1 ?
                expression.and(customer.custId.gt(params.getCustId())):expression;
        expression = !StringUtils.isEmpty(params.getCustName()) ?
                expression.and(customer.custName.in(params.getCustName().split("，"))) : expression;
        expression = !StringUtils.isEmpty(params.getCustAddress()) ?
                expression.and(customer.custAddress.eq(params.getCustAddress())) : expression;

        System.out.println(queryDSLRepository.findAll(expression));
    }

    /*自定义列查询、分组
    * 需要使用原生态的方式
    * */
    // @Autowired // 使用Autowired装配会有线程安全的问题
    // 要保证线程安全问题，需要使用注解@PersistenceContext注解，通过对每个Bean绑定一个EntityManager实现线程安全
    @PersistenceContext // 线程安全的装配方式
    EntityManager entityManager;

    @Test
    public void testDefinition() {
        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(entityManager);

        QCustomer customer = QCustomer.customer;

        /*构建基于QueryDSL的查询*/
        /*自定义类Tuple帮助接收返回值类型，因为只查询两列，没有与之匹配的类型，所以帮我们自定义类型接收*/
        JPAQuery<Tuple> jpaQuery = jpaQueryFactory.select(customer.custName, customer.custAddress)
                .from(customer)
                .where(customer.custAddress.eq("不良人"))
                .orderBy(customer.custId.desc());

        /*执行查询*/
        List<Tuple> fetch = jpaQuery.fetch();

        for (Tuple member : fetch){
            System.out.print(member.get(customer.custName) + ":");
            System.out.println(member.get(customer.custAddress));
        }
    }
}
