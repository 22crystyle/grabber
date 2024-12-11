package ru.job4j.grabber.service;

import ru.job4j.grabber.model.Post;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface Store {
    void save(Post post) throws SQLException;

    List<Post> getAll();

    Optional<Post> findById(Long id);
}
