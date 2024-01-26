package com.productiontracking.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.productiontracking.entity.Role;
import com.productiontracking.entity.User;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtUserDetails implements UserDetails {

    public Long id;
    private String username;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;

    private JwtUserDetails(Long id, String username, String password,
            Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.authorities = authorities;
    }

    public static JwtUserDetails create(User _pUser) {
        List<GrantedAuthority> authoritiesList = new ArrayList<>();
        for (Role _vRole : _pUser.getRoles()) {
            authoritiesList.add(new SimpleGrantedAuthority("ROLE_" + _vRole.getName()));
        }
        authoritiesList.add(new SimpleGrantedAuthority("ID" + _pUser.getId()));
        return new JwtUserDetails(_pUser.getId(), _pUser.getEmail(), _pUser.getPassword(),
                authoritiesList);
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
