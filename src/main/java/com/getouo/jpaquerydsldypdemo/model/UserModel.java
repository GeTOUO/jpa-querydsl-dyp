package com.getouo.jpaquerydsldypdemo.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Getter
@Setter
@Entity
@Table(name = "user_model")
@NoArgsConstructor
@AllArgsConstructor
public class UserModel {
    @Id
    @Column(name = "user_id")
    private String userId;
    @Column(name = "username")
    private String username;
    @Column(name = "user_age")
    private Integer userAge;
    @Column(name = "password")
    private String password;
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "user_role", joinColumns = {
            @JoinColumn(name = "u_id", referencedColumnName = "user_id") }, inverseJoinColumns = {
            @JoinColumn(name = "role_id", referencedColumnName = "role_name") })
    private List<RoleModel> roles = new ArrayList<>();
}
