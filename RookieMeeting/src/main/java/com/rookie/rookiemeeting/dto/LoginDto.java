package com.rookie.rookiemeeting.dto;

import com.rookie.rookiemeeting.entity.Menu;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.List;

@Data
public class LoginDto implements Serializable {

    private long employeeid;

    @NotBlank(message = "账号不能为空")
    private String username;

    @NotBlank(message = "密码不能为空")
    private String password;

    private String employeename;

    private String token;

    private String role;

    private List<Menu> menus;
}
