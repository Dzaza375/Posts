package com.dzaza.posts.models;

import jakarta.persistence.*;

@Entity
public class Comment
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long commentId;

    public String commentAuthor, commentContext;

    @ManyToOne
    @JoinColumn(name = "postId", nullable = false)
    private Post post;

    public Long getCommentId()
    {
        return commentId;
    }

    public String getCommentAuthor()
    {
        return commentAuthor;
    }

    public void setCommentAuthor(String commentAuthor)
    {
        this.commentAuthor = commentAuthor;
    }

    public String getCommentContext()
    {
        return commentContext;
    }

    public void setCommentContext(String commentContext)
    {
        this.commentContext = commentContext;
    }

    public Post getPost()
    {
        return post;
    }

    public Comment()
    {
    }

    public Comment(Post post, String commentAuthor, String commentContext)
    {
        this.post = post;
        this.commentAuthor = commentAuthor;
        this.commentContext = commentContext;


    }
}
