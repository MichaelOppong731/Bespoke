package com.bespoke.Bespoke.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.grammars.hql.HqlParser;

import java.time.LocalDateTime;
@Data
@Entity
@NoArgsConstructor
public class VerificationToken {

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

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = true)
    private LocalDateTime confirmedAt;

    @Column(nullable = false)
    private boolean isUsed;

    @Column(nullable = false)
    private LocalDateTime expiration;

    @ManyToOne(targetEntity = AppUser.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private AppUser appUser;

    public VerificationToken(String token,
                             LocalDateTime createdAt,
                             boolean isUsed,
                             LocalDateTime expiration,
                             AppUser appUser){
        this.token = token;
        this.createdAt = createdAt;
        this.isUsed = isUsed;
        this.expiration = expiration;
        this.appUser = appUser;
    }





}
