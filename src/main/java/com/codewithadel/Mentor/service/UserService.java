package com.codewithadel.Mentor.service;

import com.codewithadel.Mentor.model.Users;
import com.codewithadel.Mentor.repository.UsersRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UsersRepo repo;
    public Users register(Users user) {
       return  repo.save(user);


    }
}
