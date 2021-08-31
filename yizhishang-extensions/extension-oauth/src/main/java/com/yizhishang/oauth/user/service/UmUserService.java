package com.yizhishang.oauth.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yizhishang.oauth.user.entity.UmUser;

/**
 * 服务类
 *
 * @author yizhishang
 * @since 2020-11-10 13:46
 */
public interface UmUserService extends IService<UmUser> {

    UmUser query(String account);

    UmUser insert(UmUser umUser);

    UmUser modify(UmUser umUser);

    void delete(Long Id);

}
