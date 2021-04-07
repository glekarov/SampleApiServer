package org.sample.server.auth;

import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;

public class AuthUser extends User {
    private String token;

    public AuthUser(String user, String passwd) {
        super(user, passwd, true, true, true, true,
                AuthorityUtils.createAuthorityList("USER"));
    }

    public String getUser() {
        return super.getUsername();
    }

    public String getPasswd() {
        return super.getPassword();
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
