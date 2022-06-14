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
