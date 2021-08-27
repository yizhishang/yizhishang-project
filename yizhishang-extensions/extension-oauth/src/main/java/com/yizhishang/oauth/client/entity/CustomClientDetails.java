package com.yizhishang.oauth.client.entity;

import com.fasterxml.jackson.core.util.Separators;
import com.google.common.collect.Sets;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.provider.ClientDetails;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

/**
 * @Description TODO
 * @since 2020/5/19 15:18
 * @author by yizhishang
 */
public class CustomClientDetails implements ClientDetails, Serializable {

    private static final long serialVersionUID = 1L;

    private OauthClientDetails clientDetails;

    public CustomClientDetails(OauthClientDetails clientDetails) {
        this.clientDetails = clientDetails;
    }

    public OauthClientDetails getOauthClientDetails() {
        return clientDetails;
    }

    public void setOauthClientDetails(OauthClientDetails clientDto) {
        this.clientDetails = clientDto;
    }

    @Override
    public String getClientId() {
        return clientDetails.getClientId();
    }

    @Override
    public Set<String> getResourceIds() {
        return stringToSet(clientDetails.getResourceIds());
    }

    @Override
    public boolean isSecretRequired() {
        return false;
    }

    @Override
    public String getClientSecret() {
        return clientDetails.getClientSecret();
    }

    @Override
    public boolean isScoped() {
        return false;
    }

    @Override
    public Set<String> getScope() {
        return stringToSet(clientDetails.getScope());
    }

    @Override
    public Set<String> getAuthorizedGrantTypes() {
        return stringToSet(clientDetails.getAuthorizedGrantTypes());
    }

    @Override
    public Set<String> getRegisteredRedirectUri() {
        return stringToSet(clientDetails.getWebServerRedirectUri());
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    @Override
    public Integer getAccessTokenValiditySeconds() {
        return clientDetails.getAccessTokenValidity();
    }

    @Override
    public Integer getRefreshTokenValiditySeconds() {
        return clientDetails.getRefreshTokenValidity();
    }

    @Override
    public boolean isAutoApprove(String scope) {
        return false;
    }

    @Override
    public Map<String, Object> getAdditionalInformation() {
        return null;
    }

    private static Set<String> stringToSet(String input) {
        if (input == null) {
            return null;
        }
        Separators separators = Separators.createDefaultInstance();
        String[] values = input.split(String.valueOf(separators.getObjectEntrySeparator()));
        Set<String> set = Sets.newTreeSet();
        for(String value: values){
            set.add(value);
        }
        return set;
    }
}
