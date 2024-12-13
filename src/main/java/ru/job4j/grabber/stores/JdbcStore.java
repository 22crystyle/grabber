package ru.job4j.grabber.stores;

import lombok.extern.slf4j.Slf4j;
import ru.job4j.grabber.model.Post;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
public class JdbcStore implements Store {
    private final Connection connection;

    public JdbcStore(Connection connection) throws SQLException {
        this.connection = connection;
    }

    @Override
    public void save(Post post) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO post(name, text, link, created) VALUES (?, ?, ?, ?) ON DUPLICATE KEY UPDATE link=?"
        );

        preparedStatement.setString(1, post.getTitle());
        preparedStatement.setString(2, post.getDescription());
        preparedStatement.setString(3, post.getLink());
        preparedStatement.setLong(4, post.getTime());
        preparedStatement.setString(5, post.getLink());

        preparedStatement.executeUpdate();
        preparedStatement.close();
    }

    @Override
    public List<Post> getAll() {
        List<Post> posts = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery("SELECT * FROM post");
            while (result.next()) {
                posts.add(new Post(result.getLong(1), result.getString(2), result.getString(3), result.getString(4), result.getInt(5)));
            }
            statement.close();
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
        return posts;
    }

    @Override
    public Optional<Post> findById(Long id) {
        try {
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery("SELECT * FROM post WHERE id = %d".formatted(id));
            statement.close();
            return result.next() ? Optional.of(new Post(result.getLong(1), result.getString(2), result.getString(3), result.getString(4), result.getInt(5))) : Optional.empty();
        } catch (SQLException ex) {
            log.error(ex.getMessage(), ex);
        }
        return Optional.empty();
    }

    public void saveList(List<Post> posts) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(
            "INSERT INTO post(name, text, link, created) VALUES (?, ?, ?, ?) ON DUPLICATE KEY UPDATE link=?"
        );

        for (Post post : posts) {
            statement.setString(1, post.getTitle());
            statement.setString(2, post.getDescription());
            statement.setString(3, post.getLink());
            statement.setLong(4, post.getTime());
            statement.setString(5, post.getLink());
            statement.executeUpdate();
        }

        statement.close();
    }
}
