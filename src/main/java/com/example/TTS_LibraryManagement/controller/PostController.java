package com.example.TTS_LibraryManagement.controller;

import com.example.TTS_LibraryManagement.dto.request.Post.LikeOrDislikeRequest;
import com.example.TTS_LibraryManagement.dto.request.Post.PostCreationRequest;
import com.example.TTS_LibraryManagement.dto.request.Post.PostUpdateRequest;
import com.example.TTS_LibraryManagement.dto.response.ApiResponse;
import com.example.TTS_LibraryManagement.dto.response.Dashboard.DashboardPostResponse;
import com.example.TTS_LibraryManagement.dto.response.Post.PostLikeResponse;
import com.example.TTS_LibraryManagement.dto.response.Post.PostResponse;
import com.example.TTS_LibraryManagement.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Post", description = "Post Management APIs")
public class PostController {
    PostService postService;

    @Operation(summary = "Create a new post")
    @PostMapping("/create/{userId}")
    @PreAuthorize("fileRole(#httpServletRequest)")
    ApiResponse<PostResponse> createPost(HttpServletRequest httpServletRequest, @PathVariable Long userId, @RequestBody PostCreationRequest request) {
        ApiResponse<PostResponse> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Successfully created post!");
        apiResponse.setResult(postService.createPost(userId, request));
        return apiResponse;
    }

    @Operation(summary = "Get all posts")
    @GetMapping
    @PreAuthorize("fileRole(#httpServletRequest)")
    ApiResponse<List<PostResponse>> getPosts(HttpServletRequest httpServletRequest) {
        ApiResponse<List<PostResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Successfully retrieved posts!");
        apiResponse.setResult(postService.getPosts());
        return apiResponse;
    }

    @Operation(summary = "Get posts by user ID")
    @GetMapping("/detail/{id}")
    @PreAuthorize("fileRole(#httpServletRequest)")
    ApiResponse<PostResponse> getPost(HttpServletRequest httpServletRequest, @PathVariable Long id) {
        ApiResponse<PostResponse> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Successfully retrieved post!");
        apiResponse.setResult(postService.getPost(id));
        return apiResponse;
    }

    @Operation(summary = "Search posts by title")
    @PutMapping("/update/{id}")
    @PreAuthorize("fileRole(#httpServletRequest)")
    ApiResponse<PostResponse> updatePost(HttpServletRequest httpServletRequest, @PathVariable Long id, @RequestBody PostUpdateRequest request) {
        ApiResponse<PostResponse> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Successfully updated post!");
        apiResponse.setResult(postService.updatePost(id, request));
        return apiResponse;
    }

    @Operation(summary = "Delete a post")
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("fileRole(#httpServletRequest)")
    ApiResponse<PostResponse> deletePost(HttpServletRequest httpServletRequest, @PathVariable Long id) {
        ApiResponse<PostResponse> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Successfully deleted post!");
        postService.deletePost(id);
        return apiResponse;
    }

    @Operation(summary = "Like or dislike a post")
    @PatchMapping("/restore/{id}")
    @PreAuthorize("fileRole(#httpServletRequest)")
    ApiResponse<PostResponse> restorePost(HttpServletRequest httpServletRequest, @PathVariable Long id) {
        ApiResponse<PostResponse> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Successfully restored post!");
        postService.restorePost(id);
        return apiResponse;
    }

    @Operation(summary = "Get posts by pagination")
    @PostMapping("/like/{userId}")
    @PreAuthorize("fileRole(#httpServletRequest)")
    ApiResponse<PostLikeResponse> likePost(HttpServletRequest httpServletRequest, @PathVariable Long userId, @RequestBody LikeOrDislikeRequest request) {
        ApiResponse<PostLikeResponse> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Successfully liked post!");
        apiResponse.setResult(postService.likeOrDislikePost(userId, request));
        return apiResponse;
    }

    @Operation(summary = "Get top liked posts")
    @GetMapping("/top-liked")
    @PreAuthorize("fileRole(#httpServletRequest)")
    ApiResponse<List<DashboardPostResponse>> getTopLikedPosts(HttpServletRequest httpServletRequest) {
        ApiResponse<List<DashboardPostResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Successfully retrieved top liked posts!");
        apiResponse.setResult(postService.getListTopLiked());
        return apiResponse;
    }
}
