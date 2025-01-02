package com.dzaza.posts.dao;

import com.dzaza.posts.exceptions.CommentNotFoundException;
import com.dzaza.posts.models.Comment;
import com.dzaza.posts.models.Post;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class CommentDAO {
    private static final String URL = "jdbc:postgresql://localhost:5432/posts";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "8911";

    private static final Connection connection;

    private final PostDAO postDAO;

    static {
        try
        {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }

    public CommentDAO(PostDAO postDAO)
    {
        this.postDAO = postDAO;
    }

    public List<Comment> findAll() {
       List<Comment> comments = new ArrayList<>();

        try
        {
            Statement statement = connection.createStatement();
            String SQL = "SELECT * FROM Comment";
            ResultSet resultSet = statement.executeQuery(SQL);

            while(resultSet.next()) {
                Comment comment = new Comment();

                comment.setCommentId(resultSet.getLong("comment_id"));
                comment.setCommentAuthor(resultSet.getString("comment_author"));
                comment.setCommentContext(resultSet.getString("comment_context"));

                Long postId = resultSet.getLong("post_id");

                Post post = postDAO.findById(postId);
                comment.setPost(post);

                comments.add(comment);
            }
        } catch (SQLException e)
        {
            throw new RuntimeException(e);
        }

        return comments;
    }

    private Comment setData(Long commentId, String commentAuthor, String commentContext, Long postId) {
        Comment comment = new Comment();

        comment.setCommentId(commentId);
        comment.setCommentAuthor(commentAuthor);
        comment.setCommentContext(commentContext);

        Post post = postDAO.findById(postId);
        comment.setPost(post);

        return comment;
    }

    public Comment findById(Long commentId) {
        Comment comment = null;

        try
        {
            PreparedStatement preparedStatement =
                    connection.prepareStatement("SELECT * FROM Comment WHERE comment_id=?");

            preparedStatement.setLong(1, commentId);

            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()) {
                comment = setData(resultSet.getLong("comment_id"),
                        resultSet.getString("comment_author"),
                        resultSet.getString("comment_context"),
                        resultSet.getLong("post_id"));
            } else {
                throw new CommentNotFoundException();
            }
        } catch (SQLException e)
        {
            throw new RuntimeException(e);
        }

        return comment;
    }

    public void add(Comment comment) {
        try
        {
            PreparedStatement preparedStatement =
                    connection.prepareStatement("INSERT INTO Comment (comment_author, comment_context, post_id) VALUES(?, ?, ?)");

            preparedStatement.setString(1, comment.getCommentAuthor());
            preparedStatement.setString(2, comment.getCommentContext());
            preparedStatement.setLong(3, comment.getPost().getId());

            preparedStatement.executeUpdate();

        } catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }

    public void delete(Comment comment) {
        try
        {
            PreparedStatement preparedStatement =
                    connection.prepareStatement("DELETE FROM Comment WHERE comment_id=?");

            preparedStatement.setLong(1, comment.getCommentId());

            preparedStatement.executeUpdate();
        } catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }
}
