package com.son.account;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by kiost on 2017-05-23.
 */
@Entity
@Getter
@Setter
public class Account {
    @Id
    @GeneratedValue
    private Long id;
    @Column(unique = true)
    private String username;
    private String fullName;
    private String password;
    private String email;
    @Temporal(TemporalType.TIMESTAMP)
    private Date joined;
    @Temporal(TemporalType.TIMESTAMP)
    private Date updated;

    private boolean admin;
}