package com.example.TTS_LibraryManagement.controller;

import com.example.TTS_LibraryManagement.dto.request.Post.LikeOrDislikeRequest;
import com.example.TTS_LibraryManagement.dto.request.Post.PostCreationRequest;
import com.example.TTS_LibraryManagement.dto.request.Post.PostUpdateRequest;
import com.example.TTS_LibraryManagement.dto.response.ApiResponse;
import com.example.TTS_LibraryManagement.dto.response.Dashboard.DashboardPostResponse;
import com.example.TTS_LibraryManagement.dto.response.Post.PostLikeResponse;
import com.example.TTS_LibraryManagement.dto.response.Post.PostResponse;
import com.example.TTS_LibraryManagement.service.PostService;
import com.example.TTS_LibraryManagement.utils.ApiUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
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
    @PostMapping("/create")
    @PreAuthorize("fileRole(#httpServletRequest)")
    ApiResponse<PostResponse> createPost(HttpServletRequest httpServletRequest, @RequestBody @Valid PostCreationRequest request) {
        return ApiUtils.success(postService.createPost(request));
    }

    @Operation(summary = "Get all posts")
    @GetMapping
    @PreAuthorize("fileRole(#httpServletRequest)")
    ApiResponse<List<PostResponse>> getPosts(HttpServletRequest httpServletRequest) {
        return ApiUtils.success(postService.getPosts());
    }

    @Operation(summary = "Get posts by user ID")
    @GetMapping("/detail/{id}")
    @PreAuthorize("fileRole(#httpServletRequest)")
    ApiResponse<PostResponse> getPost(HttpServletRequest httpServletRequest, @PathVariable Long id) {
        return ApiUtils.success(postService.getPost(id));
    }

    @Operation(summary = "Update a post")
    @PutMapping("/update/{id}")
    @PreAuthorize("fileRole(#httpServletRequest)")
    ApiResponse<PostResponse> updatePost(HttpServletRequest httpServletRequest, @PathVariable Long id, @RequestBody @Valid PostUpdateRequest request) {
        return ApiUtils.success(postService.updatePost(id, request));
    }

    @Operation(summary = "Delete a post")
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("fileRole(#httpServletRequest)")
    ApiResponse<PostResponse> deletePost(HttpServletRequest httpServletRequest, @PathVariable Long id) {
        postService.deletePost(id);
        return ApiUtils.successDeleteOrRestore("Successfully deleted post with ID: " + id);
    }

    @Operation(summary = "Like or dislike a post")
    @PatchMapping("/restore/{id}")
    @PreAuthorize("fileRole(#httpServletRequest)")
    ApiResponse<PostResponse> restorePost(HttpServletRequest httpServletRequest, @PathVariable Long id) {
        postService.restorePost(id);
        return ApiUtils.successDeleteOrRestore("Successfully restored post with ID: " + id);
    }

    @Operation(summary = "Get posts by pagination")
    @PostMapping("/like/{userId}")
    @PreAuthorize("fileRole(#httpServletRequest)")
    ApiResponse<PostLikeResponse> likePost(HttpServletRequest httpServletRequest, @PathVariable Long userId, @RequestBody LikeOrDislikeRequest request) {
        return ApiUtils.success(postService.likeOrDislikePost(userId, request));
    }

    @Operation(summary = "Get top liked posts")
    @GetMapping("/top-liked")
    @PreAuthorize("fileRole(#httpServletRequest)")
    ApiResponse<List<DashboardPostResponse>> getTopLikedPosts(HttpServletRequest httpServletRequest) {
        return ApiUtils.success(postService.getListTopLiked());
    }
}
