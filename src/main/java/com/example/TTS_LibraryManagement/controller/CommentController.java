package com.example.TTS_LibraryManagement.controller;

import com.example.TTS_LibraryManagement.dto.request.Comment.CommentCreationRequest;
import com.example.TTS_LibraryManagement.dto.request.Comment.CommentUpdateRequest;
import com.example.TTS_LibraryManagement.dto.response.ApiResponse;
import com.example.TTS_LibraryManagement.dto.response.Comment.CommentResponse;
import com.example.TTS_LibraryManagement.service.CommentService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/library/comment")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CommentController {
    CommentService commentService;

    @PostMapping("/create/{userId}")
    ApiResponse<CommentResponse> createComment(@PathVariable Long userId, @RequestBody CommentCreationRequest request) {
        ApiResponse<CommentResponse> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Successfully created comment!");
        apiResponse.setResult(commentService.createComment(userId, request));
        return apiResponse;
    }

    @GetMapping
    ApiResponse<List<CommentResponse>> getComments() {
        ApiResponse<List<CommentResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Successfully retrieved comments!");
        apiResponse.setResult(commentService.getCommentsByPostId());
        return apiResponse;
    }

    @GetMapping("/detail/{id}")
    ApiResponse<CommentResponse> getComment(@PathVariable Long id) {
        ApiResponse<CommentResponse> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Successfully retrieved comment!");
        apiResponse.setResult(commentService.getCommentById(id));
        return apiResponse;
    }

    @PutMapping("/update/{id}")
    ApiResponse<CommentResponse> updateComment(@PathVariable Long id, @RequestBody CommentUpdateRequest request) {
        ApiResponse<CommentResponse> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Successfully updated comment!");
        apiResponse.setResult(commentService.updateComment(id, request));
        return apiResponse;
    }

    @DeleteMapping("/delete/{id}")
    ApiResponse<CommentResponse> deleteComment(@PathVariable Long id) {
        ApiResponse<CommentResponse> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Successfully deleted comment!");
        commentService.deleteComment(id);
        return apiResponse;
    }

    @PatchMapping("/restore/{id}")
    ApiResponse<CommentResponse> restoreComment(@PathVariable Long id) {
        ApiResponse<CommentResponse> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Successfully restored comment!");
        commentService.restorePost(id);
        return apiResponse;
    }
}
