package com.codewithadel.Mentor.repository;

import com.codewithadel.Mentor.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepo extends JpaRepository<Users, Long> {
}
