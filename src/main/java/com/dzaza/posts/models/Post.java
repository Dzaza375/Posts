package com.dzaza.posts.models;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Post
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title, author, imageURL;

    @OneToMany(mappedBy = "post")
    private List<Comment> comments;

    public Long getId()
    {
        return id;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getAuthor()
    {
        return author;
    }

    public void setAuthor(String author)
    {
        this.author = author;
    }

    public String getImageURL()
    {
        return imageURL;
    }

    public void setImageURL(String imageURL)
    {
        this.imageURL = imageURL;
    }

    public List<Comment> getComments()
    {
        return comments;
    }

    public Post()
    {
    }

    public Post(String title, String author, String imageURL)
    {
        this.title = title;
        this.author = author;
        this.imageURL = imageURL;
    }
}
