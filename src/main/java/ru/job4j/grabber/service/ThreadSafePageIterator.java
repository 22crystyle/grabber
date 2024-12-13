package ru.job4j.grabber.service;

import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import ru.job4j.grabber.model.Post;
import ru.job4j.grabber.utils.HabrCareerDateTimeParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.IntStream;

@Slf4j
public class ThreadSafePageIterator {
    //private static final int NUM_THREADS = Runtime.getRuntime().availableProcessors();
    private static final int NUM_THREADS = 6;
    private static final List<Post> results = new CopyOnWriteArrayList<>();

    public static List<Post> run(String link) {
        List<CompletableFuture<Void>> futures = new ArrayList<>();

        IntStream.range(0, NUM_THREADS).forEach(i -> {
            CompletableFuture.runAsync(() -> {
                try {
                    loadAndProcess(link, i);
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                }
            }).whenComplete((result, exception) -> {
                if (exception != null) {
                    log.error(exception.getMessage(), exception);
                }
            });
        });

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        return results;
    }

    private static void loadAndProcess(String link, int pageCount) throws IOException {
        HabrCareerParse parser = new HabrCareerParse(new HabrCareerDateTimeParser(), pageCount);
        List<Post> posts = parser.list(link);
        results.addAll(posts);
    }
}
