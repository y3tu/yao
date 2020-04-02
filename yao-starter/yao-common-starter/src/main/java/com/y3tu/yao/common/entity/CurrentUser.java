package com.y3tu.yao.common.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 * 当前用户
 *
 * @author y3tu
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CurrentUser implements Serializable {

    @JsonIgnore
    private String password;
    private String username;
    @JsonIgnore
    private Set<GrantedAuthority> authorities;
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    private boolean enabled;
    private String userId;
    private String avatar;
    private String email;
    private String mobile;
    private String sex;
    private Long departmentId;
    private String departmentName;
    private String roleId;
    private String roleName;
    @JsonIgnore
    private Date lastLoginTime;
    private String description;
    private String status;
}
