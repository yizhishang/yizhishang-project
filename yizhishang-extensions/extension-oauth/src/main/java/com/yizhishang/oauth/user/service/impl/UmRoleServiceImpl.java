package com.yizhishang.oauth.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yizhishang.common.exception.BizException;
import com.yizhishang.oauth.user.entity.UmRole;
import com.yizhishang.oauth.user.mapper.UmRoleMapper;
import com.yizhishang.oauth.user.service.UmRoleService;
import com.yizhishang.redis.cache.annotation.RedisEvict;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 角色信息 服务实现类
 *
 * @author yizhishang
 * @since 2020-11-10 13:45
 */
@Slf4j
@Service
public class UmRoleServiceImpl extends ServiceImpl<UmRoleMapper, UmRole> implements UmRoleService {

    @Autowired
    private UmRoleMapper umRoleMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UmRole insert(UmRole umRole) {
        if (!this.save(umRole)) {
            throw new BizException("插入角色信息失败");
        }
        return umRole;
    }

    @Override
    @RedisEvict(keys = "UmRole:query:#umRole.id")
    @Transactional(rollbackFor = Exception.class)
    public UmRole modify(UmRole umRole) {
        if (!this.updateById(umRole)) {
            throw new BizException("更新角色信息失败");
        }
        return umRole;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long umRoleId) {
        if (!this.removeById(umRoleId)) {
            throw new BizException("删除角色信息失败");
        }
    }

}