package com.ever.pojo;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "tb_account")
@Data
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;

    @OneToOne
    @JoinColumn(name = "customer_id")    // 设置外键的字段名
    private Customer customer;
}
