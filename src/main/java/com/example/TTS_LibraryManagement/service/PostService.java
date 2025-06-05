package com.example.TTS_LibraryManagement.service;

import com.example.TTS_LibraryManagement.dto.request.Post.LikeOrDislikeRequest;
import com.example.TTS_LibraryManagement.dto.request.Post.PostCreationRequest;
import com.example.TTS_LibraryManagement.dto.request.Post.PostUpdateRequest;
import com.example.TTS_LibraryManagement.dto.response.Dashboard.DashboardPostResponse;
import com.example.TTS_LibraryManagement.dto.response.Post.PostLikeResponse;
import com.example.TTS_LibraryManagement.dto.response.Post.PostResponse;

import java.util.List;

public interface PostService {
    List<PostResponse> getPosts();
    PostResponse getPost(Long id);
    PostResponse createPost(PostCreationRequest request);
    PostResponse updatePost(Long id, PostUpdateRequest request);
    void deletePost(Long id);
    void restorePost(Long id);
    PostLikeResponse likeOrDislikePost(Long userId, LikeOrDislikeRequest request);
    List<DashboardPostResponse> getListTopLiked();
}
