package ru.job4j.grabber.service;

import jakarta.annotation.Nullable;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import ru.job4j.grabber.model.Post;
import ru.job4j.grabber.utils.DateTimeParser;
import ru.job4j.grabber.utils.HabrCareerDateTimeParser;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.time.format.DateTimeFormatter.BASIC_ISO_DATE;

@Slf4j
public class HabrCareerParse implements Parse {
    private static final String SOURCE_LINK = "https://career.habr.com";
    private static final String PREFIX = "/vacancies?page=";
    private static final String SUFFIX = "&q=Java%20developer&type=all";
    private static final int PAGE_COUNT = 5;

    private final DateTimeParser dateTimeParser;
    public HabrCareerParse(DateTimeParser dateTimeParser) {
        this.dateTimeParser = dateTimeParser;
    }

    @Override
    public List<Post> list(String link) {
        var result = new ArrayList<Post>();

        try {
            for (int pageNumber = 1; pageNumber <= PAGE_COUNT; pageNumber++) {
                String fullLink = link.replace("page=1", "page=" + pageNumber);
                var connection = Jsoup.connect(fullLink);
                var document = connection.get();
                Elements rows = document.select(".vacancy-card__inner");
                rows.forEach(row -> {
                    var titleElement = row.select(".vacancy-card__title").first();
                    var linkElement = titleElement.child(0);
                    var dateElement = row.select(".vacancy-card__date").first().child(0).attr("datetime");
                    var descriptionLink = SOURCE_LINK + linkElement.attr("href");

                    String vacancyName = titleElement.text();
                    String vacancyLink = String.format("%s%s", SOURCE_LINK, linkElement.attr("href"));
                    String date = dateTimeParser.parse(dateElement).format(BASIC_ISO_DATE);

                    var post = new Post();
                    post.setTitle(vacancyName);
                    post.setLink(vacancyLink);
                    post.setTime(Long.parseLong(date));
                    post.setDescription(retrieveDescription(descriptionLink).replace('\'', '\"'));
                    result.add(post);
                });
            }
        } catch (IOException io) {
            log.error(io.getMessage(), io);
        }

        return result;
    }

    public String retrieveDescription(String link) {
        String description = "";
        try {
            var connection = Jsoup.connect(link);
            var document = connection.get();
            var descriptionElement = Objects.requireNonNull(document.select(".vacancy-description__text").first()).text();
            description = descriptionElement.trim();

        } catch (IOException io) {
            log.error(io.getMessage(), io);
        }

        return description;
    }
}
