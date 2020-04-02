package com.y3tu.yao.auth.service;

import com.y3tu.tool.web.base.pojo.Page;
import com.y3tu.tool.web.base.service.BaseService;
import com.y3tu.yao.auth.entity.OAuthClientDetails;
import com.y3tu.yao.auth.exception.OAuthClientDetailsException;

/**
 * 客户端service
 *
 * @author y3tu
 */
public interface OAuthClientDetailsService extends BaseService<OAuthClientDetails> {
    /**
     * 查询（分页）
     *
     * @param Page  分页参数
     * @return Page<OAuthClientDetails>
     */
    Page<OAuthClientDetails> findOAuthClientDetailsPage(Page<OAuthClientDetails> Page);
    /**
     * 根据主键查询
     *
     * @param clientId clientId
     * @return OAuthClientDetails
     */
    OAuthClientDetails findById(String clientId);

    /**
     * 新增
     *
     * @param oauthClientDetails oauthClientDetails
     */
    void createOAuthClientDetails(OAuthClientDetails oauthClientDetails) throws OAuthClientDetailsException;

    /**
     * 修改
     *
     * @param oauthClientDetails oauthClientDetails
     */
    void updateOAuthClientDetails(OAuthClientDetails oauthClientDetails);

    /**
     * 删除
     *
     * @param clientIds clientIds
     */
    void deleteOAuthClientDetails(String clientIds);
}
