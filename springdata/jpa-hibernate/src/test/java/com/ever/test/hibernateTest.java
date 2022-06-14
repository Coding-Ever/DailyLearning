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
