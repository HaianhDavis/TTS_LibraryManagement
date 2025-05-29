package com.example.TTS_LibraryManagement.controller;

import com.example.TTS_LibraryManagement.dto.request.Comment.CommentCreationRequest;
import com.example.TTS_LibraryManagement.dto.request.Comment.CommentUpdateRequest;
import com.example.TTS_LibraryManagement.dto.response.ApiResponse;
import com.example.TTS_LibraryManagement.dto.response.Comment.CommentResponse;
import com.example.TTS_LibraryManagement.service.CommentService;
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
@RequestMapping("/comment")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Comment", description = "Comment Management APIs")
public class CommentController {
    CommentService commentService;

    @Operation(summary = "Create a new comment")
    @PostMapping("/create/{userId}")
    @PreAuthorize("fileRole(#httpServletRequest)")
    ApiResponse<CommentResponse> createComment(HttpServletRequest httpServletRequest, @PathVariable Long userId, @RequestBody CommentCreationRequest request) {
        ApiResponse<CommentResponse> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Successfully created comment!");
        apiResponse.setResult(commentService.createComment(userId, request));
        return apiResponse;
    }

    @Operation(summary = "Get all comments")
    @GetMapping
    @PreAuthorize("fileRole(#httpServletRequest)")
    ApiResponse<List<CommentResponse>> getComments(HttpServletRequest httpServletRequest) {
        ApiResponse<List<CommentResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Successfully retrieved comments!");
        apiResponse.setResult(commentService.getCommentsByPostId());
        return apiResponse;
    }

    @Operation(summary = "Get comment by ID")
    @GetMapping("/detail/{id}")
    @PreAuthorize("fileRole(#httpServletRequest)")
    ApiResponse<CommentResponse> getComment(HttpServletRequest httpServletRequest, @PathVariable Long id) {
        ApiResponse<CommentResponse> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Successfully retrieved comment!");
        apiResponse.setResult(commentService.getCommentById(id));
        return apiResponse;
    }

    @Operation(summary = "Update a comment")
    @PutMapping("/update/{id}")
    @PreAuthorize("fileRole(#httpServletRequest)")
    ApiResponse<CommentResponse> updateComment(HttpServletRequest httpServletRequest, @PathVariable Long id, @RequestBody CommentUpdateRequest request) {
        ApiResponse<CommentResponse> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Successfully updated comment!");
        apiResponse.setResult(commentService.updateComment(id, request));
        return apiResponse;
    }

    @Operation(summary = "Delete a comment")
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("fileRole(#httpServletRequest)")
    ApiResponse<CommentResponse> deleteComment(HttpServletRequest httpServletRequest, @PathVariable Long id) {
        ApiResponse<CommentResponse> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Successfully deleted comment!");
        commentService.deleteComment(id);
        return apiResponse;
    }

    @Operation(summary = "Restore a comment")
    @PatchMapping("/restore/{id}")
    @PreAuthorize("fileRole(#httpServletRequest)")
    ApiResponse<CommentResponse> restoreComment(HttpServletRequest httpServletRequest, @PathVariable Long id) {
        ApiResponse<CommentResponse> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Successfully restored comment!");
        commentService.restorePost(id);
        return apiResponse;
    }
}
