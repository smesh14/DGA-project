package com.contactbook.mapper;


import com.contactbook.entity.User;
import com.contactbook.entity.UserRequest;
import com.contactbook.entity.UserResponse;
import org.springframework.stereotype.Service;

@Service
public  class UserMapper {

    public UserResponse userToUserResponse(User user){
        var userResponse = new UserResponse();
        userResponse.setEmail(user.getEmail());
        userResponse.setName(user.getName());
        userResponse.setPhoneNumber(user.getPhoneNumber());
        userResponse.setRole(user.getRole());
        return userResponse;
    }

    public User userRequestToUser(UserRequest userRequest){
        var user = new User();
        user.setPhoneNumber(userRequest.getPhoneNumber());
        user.setName(userRequest.getName());
        user.setRole(userRequest.getRole());
        user.setEmail(userRequest.getEmail());
        return user;
    }


}
