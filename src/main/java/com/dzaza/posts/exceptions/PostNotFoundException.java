package com.dzaza.posts.exceptions;

public class PostNotFoundException extends RuntimeException {
    public PostNotFoundException() {
        super("The post was not found");
    }
}
