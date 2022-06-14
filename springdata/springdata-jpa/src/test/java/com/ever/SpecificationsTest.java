package com.ever;

import com.alibaba.druid.util.StringUtils;
import com.ever.config.SpringDataJPAConfig;
import com.ever.pojo.Customer;
import com.ever.pojo.QCustomer;
import com.ever.repositories.CustomerSpecificationsRepository;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@ContextConfiguration(classes = SpringDataJPAConfig.class)  // 这种是使用Config配置类形式
@RunWith(SpringJUnit4ClassRunner.class)
public class SpecificationsTest {
    @Autowired
    CustomerSpecificationsRepository customerSpecificationsRepository;

    /*客户范围查询（in）
    * id > ?
    * 地址 精确*/
    @Test
    public void testFind(){
        List<Customer> customers = customerSpecificationsRepository.findAll(new Specification<Customer>() {
            @Override
            public Predicate toPredicate(Root<Customer> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                /*root:理解为from Customer，可以用于获取列
                * query:组合（order by, where 等）
                * CriteriaBuilder criteriaBuilder：设置各种条件，如where语句中的(>  <  in ...)*/

                // 通过root获取需要的列，注意获取到的类型要与属性类型相同
                Path<Long> custId = root.get("custId");
                Path<String> custName = root.get("custName");
                Path<String> custAddress = root.get("custAddress");

                // 设置条件
                /*参数1：为哪一个字段设置条件  参数2：条件设置的值*/
                Predicate predicateId = criteriaBuilder.gt(custId, 5L);
                /*<T> CriteriaBuilder.In<T> in(Expression<? extends T> var1);
                * 只有一个参数*/
                CriteriaBuilder.In<String> predicateName = criteriaBuilder.in(custName);
                predicateName.value("肖战").value("李星云").value("唐三");
                Predicate predicateAddress = criteriaBuilder.equal(custAddress, "不良人");

                // 条件组合
                Predicate predicate = criteriaBuilder.and(predicateId, predicateName, predicateAddress);
                return predicate;
            }
        });
        System.out.println(customers);
    }

    /*动态查询*/
    @Test
    public void testFindDynamic(){
        /*通过前端传入的参数*/
        Customer params = new Customer();
        params.setCustId(1L);
        params.setCustName("李星云，姬如雪");
        params.setCustAddress("不良人");

        List<Customer> customers = customerSpecificationsRepository.findAll(new Specification<Customer>() {
            @Override
            public Predicate toPredicate(Root<Customer> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {

                // 通过root获取需要的列，注意获取到的类型要与属性类型相同
                Path<Long> custId = root.get("custId");
                Path<String> custName = root.get("custName");
                Path<String> custAddress = root.get("custAddress");

                // 设置条件
                List<Predicate> predicates = new ArrayList<>();
                if(!StringUtils.isEmpty(params.getCustAddress())){
                    predicates.add(criteriaBuilder.equal(custAddress, params.getCustAddress()));
                }
                if(params.getCustId() > -1){
                    predicates.add(criteriaBuilder.gt(custId, params.getCustId()));
                }
                if(!StringUtils.isEmpty(params.getCustName())){
                    CriteriaBuilder.In<String> predicateName = criteriaBuilder.in(custName);
                    predicateName.value("肖战").value("李星云").value("唐三");
                    predicates.add(predicateName);
                }

                // 条件组合
                Predicate predicate = criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));

                // 还可以实现排序等
                Order desc = criteriaBuilder.desc(custId);

                //return predicate;
                return query.where(predicate).orderBy(desc).getRestriction();
            }
        });
        System.out.println(customers);
    }
}
