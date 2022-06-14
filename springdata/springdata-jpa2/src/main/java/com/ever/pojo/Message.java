package com.ever.pojo;


import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "tb_message")
@Data
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String info;

    public Message() {
    }

    public Message(String info) {
        this.info = info;
    }

    /*多对一关联关系*/
}
