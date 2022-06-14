package com.ever.pojo;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "tb_role")
@Data
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String rName;

    public Role() {
    }

    public Role(String rName) {
        this.rName = rName;
    }

    public Role(Long id, String rName) {
        this.id = id;
        this.rName = rName;
    }
}
