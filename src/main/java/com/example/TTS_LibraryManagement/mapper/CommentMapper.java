package com.example.TTS_LibraryManagement.mapper;

import com.example.TTS_LibraryManagement.dto.request.Comment.CommentCreationRequest;
import com.example.TTS_LibraryManagement.dto.request.Comment.CommentUpdateRequest;
import com.example.TTS_LibraryManagement.dto.response.Comment.CommentPostResponse;
import com.example.TTS_LibraryManagement.dto.response.Comment.CommentResponse;
import com.example.TTS_LibraryManagement.dto.response.Comment.CommentUserResponse;
import com.example.TTS_LibraryManagement.entity.Book;
import com.example.TTS_LibraryManagement.entity.Comment;
import com.example.TTS_LibraryManagement.entity.Post;
import com.example.TTS_LibraryManagement.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    Comment toCommentCreate(CommentCreationRequest request);

    CommentResponse toCommentResponse(Comment comment);

    void toCommentUpdate(@MappingTarget Comment comment, CommentUpdateRequest request);

    CommentUpdateRequest toCommentUpdateRequest(Comment Comment);

    CommentUserResponse toCommentUserResponse(User user);
    CommentPostResponse toCommentPostResponse(Post post);
}
