package ru.job4j.grabber.stores;

import lombok.Getter;
import ru.job4j.grabber.model.Post;

import java.util.*;

public class MemStore implements Store {
    @Getter
    private static long autoIncrement = 1;
    private final Map<Long, Post> mem = new HashMap<>();

    @Override
    public void save(Post post) {
        mem.put(autoIncrement++, post);
    }

    @Override
    public List<Post> getAll() {
        return new ArrayList<>(mem.values());
    }

    @Override
    public long getCount() {
        return mem.size();
    }

    @Override
    public Optional<Post> findById(Long id) {
        return Optional.ofNullable(mem.get(id));
    }

    @Override
    public void saveList(List<Post> posts) {
        for (Post post : posts) {
            save(post);
        }
    }
}
