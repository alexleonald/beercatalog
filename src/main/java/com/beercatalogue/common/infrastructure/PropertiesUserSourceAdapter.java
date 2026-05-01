package com.beercatalogue.common.infrastructure;

import com.beercatalogue.common.application.SecurityAccount;
import com.beercatalogue.common.application.UserSourcePort;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "app.security")
public class PropertiesUserSourceAdapter implements UserSourcePort {

    private List<UserConfig> users;

    public List<UserConfig> getUsers() {
        return users;
    }

    public void setUsers(List<UserConfig> users) {
        this.users = users;
    }

    @Override
    public List<SecurityAccount> getAllUsers() {
        return users.stream()
                .map(u -> new SecurityAccount(u.getUsername(), u.getPassword(), u.getRoles()))
                .toList();
    }

    static class UserConfig{
        private String username;
        private String password;
        private String roles;

        public UserConfig(String username, String password, String roles) {
            this.username = username;
            this.password = password;
            this.roles = roles;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getRoles() {
            return roles;
        }

        public void setRoles(String roles) {
            this.roles = roles;
        }
    }
}
