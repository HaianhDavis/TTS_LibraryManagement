package com.example.TTS_LibraryManagement.service.impl;

import com.example.TTS_LibraryManagement.dto.request.Comment.CommentCreationRequest;
import com.example.TTS_LibraryManagement.dto.request.Comment.CommentUpdateRequest;
import com.example.TTS_LibraryManagement.dto.response.Comment.CommentResponse;
import com.example.TTS_LibraryManagement.entity.Comment;
import com.example.TTS_LibraryManagement.entity.Post;
import com.example.TTS_LibraryManagement.entity.User;
import com.example.TTS_LibraryManagement.exception.AppException;
import com.example.TTS_LibraryManagement.enums.ErrorCode;
import com.example.TTS_LibraryManagement.mapper.CommentMapper;
import com.example.TTS_LibraryManagement.repository.CommentRepo;
import com.example.TTS_LibraryManagement.repository.PostRepo;
import com.example.TTS_LibraryManagement.repository.UserRepo;
import com.example.TTS_LibraryManagement.service.CommentService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CommentServiceImpl implements CommentService {
    CommentRepo commentRepo;

    CommentMapper commentMapper;

    UserRepo userRepo;

    PostRepo postRepo;

    @Transactional
    public List<CommentResponse> getCommentsByPostId() {
        List<Comment> comments = commentRepo.findAllIsDeletedFalse();
        if (comments.isEmpty()) {
            throw new AppException(ErrorCode.POST_NOT_FOUND);
        }
        return comments.stream().map(comment -> {
            CommentResponse response = commentMapper.toCommentResponse(comment);
            User user = userRepo.findUserByCommentIdAndIsDeletedFalse(comment.getId()).orElseThrow();
            Post post = postRepo.findPostByCommentIdAndIsDeletedFalse(comment.getId()).orElseThrow();
            response.setUser(commentMapper.toCommentUserResponse(user));
            response.setPost(commentMapper.toCommentPostResponse(post));
            return response;
        }).collect(Collectors.toList());
    }

    @Transactional
    public CommentResponse getCommentById(Long id) {
        Comment comment = commentRepo.findByCommentId(id)
                .orElseThrow(() -> new AppException(ErrorCode.COMMENT_NOT_FOUND));
        CommentResponse commentResponse = commentMapper.toCommentResponse(comment);
        commentResponse.setUser(commentMapper.toCommentUserResponse(userRepo.findUserByCommentIdAndIsDeletedFalse(comment.getId()).orElseThrow()));
        commentResponse.setPost(commentMapper.toCommentPostResponse(postRepo.findPostByCommentIdAndIsDeletedFalse(comment.getId()).orElseThrow()));
        return commentResponse;
    }

    @Transactional
    public CommentResponse createComment(Long userId, CommentCreationRequest request) {
        User user = userRepo.findUserByIdAndIsDeletedFalse(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        Post post = postRepo.findPostByIdAndIsDeletedFalse(request.getPostId()).orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND));
        Comment comment = commentMapper.toCommentCreate(request);
        comment.setUser(user);
        comment.setPost(post);
        comment.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
        commentRepo.save(comment);
        CommentResponse response = commentMapper.toCommentResponse(comment);
        response.setUser(commentMapper.toCommentUserResponse(user));
        response.setPost(commentMapper.toCommentPostResponse(post));
        return response;
    }

    @Transactional
    public CommentResponse updateComment(Long id, CommentUpdateRequest request) {
        Comment comment = commentRepo.findByCommentId(id)
                .orElseThrow(() -> new AppException(ErrorCode.COMMENT_NOT_FOUND));
        Post newPost = postRepo.findPostByIdAndIsDeletedFalse(request.getPostId()).orElseThrow(() -> new AppException(ErrorCode.BOOK_NOT_FOUND));
        if(request.getContent().equals(comment.getContent()) && comment.getPost().getId().equals(newPost.getId())) {
            throw new AppException(ErrorCode.COMMENT_NOT_CHANGED);
        }
        commentMapper.toCommentUpdate(comment, request);
        comment.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
        commentRepo.save(comment);
        CommentResponse response = commentMapper.toCommentResponse(comment);
        response.setUser(commentMapper.toCommentUserResponse(userRepo.findUserByPostIdAndIsDeletedFalse(id).orElseThrow()));
        response.setPost(commentMapper.toCommentPostResponse(newPost));
        return response;
    }

    public void deleteComment(Long id) {
        Comment comment = commentRepo.findByCommentId(id)
                .orElseThrow(() -> new AppException(ErrorCode.COMMENT_NOT_FOUND));
        comment.setIsDeleted(1);
        comment.setDeletedAt(Timestamp.valueOf(LocalDateTime.now()));
        commentRepo.save(comment);
    }

    public void restorePost(Long id) {
        Comment comment = commentRepo.findById(id).orElseThrow(() -> new AppException(ErrorCode.COMMENT_NOT_FOUND));
        if (comment.getIsDeleted() == 0) {
            throw new AppException(ErrorCode.COMMENT_NOT_DELETED);
        }
        comment.setIsDeleted(0);
        comment.setDeletedAt(null);
        comment.setDeletedBy(null);
        commentRepo.save(comment);
    }

}
