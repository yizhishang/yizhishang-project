package com.yizhishang.oauth.client.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yizhishang.oauth.client.entity.OauthClientDetails;
import org.apache.ibatis.annotations.Mapper;

/**
 * Mapper 接口
 *
 * @author yizhishang
 * @since 2020-11-05 15:33
 */
@Mapper
public interface OauthClientDetailsMapper extends BaseMapper<OauthClientDetails> {

}
