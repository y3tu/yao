package com.y3tu.yao.auth.controller;

import com.y3tu.tool.core.pojo.R;
import com.y3tu.tool.core.util.StrUtil;
import com.y3tu.tool.web.base.pojo.Page;
import com.y3tu.yao.auth.entity.OAuthClientDetails;
import com.y3tu.yao.auth.exception.OAuthClientDetailsException;
import com.y3tu.yao.auth.service.OAuthClientDetailsService;
import com.y3tu.yao.common.constant.ServerConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

/**
 * 客户端Controller
 *
 * @author y3tu
 */
@Slf4j
@Validated
@RestController
@RequestMapping("client")
public class OAuthClientDetailsController {

    @Autowired
    private OAuthClientDetailsService oAuthClientDetailsService;


    @PostMapping("page")
    public R page(@RequestBody Page<OAuthClientDetails> page){
        return R.success(oAuthClientDetailsService.findOAuthClientDetailsPage(page));
    }

    @GetMapping("check/{clientId}")
    public R checkUserName(@NotBlank(message = "{required}") @PathVariable String clientId) {
        OAuthClientDetails client = this.oAuthClientDetailsService.findById(clientId);
        if(client==null){
            return R.success(false);
        }else {
            return R.success(true);
        }
    }

    @GetMapping("secret/{clientId}")
    @PreAuthorize("hasAuthority('client:decrypt')")
    public R getOriginClientSecret(@NotBlank(message = "{required}") @PathVariable String clientId) {
        OAuthClientDetails client = this.oAuthClientDetailsService.findById(clientId);
        String origin = client != null ? client.getOriginSecret() : StrUtil.EMPTY;
        return R.success(origin);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('client:create')")
    public void create(@Valid OAuthClientDetails oAuthClientDetails) throws OAuthClientDetailsException {
        try {
            this.oAuthClientDetailsService.createOAuthClientDetails(oAuthClientDetails);
        } catch (Exception e) {
            String message = "新增客户端失败";
            log.error(message, e);
            throw new OAuthClientDetailsException(message);
        }
    }

    @DeleteMapping
    @PreAuthorize("hasAuthority('client:delete')")
    public void delete(@NotBlank(message = "{required}") String clientIds) throws OAuthClientDetailsException {
        try {
            this.oAuthClientDetailsService.deleteOAuthClientDetails(clientIds);
        } catch (Exception e) {
            String message = "删除客户端失败";
            log.error(message, e);
            throw new OAuthClientDetailsException(message);
        }
    }

    @PutMapping
    @PreAuthorize("hasAuthority('client:update')")
    public void updateOauthClientDetails(@Valid OAuthClientDetails oAuthClientDetails) throws OAuthClientDetailsException {
        try {
            this.oAuthClientDetailsService.updateOAuthClientDetails(oAuthClientDetails);
        } catch (Exception e) {
            String message = "修改客户端失败";
            log.error(message, e);
            throw new OAuthClientDetailsException(message);
        }
    }


}
