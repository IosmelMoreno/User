package com.ios.project.user.service;

import com.ios.project.user.entity.User;
import com.ios.project.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    @Autowired
    UserRepository user1Repository;

    @Override
    public User getUser(String email) {
        return user1Repository.findById(email).orElse(null);
    }

    @Override
    public List<User> listAllUser() {
        return user1Repository.findAll();
    }

    @Override
    public User createUser(User User) {
        User.setStatus("CREATED");
        return user1Repository.insert(User);
    }

    @Override
    public User updateUser(User User) {
        User userDB = getUser(User.getEmail());
        if (null == userDB){
            return null;
        }
        userDB.setUserName(User.getUserName());
        userDB.setEmail(User.getEmail());

        return user1Repository.save(userDB);
    }

    @Override
    public User deleteUser(User user) {
        User userDB = getUser(user.getEmail());
        if (null == userDB){
            return null;
        }
        userDB.setStatus("DELETED");
        return user1Repository.save(userDB);
    }
}
