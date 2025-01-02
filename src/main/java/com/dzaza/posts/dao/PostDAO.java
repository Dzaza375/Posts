package com.dzaza.posts.dao;

import com.dzaza.posts.exceptions.PostNotFoundException;
import com.dzaza.posts.models.Comment;
import com.dzaza.posts.models.Post;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class PostDAO {
    private static final String URL = "jdbc:postgresql://localhost:5432/posts";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "8911";

    private static final Connection connection;

    static {
        try
        {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }

    public List<Post> findAll() {
        List<Post> posts = new ArrayList<>();

        try
        {
            Statement statement = connection.createStatement();
            String SQL = "SELECT * FROM Post";
            ResultSet resultSet = statement.executeQuery(SQL);

            while(resultSet.next()) {
                Post post = setData(resultSet.getLong("id"),
                        resultSet.getString("title"),
                        resultSet.getString("author"),
                        resultSet.getString("imageURL"));

                posts.add(post);
            }
        } catch (SQLException e)
        {
            throw new RuntimeException(e);
        }

        return posts;
    }

    public Post findById(Long id) {
        Post post = null;

        try
        {
            PreparedStatement preparedStatement =
                    connection.prepareStatement("SELECT * FROM Post WHERE id=?");

            preparedStatement.setLong(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()) {
                post = setData(resultSet.getLong("id"),
                        resultSet.getString("title"),
                        resultSet.getString("author"),
                        resultSet.getString("imageURL"));
            } else {
                throw new PostNotFoundException();
            }

        } catch (SQLException e)
        {
            throw new RuntimeException(e);
        }

        return post;
    }

    public void add(Post post) {
        try
        {
            PreparedStatement preparedStatement =
                    connection.prepareStatement("INSERT INTO Post (title, author, imageURL) VALUES(?, ?, ?)");

            preparedStatement.setString(1, post.getTitle());
            preparedStatement.setString(2, post.getAuthor());
            preparedStatement.setString(3, post.getImageURL());

            preparedStatement.executeUpdate();
        } catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }

    public void edit(Post post) {
        try
        {
            PreparedStatement preparedStatement =
                    connection.prepareStatement("UPDATE Post SET " +
                            "title=?, author=?, imageURL=? WHERE id=?");

            preparedStatement.setString(1, post.getTitle());
            preparedStatement.setString(2, post.getAuthor());
            preparedStatement.setString(3, post.getImageURL());
            preparedStatement.setLong(4, post.getId());

            preparedStatement.executeUpdate();
        } catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }

    public void delete(Post post) {
        try
        {
            PreparedStatement preparedStatement =
                    connection.prepareStatement("DELETE FROM Post WHERE id=?");

            preparedStatement.setLong(1, post.getId());

            preparedStatement.executeUpdate();
        } catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }

    private Post setData(Long id, String title, String author, String imageURL) {
        Post post = new Post();

        post.setId(id);
        post.setTitle(title);
        post.setAuthor(author);
        post.setImageURL(imageURL);

        post.setComments(findCommentsByPostId(post.getId(), post));

        return post;
    }

    private List<Comment> findCommentsByPostId(Long postId, Post post) {
        List<Comment> comments = new ArrayList<>();

        try
        {
            PreparedStatement preparedStatement =
                    connection.prepareStatement("SELECT * FROM Comment WHERE post_id=?");

            preparedStatement.setLong(1, postId);

            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()) {
                Comment comment = new Comment();

                comment.setCommentId(resultSet.getLong("comment_id"));
                comment.setCommentAuthor(resultSet.getString("comment_author"));
                comment.setCommentContext(resultSet.getString("comment_context"));

                comment.setPost(post);

                comments.add(comment);
            }
        } catch (SQLException e)
        {
            throw new RuntimeException(e);
        }

        return comments;
    }
}
