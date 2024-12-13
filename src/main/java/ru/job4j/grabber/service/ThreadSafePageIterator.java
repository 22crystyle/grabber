package ru.job4j.grabber.service;

import lombok.extern.slf4j.Slf4j;
import ru.job4j.grabber.model.Post;
import ru.job4j.grabber.utils.HabrCareerDateTimeParser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;

@Slf4j
public class ThreadSafePageIterator {
    private static final int NUM_THREADS = Runtime.getRuntime().availableProcessors();
    //private static final int NUM_THREADS = 5;
    private static final List<Post> results = Collections.synchronizedList(new ArrayList<>());

    public static List<Post> run(String link) {
        ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);

        for (int i = 1; i <= NUM_THREADS; i++) {
            final int threadIndex = i;
            executor.submit(() -> {
                    String pageLink = String.format("https://career.habr.com/vacancies?page=%d&q=Java%%20developer&type=all", threadIndex);
                    HabrCareerParse parser = new HabrCareerParse(new HabrCareerDateTimeParser(), threadIndex);
                    List<Post> posts = parser.list(pageLink);
                    log.info("----------- pageLink: {}", pageLink);
                    results.addAll(posts);
                    /*synchronized (results) {
                        results.addAll(posts);
                    }*/
            });
        }

        executor.shutdown();
        try {
            if (!executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            throw new RuntimeException(e);
        }

        return results;
    }

}
