package com.yizhishang.oauth.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yizhishang.oauth.user.entity.UmRoleUser;

/**
 * 服务类
 *
 * @author yizhishang
 * @since 2020-11-10 13:48
 */
public interface UmRoleUserService extends IService<UmRoleUser> {

    UmRoleUser insert(UmRoleUser umRoleUser);

    UmRoleUser modify(UmRoleUser umRoleUser);

    void delete(Long id);

}
