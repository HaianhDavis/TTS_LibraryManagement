package com.example.TTS_LibraryManagement.mapper;

import com.example.TTS_LibraryManagement.dto.request.Post.PostCreationRequest;
import com.example.TTS_LibraryManagement.dto.request.Post.PostUpdateRequest;
import com.example.TTS_LibraryManagement.dto.response.Post.*;
import com.example.TTS_LibraryManagement.entity.Book;
import com.example.TTS_LibraryManagement.entity.Post;
import com.example.TTS_LibraryManagement.entity.PostLike;
import com.example.TTS_LibraryManagement.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PostMapper {
    Post toPostCreate(PostCreationRequest request);

    PostResponse toPostResponse(Post Post);

    void toPostUpdate(@MappingTarget Post post, PostUpdateRequest request);

    PostUpdateRequest toPostUpdateRequest(Post post);

    PostUserResponse toPostUserResponse(User user);
    PostBookResponse toPostBookResponse(Book book);

    @Mapping(target = "post", ignore = true)
    @Mapping(target = "user", ignore = true)
    PostLikeResponse toPostLikeResponse(PostLike postLike);

    PLUserResponse toPLUserResponse(User user);
    PLPostResponse toPLPostResponse(Post post);
}
