package com.yizhishang.oauth.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yizhishang.oauth.user.entity.UmUser;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 *  Mapper 接口
 *
 * @author yizhishang
 * @since 2020-11-10 13:46
 */
@Mapper
@Repository
public interface UmUserMapper extends BaseMapper<UmUser> {

}
