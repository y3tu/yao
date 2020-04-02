package com.y3tu.yao.auth.controller;

import com.y3tu.tool.core.pojo.R;
import com.y3tu.tool.core.util.StrUtil;
import com.y3tu.yao.auth.entity.BindUser;
import com.y3tu.yao.auth.entity.UserConnection;
import com.y3tu.yao.auth.exception.SocialLoginException;
import com.y3tu.yao.auth.service.SocialLoginService;
import lombok.extern.slf4j.Slf4j;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.model.AuthUser;
import me.zhyd.oauth.request.AuthRequest;
import me.zhyd.oauth.utils.AuthStateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.io.IOException;
import java.util.List;

/**
 * 第三方登录Controller
 *
 * @author y3tu
 */
@Slf4j
@RestController
@RequestMapping("social")
public class SocialLoginController {

    private static final String TYPE_LOGIN = "login";
    private static final String TYPE_BIND = "bind";

    @Autowired
    private SocialLoginService socialLoginService;

    @Value("${yao.frontUrl}")
    private String frontUrl;

    /**
     * 登录
     *
     * @param oauthType 第三方登录类型
     * @param response  response
     */
    @ResponseBody
    @GetMapping("/login/{oauthType}/{type}")
    public void renderAuth(@PathVariable String oauthType, @PathVariable String type, HttpServletResponse response) throws IOException, SocialLoginException {
        AuthRequest authRequest = socialLoginService.renderAuth(oauthType);
        response.sendRedirect(authRequest.authorize(oauthType + "::" + AuthStateUtils.createState()) + "::" + type);
    }

    /**
     * 登录成功后的回调
     *
     * @param oauthType 第三方登录类型
     * @param callback  携带返回的信息
     * @return String
     */
    @GetMapping("/{oauthType}/callback")
    public ModelAndView login(@PathVariable String oauthType, AuthCallback callback, String state, Model model) {
        try {
            R r;
            String type = StrUtil.subAfter(state, "::",true);
            if (StrUtil.equals(type, TYPE_BIND)) {
                r = socialLoginService.resolveBind(oauthType, callback);
            } else {
                r = socialLoginService.resolveLogin(oauthType, callback);
            }
            model.addAttribute("response", r);
            model.addAttribute("frontUrl", frontUrl);
            return new ModelAndView("result","userModel",model);
        } catch (Exception e) {
            model.addAttribute("error", "第三方登录失败"+e.getMessage());
            return new ModelAndView("error","userModel",model);
        }
    }

    /**
     * 绑定并登录
     *
     * @param bindUser bindUser
     * @param authUser authUser
     * @return R
     */
    @ResponseBody
    @PostMapping("bind/login")
    public R bindLogin(@Valid BindUser bindUser, AuthUser authUser) throws SocialLoginException{
        OAuth2AccessToken oAuth2AccessToken = this.socialLoginService.bindLogin(bindUser, authUser);
        return R.success(oAuth2AccessToken);
    }

    /**
     * 注册并登录
     *
     * @param registerUser registerUser
     * @param authUser   authUser
     * @return R
     */
    @ResponseBody
    @PostMapping("sign/login")
    public R signLogin(@Valid BindUser registerUser, AuthUser authUser) throws SocialLoginException {
        OAuth2AccessToken oAuth2AccessToken = this.socialLoginService.signLogin(registerUser, authUser);
        return R.success(oAuth2AccessToken);
    }

    /**
     * 绑定
     *
     * @param bindUser bindUser
     * @param authUser authUser
     */
    @ResponseBody
    @PostMapping("bind")
    public void bind(BindUser bindUser, AuthUser authUser) throws SocialLoginException {
        this.socialLoginService.bind(bindUser, authUser);
    }

    /**
     * 解绑
     *
     * @param bindUser  bindUser
     * @param oauthType oauthType
     */
    @ResponseBody
    @DeleteMapping("unbind")
    public void unbind(BindUser bindUser, String oauthType) throws SocialLoginException {
        this.socialLoginService.unbind(bindUser, oauthType);
    }

    /**
     * 根据用户名获取绑定关系
     *
     * @param username 用户名
     * @return R
     */
    @ResponseBody
    @GetMapping("connections/{username}")
    public R findUserConnections(@NotBlank(message = "{required}") @PathVariable String username) {
        List<UserConnection> userConnections = this.socialLoginService.findUserConnections(username);
        return R.success(userConnections);
    }
}
