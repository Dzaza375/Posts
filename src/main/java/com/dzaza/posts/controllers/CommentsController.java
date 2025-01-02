package com.dzaza.posts.controllers;

import com.dzaza.posts.dao.CommentDAO;
import com.dzaza.posts.dao.PostDAO;
import com.dzaza.posts.models.Comment;
import com.dzaza.posts.models.Post;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class CommentsController {
    private final PostDAO postDAO;
    private final CommentDAO commentDAO;

    public CommentsController(PostDAO postDAO, CommentDAO commentDAO) {
        this.postDAO = postDAO;
        this.commentDAO = commentDAO;
    }

    @PostMapping("/posts/{id}/add-comment")
    public String addComment(@Valid @ModelAttribute("comment") Comment comment, BindingResult bindingResult, @PathVariable(value = "id") Long postId, Model model) {
        if (bindingResult.hasErrors()) {
            List<Post> posts = postDAO.findAll();
            model.addAttribute("posts", posts);
            model.addAttribute("postId", postId);
            model.addAttribute("comment", comment);
            return "posts";
        }

        Post post = postDAO.findById(postId);
        comment.setPost(post);
        commentDAO.add(comment);
        return "redirect:/posts";
    }

    @DeleteMapping("/posts/{id}/remove-comment")
    public String removeComment(@PathVariable(value = "id") Long id) {
        Comment comment = commentDAO.findById(id);
        commentDAO.delete(comment);

        return "redirect:/posts";
    }
}
