package ru.job4j.grabber.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.job4j.grabber.model.Post;

public interface PostRespository extends JpaRepository<Post, Long> {
}
