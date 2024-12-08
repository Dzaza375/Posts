package com.dzaza.posts.controllers;

import com.dzaza.posts.models.Comment;
import com.dzaza.posts.models.Post;
import com.dzaza.posts.repo.CommentRepository;
import com.dzaza.posts.repo.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CommentsController
{
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private PostRepository postRepository;

    @PostMapping("/posts/{id}/add-comment")
    public String addComment(@RequestParam("commentAuthor") String commentAuthor, @RequestParam("commentContext") String commentContext, @PathVariable(value = "id") long postId, Model model)
    {
        Post post = postRepository.findById(postId).orElseThrow();
        Comment comment = new Comment(post, commentAuthor, commentContext);
        commentRepository.save(comment);
        return "redirect:/posts";
    }

    @PostMapping("/posts/{id}/edit-comment")
    public String editComment(@RequestParam("commentAuthor") String commentAuthor, @RequestParam("commentContext") String commentContext, @PathVariable(value = "id") long postId, Model model)
    {
        Comment comment = commentRepository.findById(postId).orElseThrow();
        comment.setCommentAuthor(commentAuthor);
        comment.setCommentContext(commentContext);
        commentRepository.save(comment);

        return "redirect:/posts";
    }

    @PostMapping("/posts/{id}/remove-comment")
    public String removeComment(@PathVariable(value = "id") long id, Model model)
    {
        Comment comment = commentRepository.findById(id).orElseThrow();
        commentRepository.delete(comment);

        return "redirect:/posts";
    }
}
