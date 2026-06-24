package com.vault.controller;

import com.vault.entity.LoginDTO;
import com.vault.entity.User;
import com.vault.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class LoginController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody LoginDTO loginDTO) {
        Map<String, Object> result = new HashMap<>();
        User user = userService.login(loginDTO.getUsername(), loginDTO.getPassword());
        if (user != null) {
            result.put("code", 200);
            result.put("msg", "登录成功");
            result.put("token", "vault_token_" + user.getId());
            result.put("userId", user.getId());
        } else {
            result.put("code", 401);
            result.put("msg", "用户名或密码错误");
        }
        return result;
    }
}
