package com.son.account;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Size;
import java.util.Date;

/**
 * Created by kiost on 2017-05-23.
 */
public class AccountDto {
    @Data
    public static class Create {
        @NotEmpty
        @Size(min = 5)
        private String username;

        @NotBlank
        @Size(min = 5)
        private String password;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    @Data
    public static class Response {
        private Long id;
        private String username;
        private String fullName;
        private Date joined;
        private Date updated;

        public Response() {
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getFullName() {
            return fullName;
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
        }

        public Date getJoined() {
            return joined;
        }

        public void setJoined(Date joined) {
            this.joined = joined;
        }

        public Date getUpdated() {
            return updated;
        }

        public void setUpdated(Date updated) {
            this.updated = updated;
        }
    }
}
