package com.ever.pojo;

import lombok.Data;
import javax.persistence.*;
import java.util.List;

@Entity     // 作为hibernate实体类
@Table(name = "cst_customer")   // 映射的表明
@Data
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long custId;// 客户的主键

    @Column(name = "cust_name")
    private String custName;//客户名称

    @Column(name = "cust_address")
    private String custAddress;//客户地址

    /*// 单向关联  一对一
    cascade 设置关联操作
        ALL     所有持久化操作都会执行关联操作
        PERSIST 只有插入才会执行关联操作
        MERGE   只有修改才会执行关联操作
        REMOVE  只有删除才会执行关联操作
    fetch   设置是否懒加载
        EAGER   立即加载（默认）
        LAZY    懒加载（直到用到对象时才会进行查询，可以提高查询效率，因为不是所有的关联对象都会用到）
    orphanRemoval   关联移除（通常在修改的时候会用到）
        一旦把关联的数据设置为null，或者修改为其他的关联数据，如果想删除关联数据，就可以设置为true
        默认为false
    optional    限制关联的对象是否可以为null（默认为true）
    mappedBy    将外键约束指向另一方维护。值 = 另一方的关联属性名。(通常在双向关联关系中，会放弃一方的外键约束)
    * */
    @OneToOne(mappedBy = "customer",cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "account_id")    // 设置外键的字段名
    private Account account;


    /*一对多关联关系*/
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "customer_id")
    private List<Message> messages;


    // 单向多对多
    @ManyToMany
    /*中间表需要通过@JoinTable来维护：（不设置也会自动生成）
    * name  指定中间表的名称
    * joinColumns   设置本表的外键名称
    * inverseJoinColumns    设置关联表的外键名称
    * */
    @JoinTable(
            name = "tb_customer_role",
            joinColumns = {@JoinColumn(name = "c_id")},
            inverseJoinColumns = {@JoinColumn(name = "r_id")}
    )
    private List<Role> roles;
}