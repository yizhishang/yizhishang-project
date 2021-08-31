package com.yizhishang.oauth.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yizhishang.oauth.user.entity.UmRole;

/**
 * 角色信息 服务类
 *
 * @author yizhishang
 * @since 2020-11-10 13:45
 */
public interface UmRoleService extends IService<UmRole> {

    UmRole insert(UmRole umRole);

    UmRole modify(UmRole umRole);

    void delete(Long Id);

}
