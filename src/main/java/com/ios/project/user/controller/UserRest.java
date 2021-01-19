package com.ios.project.user.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ios.project.user.entity.User;
import com.ios.project.user.service.FirebaseService;
import com.ios.project.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/user")
public class UserRest {

    @Autowired
    UserService userService;

    @Autowired
    FirebaseService firebaseService;

    //-------------Users List--------------//
    @GetMapping
    public ResponseEntity<List<User>> listProduct(){
        List<User> users = userService.listAllUser();
        if(users.isEmpty()){
            return  ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(users);
    }

    //-------------User Detail--------------//
    @GetMapping(value = "/{email}")
    public ResponseEntity<User> getUser(@PathVariable("email") String email) throws ExecutionException, InterruptedException {
        User user = userService.getUser(email);
        if(user == null){
            return  ResponseEntity.notFound().build();
        }
        //firebaseService.getUser(email);
        return ResponseEntity.ok(firebaseService.getUser(user.getEmail()));
    }

    //---------------Create User-------------//
    @PostMapping(value = "/new")
    public ResponseEntity<User> createUser(@RequestBody User user, BindingResult result) throws ExecutionException, InterruptedException {
        if (result.hasErrors()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, this.formatMessage(result));
        }
        User userDB = userService.createUser(user);
        firebaseService.saveUserDetails(userDB);
        return ResponseEntity.status(HttpStatus.CREATED).body(userDB);
    }

    //--------------Update user-------------//
    @PutMapping(value = "/{email}")
    public ResponseEntity<?> updateUser(@PathVariable("email") String email, @RequestBody User user) throws ExecutionException, InterruptedException {
        user.setEmail(email);
        User currentUser = userService.updateUser(user);
        if(currentUser==null){
            return ResponseEntity.notFound().build();
        }
        //firebaseService.updateUser(currentUser);
        return ResponseEntity.ok(firebaseService.updateUser(currentUser));
    }

    //----------Delete user-----------//
    @DeleteMapping(value = "/{email}")
    public ResponseEntity<User> deleteUser(@PathVariable("email") String email){
        User user = userService.getUser(email);
        if(user==null){
            return ResponseEntity.notFound().build();
        }
        user = userService.deleteUser(user);
        firebaseService.deleteUser(email);
        return ResponseEntity.ok(user);
    }


    private String formatMessage( BindingResult result){
        List<Map<String,String>> errors = result.getFieldErrors().stream()
                .map(err ->{
                    Map<String,String> error =  new HashMap<>();
                    error.put(err.getField(), err.getDefaultMessage());
                    return error;

                }).collect(Collectors.toList());
        ErrorMessage errorMessage = ErrorMessage.builder()
                .code("01")
                .messages(errors).build();
        ObjectMapper mapper = new ObjectMapper();
        String jsonString="";
        try {
            jsonString = mapper.writeValueAsString(errorMessage);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return jsonString;
    }
}
