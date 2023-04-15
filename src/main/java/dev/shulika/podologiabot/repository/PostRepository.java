package dev.shulika.podologiabot.repository;


import dev.shulika.podologiabot.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}