package com.dzaza.posts.controllers;

import com.dzaza.posts.dao.CommentDAO;
import com.dzaza.posts.dao.PostDAO;
import com.dzaza.posts.models.Comment;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import com.dzaza.posts.models.Post;

import java.util.List;

@Controller
public class PostsController {
    private final PostDAO postDAO;
    private final CommentDAO commentDAO;

    public PostsController(PostDAO postDAO, CommentDAO commentDAO) {
        this.postDAO = postDAO;
        this.commentDAO = commentDAO;
    }

    @GetMapping("/posts")
    public String posts(Model model) {
        List<Post> posts = postDAO.findAll();
        model.addAttribute("posts", posts);
        List<Comment> comments = commentDAO.findAll();
        model.addAttribute("comments", comments);
        return "posts";
    }

    @GetMapping("/posts/add")
    public String addPosts(@ModelAttribute("post") Post post) {
        return "add-posts";
    }

    @PostMapping("/posts/add")
    public String blogPostAdd(@Valid @ModelAttribute("post") Post post, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "add-posts";

        postDAO.add(post);
        return "redirect:/posts";
    }

    @GetMapping("/posts/{id}/edit")
    public String postDetails(@PathVariable(value = "id") Long id, Model model) {
        Post post = postDAO.findById(id);
        model.addAttribute("post", post);
        return "blog-edit";
    }

    @PatchMapping("/posts/{id}/edit")
    public String updateDetails(@Valid @ModelAttribute("post") Post post, BindingResult bindingResult, @PathVariable(value = "id") Long id) {
        if (bindingResult.hasErrors())
            return "blog-edit";

        Post postNeedToBeUpdated = postDAO.findById(id);
        postNeedToBeUpdated.setTitle(post.getTitle());
        postNeedToBeUpdated.setAuthor(post.getAuthor());
        postNeedToBeUpdated.setImageURL(post.getImageURL());
        postDAO.edit(postNeedToBeUpdated);

        return "redirect:/posts";
    }

    @DeleteMapping("/posts/{id}/remove")
    public String removePost(@PathVariable(value = "id") Long id, Model model) {
        Post post = postDAO.findById(id);
        postDAO.delete(post);

        return "redirect:/posts";
    }
}
