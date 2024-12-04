package com.bezkoder.springjwt.repository;

import com.bezkoder.springjwt.models.Author;
import jdk.jfr.Registered;
import org.aspectj.apache.bcel.classfile.Module;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

//@Repository
public interface AuthorsRepository extends JpaRepository<Author, Long> {
    Optional<Author> findByName(String userName);
}
