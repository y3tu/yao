package com.y3tu.yao.auth.entity;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 用户绑定
 * @author y3tu
 */
@Data
public class BindUser implements Serializable {

    @NotBlank(message = "{required}")
    private String bindUsername;

    @NotBlank(message = "{required}")
    private String bindPassword;
}
