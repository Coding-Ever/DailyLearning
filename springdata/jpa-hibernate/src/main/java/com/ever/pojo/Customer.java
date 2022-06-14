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