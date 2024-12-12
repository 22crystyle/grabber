package ru.job4j.grabber.stores;

import lombok.extern.slf4j.Slf4j;
import ru.job4j.grabber.model.Post;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
public class JdbcStore implements Store {
    private final Statement statement;

    public JdbcStore(Connection connection) throws SQLException {
        statement = connection.createStatement();
    }

    @Override
    public void save(Post post) throws SQLException {
        String sql = "INSERT INTO post VALUES (%d, '%s', '%s', '%s', %d) ON DUPLICATE KEY UPDATE link='%s'".formatted(
                post.getId(),
                post.getTitle(),
                post.getDescription(),
                post.getLink(),
                post.getTime(),
                post.getLink());
        statement.execute(sql);
    }

    @Override
    public List<Post> getAll() {
        List<Post> posts = new ArrayList<>();
        try {
            ResultSet result = statement.executeQuery("SELECT * FROM post");
            while (result.next()) {
                posts.add(new Post(result.getLong(1), result.getString(2), result.getString(3), result.getString(4), result.getInt(5)));
            }
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
        return posts;
    }

    @Override
    public Optional<Post> findById(Long id) {
        try {
            ResultSet result = statement.executeQuery("SELECT * FROM post WHERE id = %d".formatted(id));
            return result.next() ? Optional.of(new Post(result.getLong(1), result.getString(2), result.getString(3), result.getString(4), result.getInt(5))) : Optional.empty();
        } catch (SQLException ex) {
            log.error(ex.getMessage(), ex);
        }
        return Optional.empty();
    }
}
