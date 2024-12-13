package ru.job4j.grabber.service;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.job4j.grabber.model.Post;
import ru.job4j.grabber.utils.DateTimeParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.time.format.DateTimeFormatter.BASIC_ISO_DATE;

@Slf4j
public class HabrCareerParse implements Parse {
    private static final String SOURCE_LINK = "https://career.habr.com";
    private final int pageCount;

    private final DateTimeParser dateTimeParser;

    /*public HabrCareerParse(DateTimeParser dateTimeParser) {
        this.dateTimeParser = dateTimeParser;
    }*/

    public HabrCareerParse(DateTimeParser dateTimeParser, int pageCount) {
        this.dateTimeParser = dateTimeParser;
        this.pageCount = pageCount;
    }

    @Override
    public List<Post> list(String link) {
        var result = new ArrayList<Post>();

        try {
            String fullLink = link.replace("page=1", "page=" + pageCount);
            log.info("full link: {}", fullLink);
            var connection = Jsoup.connect(fullLink);
            var document = connection.get();
            Elements rows = document.select(".vacancy-card__inner");
            rows.forEach(row -> {
                var titleElement = row.select(".vacancy-card__title").first();
                Element linkElement = null;
                if (titleElement != null) {
                    linkElement = titleElement.child(0);
                }
                var dateElement = Objects.requireNonNull(row.select(".vacancy-card__date").first()).child(0).attr("datetime");
                var descriptionLink = SOURCE_LINK + (linkElement != null ? linkElement.attr("href") : null);

                String vacancyName = titleElement != null ? titleElement.text() : null;
                String vacancyLink = String.format(
                        "%s%s",
                        SOURCE_LINK,
                        linkElement != null ? linkElement.attr("href") : null);
                String date = dateTimeParser.parse(dateElement).format(BASIC_ISO_DATE);

                var post = new Post();
                post.setTitle(vacancyName);
                post.setLink(vacancyLink);
                post.setTime(Long.parseLong(date));
                post.setDescription(retrieveDescription(descriptionLink).replace('\'', '\"'));
                result.add(post);
            });
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
