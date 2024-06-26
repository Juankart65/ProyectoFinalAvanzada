package com.avanzada.unilocal.Unilocal.repository;

import com.avanzada.unilocal.Unilocal.entity.Person;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepository extends MongoRepository<Person, String> {

    boolean existsByNickname(String nickname);
    boolean existsByEmail(String email);
    Optional<Person> findByNickname(String nickname);
    Optional<Person> findByEmail(String email);
}
