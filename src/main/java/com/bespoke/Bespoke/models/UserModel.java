package com.bespoke.Bespoke.models;

import lombok.Data;

@Data
public class UserModel {
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private  String password;
}
