package com.y3tu.yao.upms.client.api;

import com.y3tu.yao.common.constant.ServerConstant;
import org.springframework.cloud.openfeign.FeignClient;


/**
 * 获取用户信息
 *
 * @author y3tu
 */
@FeignClient(name = ServerConstant.UPMS_SERVER)
public interface UserService {

}
