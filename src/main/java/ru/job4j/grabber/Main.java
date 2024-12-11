package ru.job4j.grabber;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.job4j.grabber.model.JdbcStore;
import ru.job4j.grabber.model.Post;
import ru.job4j.grabber.service.*;
import ru.job4j.grabber.utils.HabrCareerDateTimeParser;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

public class Main {
    public static final Logger log = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        var config = new Config();
        config.load(Objects.requireNonNull(Main.class.getClassLoader().getResource("application.properties")).getFile());

        try {
            var store = new JdbcStore(DriverManager.getConnection(
                    config.get("spring.datasource.url"),
                    config.get("spring.datasource.name"),
                    config.get("spring.datasource.password")));

            /*var post1 = new Post(1L, "Title", "Description", "Link", 123L);
            var post2 = new Post(2L, "Title2", "Description2", "Link2", 123L);
            var post3 = new Post(3L, "Title3", "Description3", "Link3", 123L);
            store.save(post1);
            store.save(post2);
            store.save(post3);*/

            Post post1 = new Post(255L, "Title", "Links", "Направление разработки 'Management system' открыто много лет назад, так как всем этим оборудованием нужно как-то управлять.", 1L);

            HabrCareerParse parse = new HabrCareerParse(new HabrCareerDateTimeParser());
            List<Post> list = parse.list("https://career.habr.com/vacancies?page=1&q=Java%20developer&type=all");
            for (Post post : list) {
                store.save(post);
            }

            var scheduler = new SchedulerManager();
            scheduler.init();
            scheduler.load(
                    Integer.parseInt(config.get("rabbit.interval")),
                    SuperJobGrab.class,
                    store);
        } catch (SQLException sql) {
            log.error("When create a connection", sql);
        }
    }
}
