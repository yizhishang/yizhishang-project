package com.yizhishang.oauth;

import com.yizhishang.oauth.user.entity.UmUser;
import com.yizhishang.oauth.user.service.UmUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.common.exceptions.UnauthorizedUserException;
import org.springframework.stereotype.Service;

/**
 * @author by yizhishang
 * @since 2020/1/2 10:38
 */
@Service
public class CustomUserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private UmUserService umUserService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, UnauthorizedUserException {
        CustomUserDetail customUserDetail = new CustomUserDetail();
        UmUser user = umUserService.query(username);
        if (user != null) {
            customUserDetail.setUsername(user.getAccount());
            customUserDetail.setPassword(user.getPassword());
            return customUserDetail;
        }
        return null;
    }
}

