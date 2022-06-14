
# JPA的基本概念及示例

### 什么是JPA？与JDBC的相同点和不同点？
Java Persistence API，是Sun公司推出的一种ORM规范，O：Object，R:Relational,M:Mapping  
作用：简化吃就会操作的开发工作，让开发者可以从繁琐的JDBC和SQL代码中解脱出来，直接面向对象持久化操作；
Sun希望持久化技术能够统一，实现天下归一，如果基于JPA实现持久化，可以随意切换数据库。
- 相同点：  
    1. 都跟数据库的操作相关，JPA是JDBC的升华，升级版。  
    2. JDBC和JPA都是一组规范接口  
    3. 都是由SUN官网推荐的  
- 不同点：  
    1. JDBC是由各种关系型数据库实现的，JPA是由ORM框架实现的。  
    2. JDBC使用SQL语句和数据库通信，JPA用面向对象方式，通过ORM框架来生成SQL，进行操作。  
    3. JPA在JDBC之上，JPA也要依赖JDBC才能操作数据库。
### JPA、Hibernate、MyBatis之间的关系？
Hibernate是JPA接口的ORM框架的底层实现。也就是说，JPA是一套ORM规范，Hibernate实现了JPA规范。  
Mybatis：小巧（JDBC封装）、方便、高效、简单、直接、半自动的ORM框架
场景：在业务比较复杂系统进行使用  
Hibernate：强大（根据ORM映射生成不同的SQL）、方便、高效、复杂、绕弯子、全自动的ORM框架  
场景：在业务相对简单的系统进行使用，随着微服务的流行。  

### Hibernate示例
#### 在pom.xml文件中添加以下依赖
```xml
<dependencies>
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
  <!-- https://mvnrepository.com/artifact/org.apache.openjpa/openjpa-persistence-jdbc -->
  <dependency>
    <groupId>org.apache.openjpa</groupId>
    <artifactId>openjpa-persistence-jdbc</artifactId>
    <version>3.2.0</version>
  </dependency>
  <!-- https://mvnrepository.com/artifact/mysql/mysql-connector-java -->
  <dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>8.0.27</version>
  </dependency>
</dependencies>
```
#### 在com.ever.pojo包下创建实体类Customer
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
#### 在resources资源文件夹下创建hibernate.cfg.xml配置文件
```xml
<?xml version='1.0' encoding='utf-8'?>
<!DOCTYPE hibernate-configuration PUBLIC
        "-//Hibernate/Hibernate Configuration DTD 3.0//EN"
        "http://www.hibernate.org/dtd/hibernate-configuration-3.0.dtd">
<hibernate-configuration>
    <session-factory>
        <!--使用 Hibernate 自带的连接池配置-->
        <property name="connection.url">jdbc:mysql://localhost:3306/springdata?characterEncoding=UTF-8</property>
        <property name="hibernate.connection.username">root</property>
        <property name="hibernate.connection.password">qzy970105</property>
        <property name="connection.driver_class">com.mysql.jdbc.Driver</property>

        <!--允许显示SQL语句-->
        <!--会在日志中记录SQL语句，默认为false-->
        <property name="show_sql">true</property>
        <!--是否格式化SQL，默认为false-->
        <property name="format_sql">true</property>
        <!--表的生成策略，默认为none，不会自动生成
                        update 如果没有表会创建，有会检查更新
                        create 每次运行都会创建表-->
        <property name="hibernate.hbm2ddl.auto">update</property>

        <!--hibernate 方言-->
        <property name="hibernate.dialect">org.hibernate.dialect.MySQL5Dialect</property>

        <!--打印sql语句-->
        <property name="hibernate.show_sql">true</property>
        <!--格式化sql-->
        <property name="hibernate.format_sql">true</property>

        <!-- 加载映射文件 -->
        <!--指定哪些POJO需要ORM映射-->
        <mapping class="com.ever.pojo.Customer"/>
    </session-factory>
</hibernate-configuration>
```
#### 在test文件夹下创建com。ever.test包，在test包下创建hibernateTest测试类
对hibernate的基本增删查改进行测试：
```java
package com.ever.test;

import com.ever.pojo.Customer;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;
import org.junit.Before;
import org.junit.Test;
import java.util.List;

public class hibernateTest {

    // Session工厂
    // Session:数据库会话，代码持久化操作数据库的一个桥梁
    private SessionFactory sf;

    @Before
    public void init(){
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure("/hibernate.cfg.xml").build();
        // 根据服务注册类创建一个元数据资源集，同时构建元数据并生成应用一般的唯一的Session工厂
        sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
    }

    @Test
    public void testAdd(){
        // 通过session进行持久化操作
        try(Session session = sf.openSession()){
            // 开启一个事务
            Transaction transaction = session.beginTransaction();
            Customer customer = new Customer();
            customer.setCustName("黎明");
            customer.setCustAddress("广东广州");
            session.save(customer);
            transaction.commit();
        }
    }

    @Test
    public void testfind(){
        // 通过session进行持久化操作
        try(Session session = sf.openSession()){
            // 开启一个事务
            Transaction transaction = session.beginTransaction();
            // find()方法为饿汉式加载，执行find函数就直接执行SQL语句了
            Customer customer = session.find(Customer.class, 1L);
            System.out.println(customer);

            Customer customerLazy = session.load(Customer.class, 1L);
            // load方法为懒加载，只有使用到的时候才会查询数据库
            System.out.println(customerLazy);

            transaction.commit();
        }
    }

    @Test
    public void testUpdate(){
        // 通过session进行持久化操作
        try(Session session = sf.openSession()){
            // 开启一个事务
            Transaction transaction = session.beginTransaction();

            Customer customer = new Customer();
            customer.setCustName("唐三");
            customer.setCustAddress("斗罗大陆");
            // 不指定Id，默认为插入操作
            session.saveOrUpdate(customer);

            Customer customer1 = new Customer();
            customer1.setCustId(1L);
            customer1.setCustName("萧炎");
            customer1.setCustAddress("斗破苍穹");
            // 指定Id，默认为更新操作
            session.saveOrUpdate(customer1);

            // 插入：seesion.save()
            // 更新：session.update()  需指定Id

            transaction.commit();
        }
    }

    @Test
    public void testDelete(){
        // 通过session进行持久化操作
        try(Session session = sf.openSession()){
            // 开启一个事务
            Transaction transaction = session.beginTransaction();

            Customer customer = new Customer();
            customer.setCustId(3L);
            session.remove(customer);

            transaction.commit();
        }
    }

    @Test
    public void testHQL(){
        try(Session session = sf.openSession()){
            // 开启一个事务
            Transaction transaction = session.beginTransaction();

            String hql = " from Customer where custId =: id";

            List<Customer> resultList = session.createQuery(hql, Customer.class)
                    .setParameter("id", 1L)
                    .getResultList();
            System.out.println(resultList);

            transaction.commit();
        }
    }
}
```
### JPA示例
#### 在resources资源文件夹下创建META-INF文件夹，并在META-INF文件夹下创建JPA的persistence.xml配置文件
```xml
<?xml version="1.0" encoding="UTF-8"?>
<persistence version="2.0" xmlns="http://java.sun.com/xml/ns/persistence">

    <!-- Name属性用于定义持久化单元的名字 (name必选,空值也合法); transaction-type 指定事务类型(可选)-->
    <!--以下是HibernateJPA的持久化单元-->
    <persistence-unit name="hibernateJPA" transaction-type="RESOURCE_LOCAL">
        <!-- jpa的实现方式 -->
        <provider>org.hibernate.jpa.HibernatePersistenceProvider</provider>
        <!-- 需要进行ORM的POJO类 -->
        <class>com.ever.pojo.Customer</class>

        <!--可选配置：配置jpa实现方的配置信息-->
        <properties>
            <!--数据可信息
                用户名，    javax.persistence.jdbc.user
                密码，      javax.persistence.jdbc.password
                驱动，      javax.persistence.jdbc.driver
                数据库地址， javax.persistence.jdbc.url-->
            <property name = "javax.persistence.jdbc.user" value="root"/>
            <property name = "javax.persistence.jdbc.password" value="qzy970105"/>
            <property name = "javax.persistence.jdbc.driver" value="com.mysql.jdbc.Driver"/>
            <property name = "javax.persistence.jdbc.url" value="jdbc:mysql://localhost:3306/springdata?characterEncoding=UTF-8"/>

            <!--配置JPA实现方（hibernate）的配置信息
                    显示SQL：          false/true
                    自动创建数据表：     hibernate.hbm2ddl.auto
                            create:   程序运行时创建数据表（如果有表，先删除再创建）
                            update:   程序运行时创建表（如果有表，不会创建）
                            none:     不会创建表-->
            <property name="hibernate.hbm2ddl.auto" value="update" />
            <property name="hibernate.show_sql" value="true" />
            <property name="hibernate.format_sql" value="true"/>
        </properties>
    </persistence-unit>

    <!--以下是OpenJPA的持久化配置单元-->
    <persistence-unit name="openJPA" transaction-type="RESOURCE_LOCAL">
        <!-- jpa的实现方式 -->
        <provider>org.apache.openjpa.persistence.PersistenceProviderImpl</provider>
        <!-- 需要进行ORM的POJO类 -->
        <class>com.ever.pojo.Customer</class>

        <!--可选配置：配置jpa实现方的配置信息-->
        <properties>
            <!--数据可信息
                用户名，    javax.persistence.jdbc.user
                密码，      javax.persistence.jdbc.password
                驱动，      javax.persistence.jdbc.driver
                数据库地址， javax.persistence.jdbc.url-->
            <property name = "javax.persistence.jdbc.user" value="root"/>
            <property name = "javax.persistence.jdbc.password" value="qzy970105"/>
            <property name = "javax.persistence.jdbc.driver" value="com.mysql.jdbc.Driver"/>
            <property name = "javax.persistence.jdbc.url" value="jdbc:mysql://localhost:3306/springdata?characterEncoding=UTF-8"/>

            <!--配置JPA实现方（openjpa）的配置信息-->
            <!--可以自动生成数据库表-->
            <property name="openjpa.jdbc.SynchronizeMappings" value="buildSchema(ForeignKeys=true)" />
            <property name="openjpa.Log" value="SQL=TRACE" />
        </properties>
    </persistence-unit>
</persistence>
```
#### 在com.ever.test测试包下创建JPA的测试类JPATest
对JPA的基本操作增删查改进行测试：常用hibernate实现
```java
package com.ever.test;

import com.ever.pojo.Customer;
import org.junit.Before;
import org.junit.Test;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class JPATest {
  // 实体管理的工厂，类似于hibernate中的SessionFactory
  EntityManagerFactory factory;
  @Before
  public void before(){
    // 常用HibernateJPA
    factory = Persistence.createEntityManagerFactory("hibernateJPA");
    // openJPA很少用
    //factory = Persistence.createEntityManagerFactory("openJPA");
  }

  @Test
  public void testAdd(){
    // 实体管理，类似于hibernate中的session，是持久化数据库的桥梁
    EntityManager entityManager = factory.createEntityManager();
    // 获取一个事务
    EntityTransaction transaction = entityManager.getTransaction();
    // 开启事务
    transaction.begin();

    Customer customer = new Customer();
    customer.setCustName("唐三");
    customer.setCustAddress("斗罗大陆");
    entityManager.persist(customer);

    // 提交事务
    transaction.commit();
  }

  @Test
  public void testFind(){
    // 实体管理，类似于hibernate中的session，是持久化数据库的桥梁
    EntityManager entityManager = factory.createEntityManager();
    // 获取一个事务
    EntityTransaction transaction = entityManager.getTransaction();
    // 开启事务
    transaction.begin();

    // 立即查询
    Customer customer1 = entityManager.find(Customer.class, 1L);
    System.out.println(customer1);
    // 延迟查询,使用到customer2时才会查询
    Customer customer2 = entityManager.getReference(Customer.class, 1L);
    System.out.println(customer2);

    // 提交事务
    transaction.commit();
  }

  @Test
  public void testUpdate(){
    // 实体管理，类似于hibernate中的session，是持久化数据库的桥梁
    EntityManager entityManager = factory.createEntityManager();
    // 获取一个事务
    EntityTransaction transaction = entityManager.getTransaction();
    // 开启事务
    transaction.begin();

    Customer customer = new Customer();
    customer.setCustId(5L);
    customer.setCustName("李星云");
    customer.setCustAddress("不良人");
        /*merge()方法中的对象
            如果指定了主键：更新 -> 先查询，看是否有变化。如果有变化，更新；如果没变化则不更新
            如果没有指定主键：则直接执行插入*/
    entityManager.merge(customer);

    // 提交事务
    transaction.commit();
  }

  /*使用JPQL语句修改，直接进行更新，不会先进行查询*/
  @Test
  public void testUpdateJPQL(){
    // 实体管理，类似于hibernate中的session，是持久化数据库的桥梁
    EntityManager entityManager = factory.createEntityManager();
    // 获取一个事务
    EntityTransaction transaction = entityManager.getTransaction();
    // 开启事务
    transaction.begin();

    Customer customer = new Customer();
    customer.setCustId(5L);
    customer.setCustName("李星云");
    customer.setCustAddress("不良人");

    String jpql = "UPDATE Customer set custName =: name where custId =: id";
    entityManager.createQuery(jpql)
            .setParameter("name", "姬如雪")
            .setParameter("id", 5L)
            .executeUpdate();

    // 提交事务
    transaction.commit();
  }

  /*对于复杂的查询来说，也可以直接使用SQL查询语句来实现*/
  @Test
  public void testUpdateSQL(){
    // 实体管理，类似于hibernate中的session，是持久化数据库的桥梁
    EntityManager entityManager = factory.createEntityManager();
    // 获取一个事务
    EntityTransaction transaction = entityManager.getTransaction();
    // 开启事务
    transaction.begin();

    Customer customer = new Customer();
    customer.setCustId(5L);
    customer.setCustName("李星云");
    customer.setCustAddress("不良人");

    /*这里需要注意！！！ 这里使用表名和表中的字段，而不是类名和属性。符号"=:"后面不能有空格*/
    String sql = "UPDATE cst_customer set cust_name =:name, cust_address=:address where id =:id";
    entityManager.createNativeQuery(sql)
            .setParameter("name", "李星云")
            .setParameter("address", "不良人")
            .setParameter("id", 6L)
            .executeUpdate();

    // 提交事务
    transaction.commit();
  }

  @Test
  public void testDelete(){
    // 实体管理，类似于hibernate中的session，是持久化数据库的桥梁
    EntityManager entityManager = factory.createEntityManager();
    // 获取一个事务
    EntityTransaction transaction = entityManager.getTransaction();
    // 开启事务
    transaction.begin();

    /*删除的数据必须是持久化的数据，需要先从数据库中查询出数据
     * 自定义的对象处于游离状态，不属于持久化数据，不可直接删除*/
    Customer customer = entityManager.find(Customer.class, 7L);
    entityManager.remove(customer);

    // 提交事务
    transaction.commit();
  }
}
```
### JPA的对象的四种状态
- 临时状态：刚被创建出来，没有与entityManager发生关系，没有被持久化，不处于entityManager中的对象。
- 持久状态：与entityManager发生关系，已经被持久化 ，可以把持久化状态当做实实在在的数据库记录
- 删除状态：执行remove方法，事务提交之前
- 游离状态：指提交到数据库后，事务commit后实体的状态，因为事务已经提交了，此时实体的属性任你如何改变，也不会同步到数据库。  
- 