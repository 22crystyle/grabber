package ru.job4j.grabber.stores;

import ru.job4j.grabber.model.Post;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface Store {
    void save(Post post) throws SQLException;

    List<Post> getAll();

    long getCount();

    Optional<Post> findById(Long id);

    void saveList(List<Post> posts) throws SQLException;
}
