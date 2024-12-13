package ru.job4j.grabber.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data @AllArgsConstructor @NoArgsConstructor
public class Post {
    private Long id;
    private String title;
    private String link;
    private String description;
    private long time;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Post post)) return false;
        return Objects.equals(getId(), post.getId()) && Objects.equals(getTitle(), post.getTitle()) && Objects.equals(getLink(), post.getLink());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getTitle(), getLink());
    }

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", link='" + link + '\'' +
                ", description='" + description + '\'' +
                ", time=" + time +
                "}";
    }
}
