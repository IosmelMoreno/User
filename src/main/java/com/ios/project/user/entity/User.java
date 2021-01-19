package com.ios.project.user.entity;

import lombok.Data;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;

@Data
@Document(collection = "usersCollection")
public class User {

    @Id
    private String email;
    private String userName;
    private String Status;

}
