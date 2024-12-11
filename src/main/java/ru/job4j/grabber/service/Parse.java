package ru.job4j.grabber.service;

import lombok.extern.slf4j.Slf4j;
import ru.job4j.grabber.model.Post;

import java.util.List;

public interface Parse {
    List<Post> fetch();
}
