package com.yizhishang.oauth.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yizhishang.oauth.user.entity.UmRole;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 角色信息 Mapper 接口
 *
 * @author yizhishang
 * @since 2020-11-10 13:45
 */
@Mapper
@Repository
public interface UmRoleMapper extends BaseMapper<UmRole> {

}
