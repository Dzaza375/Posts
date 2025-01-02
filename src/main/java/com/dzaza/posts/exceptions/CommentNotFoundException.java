package com.dzaza.posts.exceptions;

public class CommentNotFoundException extends RuntimeException {
    public CommentNotFoundException() {
        super("The comment was not found");
    }
}
