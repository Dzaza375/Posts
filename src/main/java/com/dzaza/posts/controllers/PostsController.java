package com.dzaza.posts.controllers;

import com.dzaza.posts.models.Comment;
import com.dzaza.posts.repo.CommentRepository;
import com.dzaza.posts.repo.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.dzaza.posts.models.Post;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PostsController
{
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private CommentRepository commentRepository;

    @GetMapping("/posts")
    public String posts(Model model)
    {
        Iterable<Post> posts = postRepository.findAll();
        model.addAttribute("posts", posts);
        Iterable<Comment> comments = commentRepository.findAll();
        model.addAttribute("comments", comments);
        return "posts";
    }

    @GetMapping("/posts/add")
    public String addPosts()
    {
        return "add-posts";
    }

    @PostMapping("/posts/add")
    public String blogPostAdd(@RequestParam("title") String title, @RequestParam("author") String author, @RequestParam("imageURL") String imageURL, Model model)
    {
        Post post = new Post(title, author, imageURL);
        postRepository.save(post);
        return "redirect:/posts";
    }

    @GetMapping("/posts/{id}/edit")
    public String postDetails(@PathVariable(value = "id") long id, Model model)
    {
        if (!postRepository.existsById(id))
        {
            return "redirect:/blog";
        }

        Post post = postRepository.findById(id).orElseThrow();
        model.addAttribute("post", post);
        return "blog-edit";
    }

    @PostMapping("/posts/{id}/edit")
    public String updateDetails(@RequestParam("title") String title, @RequestParam("author") String author, @RequestParam("imageURL") String imageURL, @PathVariable(value = "id") long id, Model model)
    {
        Post post = postRepository.findById(id).orElseThrow();
        post.setTitle(title);
        post.setAuthor(author);
        post.setImageURL(imageURL);
        postRepository.save(post);

        return "redirect:/posts";
    }

    @PostMapping("/posts/{id}/remove")
    public String removePost(@PathVariable(value = "id") long id, Model model)
    {
        Post post = postRepository.findById(id).orElseThrow();
        postRepository.delete(post);

        return "redirect:/posts";
    }
}
