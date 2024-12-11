package ru.job4j.grabber.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.job4j.grabber.model.Post;
import ru.job4j.grabber.repository.PostRespository;

@Service
public class PostService {
    private final PostRespository postRespository;

    @Autowired
    PostService(PostRespository postRespository) {
        this.postRespository = postRespository;
    }

    public Long save(Post post) {
        return postRespository.save(post).getId();
    }

    public Post findById(Long id) {
        return postRespository.findById(id).orElse(null);
    }
}