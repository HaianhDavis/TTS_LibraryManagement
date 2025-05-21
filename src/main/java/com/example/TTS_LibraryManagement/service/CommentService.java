package com.example.TTS_LibraryManagement.service;

import com.example.TTS_LibraryManagement.dto.request.Comment.CommentCreationRequest;
import com.example.TTS_LibraryManagement.dto.request.Comment.CommentUpdateRequest;
import com.example.TTS_LibraryManagement.dto.request.Post.PostCreationRequest;
import com.example.TTS_LibraryManagement.dto.request.Post.PostUpdateRequest;
import com.example.TTS_LibraryManagement.dto.response.Comment.CommentResponse;
import com.example.TTS_LibraryManagement.dto.response.Post.PostResponse;

import java.util.List;

public interface CommentService {
    List<CommentResponse> getCommentsByPostId();

    CommentResponse getCommentById(Long id);

    CommentResponse createComment(Long userId, CommentCreationRequest request);

    CommentResponse updateComment(Long id, CommentUpdateRequest request);

    void deleteComment(Long id);

    void restorePost(Long id);
}
