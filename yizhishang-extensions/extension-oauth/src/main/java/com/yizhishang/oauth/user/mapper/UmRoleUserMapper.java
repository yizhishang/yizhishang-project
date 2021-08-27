package com.yizhishang.oauth.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yizhishang.oauth.user.entity.UmRoleUser;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 *  Mapper 接口
 *
 * @author yizhishang
 * @date 2020-11-10 13:48
 */
@Mapper
@Repository
public interface UmRoleUserMapper extends BaseMapper<UmRoleUser> {

}
