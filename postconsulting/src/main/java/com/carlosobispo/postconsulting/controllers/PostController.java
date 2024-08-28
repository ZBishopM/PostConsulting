package com.carlosobispo.postconsulting.controllers;

import java.security.Principal;
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
import com.carlosobispo.postconsulting.services.UserService;



@RestController
@RequestMapping("/posts")
public class PostController {
    private final PostService postService;
    private final UserService userService;

    public PostController(PostService postService, UserService userService) {
        this.postService = postService;
        this.userService = userService;
    }
    @GetMapping("")
    public ResponseEntity<List<Post>> getPosts() {
        List<Post> posts = postService.findAll();
        postService.countStats(posts);
        if (posts != null && !posts.isEmpty()) {
            return ResponseEntity.ok(posts);
        }
        return ResponseEntity.notFound().build();
    }
    
    @PostMapping("")
    public ResponseEntity<Post> addPost(@RequestBody Post post, Principal principal) {
        post.setUser(userService.findByEmail(principal.getName()));
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

    @GetMapping("/delete/{id}")
    public ResponseEntity<Post> deleteUser(@PathVariable Long id) {
        postService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/edit/{id}")
    public ResponseEntity<Post> editPost(@RequestBody Post post,@PathVariable Long id) {
        Post oldPost =postService.findById(id);
        if (oldPost != null) {
            post.setId(oldPost.getId());
            Post savedPost = postService.save(post);
            if (savedPost != null) {
                return ResponseEntity.ok(savedPost);
            }
        }
        return ResponseEntity.badRequest().build();
    }
    
}
