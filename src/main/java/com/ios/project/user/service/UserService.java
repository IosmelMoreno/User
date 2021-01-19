package com.ios.project.user.service;

import com.ios.project.user.entity.User;

import java.util.List;

public interface UserService {

    public User getUser(String email);

    public List<User> listAllUser();

    public User createUser(User product);

    public User updateUser(User product);

    public  User deleteUser(User user);

}
