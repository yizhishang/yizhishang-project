package com.yizhishang.oauth.client.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yizhishang.common.exception.BizException;
import com.yizhishang.oauth.client.entity.OauthClientDetails;
import com.yizhishang.oauth.client.mapper.OauthClientDetailsMapper;
import com.yizhishang.oauth.client.service.OauthClientDetailsService;
import com.yizhishang.redis.cache.annotation.RedisCache;
import com.yizhishang.redis.cache.annotation.RedisEvict;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *  服务实现类
 *
 * @author yizhishang
 * @date 2020-11-05 15:33
 */
@Slf4j
@Service
public class OauthClientDetailsServiceImpl extends ServiceImpl<OauthClientDetailsMapper, OauthClientDetails> implements OauthClientDetailsService {

    @Autowired
    private OauthClientDetailsMapper oauthClientDetailsMapper;

    @Override
    @RedisCache(key = "OauthClientDetails:query:#clientId")
    public OauthClientDetails queryByClientId(String clientId) {
        return getOne(new LambdaQueryWrapper<OauthClientDetails>().eq(OauthClientDetails::getClientId, clientId));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public OauthClientDetails insert(OauthClientDetails oauthClientDetails) {
        if (!this.save(oauthClientDetails)) {
            throw new BizException("插入失败");
        }
        return oauthClientDetails;
    }

    @Override
    @RedisEvict(keys = "OauthClientDetails:query:#oauthClientDetails.id")
    @Transactional(rollbackFor = Exception.class)
    public OauthClientDetails modify(OauthClientDetails oauthClientDetails) {
        if (!this.updateById(oauthClientDetails)) {
            throw new BizException("更新失败");
        }
        return oauthClientDetails;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long oauthClientDetailsId) {
        if (!this.removeById(oauthClientDetailsId)) {
            throw new BizException("删除失败");
        }
    }

}