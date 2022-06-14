package com.ever.repositories;

import com.ever.pojo.Customer;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CustomerRespository extends PagingAndSortingRepository<Customer, Long>, CrudRepository<Customer, Long> {
    /*CrudRepository<T, V>泛型中，T为实体类的类型，V为Id的类型*/

    /*Spring Data JPA 的自定义持久化操作*/
    /****************************************JPQL方式**********************************************/
    // 查询
    //@Query("SELECT cus FROM Customer cus WHERE cus.custName=?1")
//    @Query("FROM Customer WHERE custName=?1") // 参数的索引方式
//    List<Customer> findCustomerByCustName(String custName);
    /*参数传递的注解方式*/
    @Query("FROM Customer WHERE custName=:custName")
    List<Customer> findCustomerByCustName(@Param("custName") String custName);

    // 添加
    /*JPQL实际上是不支持新增的，只不过我们是基于hibernate实现的JPA的一种伪新增的模式
    * 尽管如此，依旧不能仅用INSERT实现新增操作，而是要以INSERT+SELECT的方式来实现*/
    @Transactional
    @Modifying      // 通知SpringDataJPA是增删改的操作
    @Query("INSERT INTO Customer(custName, custAddress) SELECT c.custName,c.custAddress FROM Customer c WHERE c.custId=:id ")
    int insertCustomerBySelect(@Param("id") Long id);

    // 更新修改,对于更新操作要以事务进行
    @Transactional
    @Modifying      // 通知SpringDataJPA是增删改的操作
    @Query("UPDATE Customer c SET c.custName=:custName where c.custId=:id")
    int updateCustomerById(@Param("custName") String custName, @Param("id") Long id);

    // 删除
    @Transactional
    @Modifying      // 通知SpringDataJPA是增删改的操作
    @Query("delete from Customer c where c.custId=:id")
    int deleteCustomerById(@Param("id") Long id);

    /*原生SQL语句实现查询*/
    /*注意！！！采用原生SQL语句查询，使用字段及表名均为数据库中的表名及字段名*/
    @Query(value = "SELECT * FROM cst_customer c WHERE cust_name=:custName",
    nativeQuery = true)
    List<Customer> findCustomerByCustNameBySQL(@Param("custName") String custName);
}
