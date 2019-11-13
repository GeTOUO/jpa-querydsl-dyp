package com.getouo.jpaquerydsldypdemo.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "role_model")
@AllArgsConstructor
@NoArgsConstructor
public class RoleModel {
    @Id
    @Column(name = "role_name")
    private String roleName;
    private String remark;
    private Integer level;
}
