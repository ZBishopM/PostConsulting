package com.carlosobispo.postconsulting.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.carlosobispo.postconsulting.models.Post;
import com.carlosobispo.postconsulting.services.PostService;



@RestController
@RequestMapping("/posts")
public class PostController {
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }
    @GetMapping("")
    public ResponseEntity<List<Post>> getPosts() {
        List<Post> posts = postService.findAll();
        if (posts != null && !posts.isEmpty()) {
            return ResponseEntity.ok(posts);
        }
        return ResponseEntity.notFound().build();
    }
    
    @PostMapping("")
    public ResponseEntity<Post> addPost(@RequestBody Post post) {
        Post savedPost = postService.save(post);
        if (savedPost != null) {
            return ResponseEntity.ok(savedPost);
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Post> getById(@PathVariable Long id) {
        Post post = postService.findById(id);
        if (post != null) {
            return ResponseEntity.ok(post);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/users/{username}/posts")
    public ResponseEntity<List<Post>> getPostsByUser(@PathVariable String username) {
        List<Post> posts = postService.findByUsername(username);
        if (!posts.isEmpty()) {
            return ResponseEntity.ok(posts);
        }
        return ResponseEntity.notFound().build();
    }
}
