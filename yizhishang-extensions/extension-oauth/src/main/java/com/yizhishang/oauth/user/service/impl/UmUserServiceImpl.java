package com.yizhishang.oauth.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yizhishang.common.exception.BizException;
import com.yizhishang.oauth.user.entity.UmUser;
import com.yizhishang.oauth.user.mapper.UmUserMapper;
import com.yizhishang.oauth.user.service.UmUserService;
import com.yizhishang.redis.cache.annotation.RedisCache;
import com.yizhishang.redis.cache.annotation.RedisEvict;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 服务实现类
 *
 * @author yizhishang
 * @since 2020-11-10 13:46
 */
@Slf4j
@Service
public class UmUserServiceImpl extends ServiceImpl<UmUserMapper, UmUser> implements UmUserService {

    @Autowired
    private UmUserMapper umUserMapper;

    @Override
    @RedisCache(key = "UmUser:query:#username")
    public UmUser query(String username) {
        return umUserMapper.selectOne(new LambdaQueryWrapper<UmUser>().eq(UmUser::getAccount, username));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UmUser insert(UmUser umUser) {
        if (!this.save(umUser)) {
            throw new BizException("插入失败");
        }
        return umUser;
    }

    @Override
    @RedisEvict(keys = "UmUser:query:#umUser.id")
    @Transactional(rollbackFor = Exception.class)
    public UmUser modify(UmUser umUser) {
        if (!this.updateById(umUser)) {
            throw new BizException("更新失败");
        }
        return umUser;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long umUserId) {
        if (!this.removeById(umUserId)) {
            throw new BizException("删除失败");
        }
    }

}