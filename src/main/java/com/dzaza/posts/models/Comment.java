package com.dzaza.posts.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "comment_seq")
    @SequenceGenerator(name = "comment_seq", sequenceName = "comment_id_seq", allocationSize = 1)
    private Long commentId;

    @NotEmpty(message = "Author should not be empty")
    @Size(min = 3, max = 20, message = "Author should be between 3 and 15 characters")
    private String commentAuthor;

    @NotEmpty(message = "Comment should not be empty")
    @Size(min = 3, message = "Comment should not be shorter than 3 characters")
    private String commentContext;

    @ManyToOne
    @JoinColumn(name = "postId", nullable = false)
    private Post post;

    public Long getCommentId() {
        return commentId;
    }

    public void setCommentId(Long commentId)
    {
        this.commentId = commentId;
    }

    public String getCommentAuthor() {
        return commentAuthor;
    }

    public void setCommentAuthor(String commentAuthor) {
        this.commentAuthor = commentAuthor;
    }

    public String getCommentContext() {
        return commentContext;
    }

    public void setCommentContext(String commentContext) {
        this.commentContext = commentContext;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post)
    {
        this.post = post;
    }

    public Comment() {
    }

    public Comment(Post post, String commentAuthor, String commentContext) {
        this.post = post;
        this.commentAuthor = commentAuthor;
        this.commentContext = commentContext;


    }
}
