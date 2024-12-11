package ru.job4j.grabber.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.job4j.grabber.model.Post;
import ru.job4j.grabber.service.PostService;

@RestController
@RequestMapping("/api")
public class PostController {
    private final PostService postService;

    @Autowired
    PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping("/create")
    public long createPost(@RequestBody Post post) {
        return postService.save(post);
    }

    @GetMapping("/{id}")
    public Post getPost(@PathVariable long id) {
        System.out.println(postService.findById(id));
        return postService.findById(id);
    }
}
