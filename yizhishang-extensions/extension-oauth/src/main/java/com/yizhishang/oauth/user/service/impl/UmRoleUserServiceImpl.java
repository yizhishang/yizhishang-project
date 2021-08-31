package com.yizhishang.oauth.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yizhishang.common.exception.BizException;
import com.yizhishang.oauth.user.entity.UmRoleUser;
import com.yizhishang.oauth.user.mapper.UmRoleUserMapper;
import com.yizhishang.oauth.user.service.UmRoleUserService;
import com.yizhishang.redis.cache.annotation.RedisEvict;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 服务实现类
 *
 * @author yizhishang
 * @since 2020-11-10 13:48
 */
@Slf4j
@Service
public class UmRoleUserServiceImpl extends ServiceImpl<UmRoleUserMapper, UmRoleUser> implements UmRoleUserService {

    @Autowired
    private UmRoleUserMapper umRoleUserMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UmRoleUser insert(UmRoleUser umRoleUser) {
        if (!this.save(umRoleUser)) {
            throw new BizException("插入失败");
        }
        return umRoleUser;
    }

    @Override
    @RedisEvict(keys = "UmRoleUser:query:#umRoleUser.id")
    @Transactional(rollbackFor = Exception.class)
    public UmRoleUser modify(UmRoleUser umRoleUser) {
        if (!this.updateById(umRoleUser)) {
            throw new BizException("更新失败");
        }
        return umRoleUser;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long umRoleUserId) {
        if (!this.removeById(umRoleUserId)) {
            throw new BizException("删除失败");
        }
    }

}