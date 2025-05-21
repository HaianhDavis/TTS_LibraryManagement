package com.example.TTS_LibraryManagement.controller;

import com.example.TTS_LibraryManagement.dto.request.Post.LikeOrDislikeRequest;
import com.example.TTS_LibraryManagement.dto.request.Post.PostCreationRequest;
import com.example.TTS_LibraryManagement.dto.request.Post.PostUpdateRequest;
import com.example.TTS_LibraryManagement.dto.response.ApiResponse;
import com.example.TTS_LibraryManagement.dto.response.Dashboard.DashboardPostResponse;
import com.example.TTS_LibraryManagement.dto.response.Post.PostLikeResponse;
import com.example.TTS_LibraryManagement.dto.response.Post.PostResponse;
import com.example.TTS_LibraryManagement.service.PostService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/library/post")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PostController {
    PostService postService;

    @PostMapping("/create/{userId}")
    ApiResponse<PostResponse> createPost(@PathVariable Long userId, @RequestBody PostCreationRequest request) {
        ApiResponse<PostResponse> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Successfully created post!");
        apiResponse.setResult(postService.createPost(userId,request));
        return apiResponse;
    }

    @GetMapping
    ApiResponse<List<PostResponse>> getPosts() {
        ApiResponse<List<PostResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Successfully retrieved posts!");
        apiResponse.setResult(postService.getPosts());
        return apiResponse;
    }

    @GetMapping("/detail/{id}")
    ApiResponse<PostResponse> getPost(@PathVariable Long id) {
        ApiResponse<PostResponse> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Successfully retrieved post!");
        apiResponse.setResult(postService.getPost(id));
        return apiResponse;
    }

    @PutMapping("/update/{id}")
    ApiResponse<PostResponse> updatePost(@PathVariable Long id, @RequestBody PostUpdateRequest request) {
        ApiResponse<PostResponse> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Successfully updated post!");
        apiResponse.setResult(postService.updatePost(id, request));
        return apiResponse;
    }

    @DeleteMapping("/delete/{id}")
    ApiResponse<PostResponse> deletePost(@PathVariable Long id) {
        ApiResponse<PostResponse> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Successfully deleted post!");
        postService.deletePost(id);
        return apiResponse;
    }

    @PatchMapping("/restore/{id}")
    ApiResponse<PostResponse> restorePost(@PathVariable Long id) {
        ApiResponse<PostResponse> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Successfully restored post!");
        postService.restorePost(id);
        return apiResponse;
    }

    @PostMapping("/like/{userId}")
    ApiResponse<PostLikeResponse> likePost(@PathVariable Long userId, @RequestBody LikeOrDislikeRequest request) {
        ApiResponse<PostLikeResponse> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Successfully liked post!");
        apiResponse.setResult(postService.likeOrDislikePost(userId, request));
        return apiResponse;
    }

    @GetMapping("/top-liked")
    ApiResponse<List<DashboardPostResponse>> getTopLikedPosts() {
        ApiResponse<List<DashboardPostResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Successfully retrieved top liked posts!");
        apiResponse.setResult(postService.getListTopLiked());
        return apiResponse;
    }
}
