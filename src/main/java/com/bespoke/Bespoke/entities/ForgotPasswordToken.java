package com.bespoke.Bespoke.entities;


import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.grammars.hql.HqlParser;

import java.time.LocalDateTime;
@Data
@Entity
public class ForgotPasswordToken {

    @Id
    @SequenceGenerator(
            name = "user_sequence",
            sequenceName = "user_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "user_sequence"
    )
    private Integer id;

    @Column(nullable = false)
    private String token;

    @ManyToOne(targetEntity = AppUser.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private AppUser appUser;

    @Column(nullable = false)
    private LocalDateTime expiration;

    @Column(nullable = false)
    private boolean isUsed;

}
