package org.backend.cloud.authentication.provider;

import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 帐号密码登录校验器
 * @author liqi
 */
public class UsernamePasswordAuthenticationProvider implements AuthenticationProvider {

    private static final Logger LOGGER = LoggerFactory
        .getLogger(UsernamePasswordAuthenticationProvider.class);

    @Getter
    @Setter
    private UserDetailsService userDetailsService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public UsernamePasswordAuthenticationProvider(UserDetailsService userDetailsService) {
        super();
        this.userDetailsService = userDetailsService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // 获取表单用户名
        String username = (String) authentication.getPrincipal();
        // 获取表单用户填写的密码
        String password = (String) authentication.getCredentials();
        UserDetails userDetails;
        try {
            userDetails = userDetailsService.loadUserByUsername(username);
        } catch (Exception e) {
            // 异常不是鉴权异常的时候，异常无法向上抛出，异常处理的controller无法返回默认异常，需要把异常处理成鉴权异常
            throw new AuthenticationServiceException(e.getMessage());
        }
        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            LOGGER.info("当前登录人:{},当前登录密码:{}", username, password);
            throw new BadCredentialsException("用户密码不正确");
        }
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        // 帐号密码登录使用的校验器
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}

