

Spring Data JPA是Spring提供的一套简化JPA开发的框架，按照按约定好的规则进行【方法命名】去写dao层接口，
就可以在不写接口实现的情况下，实现对数据库的访问和操作，同时还提供了除了CURD之外的功能，如分页、排序、复杂查询等。  
在实际的工作当中，推荐使用Spring Data JPA + ORM(如：hibernate)完成操作。



### Spring Data JPA 示例
#### 在pom.xml文件中添加以下依赖
```xml
    <dependencies>
        <dependency>
            <groupId>org.springframework.data</groupId>
            <artifactId>spring-data-jpa</artifactId>
        </dependency>
        <!-- https://mvnrepository.com/artifact/junit/junit -->
        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <version>4.13.1</version>
            <scope>test</scope>
        </dependency>
        <!-- https://mvnrepository.com/artifact/org.hibernate/hibernate-entitymanager -->
        <dependency>
            <groupId>org.hibernate</groupId>
            <artifactId>hibernate-entitymanager</artifactId>
            <version>5.6.8.Final</version>
        </dependency>
        <!-- https://mvnrepository.com/artifact/mysql/mysql-connector-java -->
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>8.0.27</version>
        </dependency>
        <!--连接池-->
        <!-- https://mvnrepository.com/artifact/com.alibaba/druid -->
        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>druid</artifactId>
            <version>1.2.8</version>
        </dependency>
        <!--spring-test-->
        <!-- https://mvnrepository.com/artifact/org.springframework/spring-test -->
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-test</artifactId>
            <version>5.3.10</version>
            <scope>test</scope>
        </dependency>
    </dependencies>
```
#### 创建实体类com.ever.pojo.Customer.class
```java
package com.ever.pojo;
import javax.persistence.*;

@Entity     // 作为hibernate实体类
@Table(name = "cst_customer")   // 映射的表明
public class Customer {
    /*
     * @Id：声明主键的配置
     * @GeneratedValue：配置主键的生成策略
     *       strategy = GenerationType.IDENTITY：自增，mysql
     *                   * 底层数据结构必须支持自动增长
     *                = GenerationType.SEQUENCE：序列，Oracle
     *                   * 底层数据库必须支持序列
     *                = GenerationType.TABLE：jpa提供的一种机制，通过一张数据库表的形式帮我们完成主键
     *                = GenerationType.AUTO：由程序自动帮我们选择主键生成策略
     * @Column：配置属性和字段的映射关系
     *       name：数据库表中字段的名称
     *  */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long custId;// 客户的主键


    @Column(name = "cust_name")
    private String custName;//客户名称

    @Column(name = "cust_address")
    private String custAddress;//客户地址

    public Long getCustId() {
        return custId;
    }

    public void setCustId(Long custId) {
        this.custId = custId;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getCustAddress() {
        return custAddress;
    }

    public void setCustAddress(String custAddress) {
        this.custAddress = custAddress;
    }
}
```
#### 使用XML方式配置
在resources文件夹下创建spring.xml
```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:jpa="http://www.springframework.org/schema/data/jpa" xmlns:tx="http://www.springframework.org/schema/tx"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
    https://www.springframework.org/schema/beans/spring-beans.xsd
    http://www.springframework.org/schema/data/jpa
    https://www.springframework.org/schema/data/jpa/spring-jpa.xsd http://www.springframework.org/schema/tx http://www.springframework.org/schema/tx/spring-tx.xsd">
    <!--用于整合jpa  @EnableJpaRespositories-->
    <jpa:repositories base-package="com.ever.repositories"
                      entity-manager-factory-ref="entityManagerFactory"
                      transaction-manager-ref="transactionManager"
    />

    <!--EntityManagerFactory-->
    <bean name = "entityManagerFactory" class="org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean">
        <property name="jpaVendorAdapter">
            <!--Hibernate的实现-->
            <bean class="org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter">
                <!--生成数据库表-->
                <property name="generateDdl" value="true"></property>
                <property name="showSql" value="true"></property>
            </bean>
        </property>
        <!--设置实体类的包,即哪些类需要映射-->
        <property name="packagesToScan" value="com.ever.pojo"></property>
        <property name="dataSource" ref="dataSource"></property>
    </bean>

    <!--数据源-->
    <bean class="com.alibaba.druid.pool.DruidDataSource" name="dataSource">
        <property name="url" value="jdbc:mysql://localhost:3306/springdata?characterEncoding=UTF-8"></property>
        <property name="driverClassName" value="com.mysql.jdbc.Driver"></property>
        <property name="username" value="root"></property>
        <property name="password" value="qzy970105"></property>
    </bean>

    <!--声明事务-->
    <bean class="org.springframework.orm.jpa.JpaTransactionManager" name="transactionManager">
        <property name="entityManagerFactory" ref="entityManagerFactory"></property>
    </bean>

    <!--启动注解方式的声明事务-->
    <tx:annotation-driven transaction-manager="transactionManager"></tx:annotation-driven>
</beans>
```
#### 在com.ever.repositories包下创建CustomerRespository接口
```java
package com.ever.repositories;

import com.ever.pojo.Customer;
import org.springframework.data.repository.CrudRepository;

public interface CustomerRespository extends CrudRepository<Customer, Long> {
    /*CrudRepository<T, V>泛型中，T为实体类的类型，V为Id的类型*/
}
```
#### 创建SpringDataJPATest类进行测试
```java
package com.ever;

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
```
#### 使用JavaConfig配置方式
创建com.ever.config包，在包下创建SpringDataJPAConfig配置类
```java
package com.ever.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

@Configuration  // 标记当前类为配置类 == XML配置文件
@EnableJpaRepositories(basePackages = "com.ever.repositories")  // 启动jpa  <jpa:repositories>
@EnableTransactionManagement    // 开启事务
public class SpringDataJPAConfig {

    @Bean
    public DataSource dataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUsername("root");
        dataSource.setPassword("qzy970105");
        dataSource.setUrl("jdbc:mysql://localhost:3306/springdata?characterEncoding=UTF-8");
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        return dataSource;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setGenerateDdl(true);
        vendorAdapter.setShowSql(true);// 显示SQL语句配置

        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setJpaVendorAdapter(vendorAdapter);
        factory.setPackagesToScan("com.ever.pojo");
        factory.setDataSource(dataSource());
        return factory;
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager txManager = new JpaTransactionManager();
        txManager.setEntityManagerFactory(entityManagerFactory);
        return txManager;
    }
}
```