package ru.job4j.grabber;

import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.job4j.grabber.stores.JdbcStore;
import ru.job4j.grabber.model.Post;
import ru.job4j.grabber.service.*;
import ru.job4j.grabber.stores.MemStore;
import ru.job4j.grabber.utils.HabrCareerDateTimeParser;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

@Slf4j
public class Main {
    public static void main(String[] args) {
        var config = new Config();
        config.load(Objects.requireNonNull(Main.class.getClassLoader().getResource("application.properties")).getFile());

        try {
            var store = new JdbcStore(DriverManager.getConnection(
                    config.get("spring.datasource.url"),
                    config.get("spring.datasource.name"),
                    config.get("spring.datasource.password")));

            List<Post> list = ThreadSafePageIterator.run("https://career.habr.com/vacancies?page=%d&q=Java%20developer&type=all");
            store.saveList(list);

            var scheduler = new SchedulerManager();
            scheduler.init();
            scheduler.load(
                    Integer.parseInt(config.get("rabbit.interval")),
                    SuperJobGrab.class,
                    store);

            new Web(store).start(Integer.parseInt(config.get("server.port")));
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
    }
}
