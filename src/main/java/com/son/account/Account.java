package com.son.account;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by kiost on 2017-05-23.
 */
@Entity
public class Account {
    @Id
    @GeneratedValue
    private Long id;
    private String username;
    private String fullName;
    private String password;
    private String email;
    @Temporal(TemporalType.TIMESTAMP)
    private Date joined;
    @Temporal(TemporalType.TIMESTAMP)
    private Date updated;

    public Account() {
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
