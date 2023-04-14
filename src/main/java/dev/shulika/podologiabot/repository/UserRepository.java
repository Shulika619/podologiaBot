package dev.shulika.podologiabot.repository;


import dev.shulika.podologiabot.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}