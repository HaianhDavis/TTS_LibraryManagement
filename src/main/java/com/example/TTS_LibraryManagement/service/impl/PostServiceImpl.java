package com.example.TTS_LibraryManagement.service.impl;

import com.example.TTS_LibraryManagement.dto.request.Post.LikeOrDislikeRequest;
import com.example.TTS_LibraryManagement.dto.request.Post.PostCreationRequest;
import com.example.TTS_LibraryManagement.dto.request.Post.PostUpdateRequest;
import com.example.TTS_LibraryManagement.dto.response.Dashboard.DashboardPostResponse;
import com.example.TTS_LibraryManagement.dto.response.Post.PostLikeResponse;
import com.example.TTS_LibraryManagement.dto.response.Post.PostResponse;
import com.example.TTS_LibraryManagement.entity.Book;
import com.example.TTS_LibraryManagement.entity.Post;
import com.example.TTS_LibraryManagement.entity.PostLike;
import com.example.TTS_LibraryManagement.entity.User;
import com.example.TTS_LibraryManagement.exception.AppException;
import com.example.TTS_LibraryManagement.enums.ErrorCode;
import com.example.TTS_LibraryManagement.mapper.PostMapper;
import com.example.TTS_LibraryManagement.repository.BookRepo;
import com.example.TTS_LibraryManagement.repository.PostLikeRepo;
import com.example.TTS_LibraryManagement.repository.PostRepo;
import com.example.TTS_LibraryManagement.repository.UserRepo;
import com.example.TTS_LibraryManagement.service.AuthenticationService;
import com.example.TTS_LibraryManagement.service.PostService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PostServiceImpl implements PostService {
    PostRepo postRepo;

    PostMapper postMapper;

    UserRepo userRepo;

    BookRepo bookRepo;

    PostLikeRepo postLikeRepo;

    AuthenticationService authenticationService;

    @Transactional
    public List<PostResponse> getPosts() {
        List<Post> posts = postRepo.findAllIsDeletedFalse();
        if (posts.isEmpty()) {
            throw new AppException(ErrorCode.POST_NOT_FOUND);
        }
        return posts.stream().map(post -> {
            PostResponse response = postMapper.toPostResponse(post);
            User user = userRepo.findUserByPostIdAndIsDeletedFalse(post.getId()).orElseThrow();
            Book book = bookRepo.findBookByPostIdAndIsDeletedFalse(post.getId()).orElseThrow();
            response.setUser(postMapper.toPostUserResponse(user));
            response.setBook(postMapper.toPostBookResponse(book));
            response.setTotalLikes(postRepo.findTotalLikesByPostId(post.getId()));
            return response;
        }).collect(Collectors.toList());
    }

    @Transactional
    public PostResponse getPost(Long id) {
        Post post = postRepo.findByPostId(id)
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND));
        PostResponse postResponse = postMapper.toPostResponse(post);
        postResponse.setUser(postMapper.toPostUserResponse(userRepo.findUserByPostIdAndIsDeletedFalse(post.getId()).orElseThrow()));
        postResponse.setBook(postMapper.toPostBookResponse(bookRepo.findBookByPostIdAndIsDeletedFalse(post.getId()).orElseThrow()));
        postResponse.setTotalLikes(postRepo.findTotalLikesByPostId(post.getId()));
        return postResponse;
    }

    @Transactional
    public PostResponse createPost(PostCreationRequest request) {
        User user = userRepo.findUserByIdAndIsDeletedFalse(request.getUserId()).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        Book book = bookRepo.findBookByIdAndIsDeletedFalse(request.getBookId()).orElseThrow(() -> new AppException(ErrorCode.BOOK_NOT_FOUND));
        Post post = postMapper.toPostCreate(request);
        post.setUser(user);
        post.setBook(book);
        post.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
        postRepo.save(post);
        PostResponse response = postMapper.toPostResponse(post);
        response.setUser(postMapper.toPostUserResponse(user));
        response.setBook(postMapper.toPostBookResponse(book));
        response.setTotalLikes(postRepo.findTotalLikesByPostId(post.getId()));
        return response;
    }

    @Transactional
    public PostResponse updatePost(Long id, PostUpdateRequest request) {
        if(!postRepo.checkPostExistsByIdAndUser(id, SecurityContextHolder.getContext().getAuthentication().getName())) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }
        Post post = postRepo.findByPostId(id).orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND));
        if (request.getTitle().equals(post.getTitle()) && request.getContent().equals(post.getContent())) {
            throw new AppException(ErrorCode.POST_NOT_CHANGED);
        }
        postMapper.toPostUpdate(post, request);
        post.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
        postRepo.save(post);
        PostResponse response = postMapper.toPostResponse(post);
        response.setUser(postMapper.toPostUserResponse(userRepo.findUserByPostIdAndIsDeletedFalse(id).orElseThrow()));
        response.setBook(postMapper.toPostBookResponse(post.getBook()));
        response.setTotalLikes(postRepo.findTotalLikesByPostId(post.getId()));
        return response;
    }

    public void deletePost(Long id) {
        Post post = postRepo.findByPostId(id).orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND));
        post.setIsDeleted(1);
        post.setDeletedAt(Timestamp.valueOf(LocalDateTime.now()));
        postRepo.save(post);
    }

    public void restorePost(Long id) {
        Post post = postRepo.findById(id).orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND));
        if (post.getIsDeleted() == 0) {
            throw new AppException(ErrorCode.POST_NOT_DELETED);
        }
        post.setIsDeleted(0);
        post.setDeletedAt(null);
        post.setDeletedBy(null);
        postRepo.save(post);
    }

    @Transactional
    public PostLikeResponse likeOrDislikePost(Long userId, LikeOrDislikeRequest request) {
        Post post = postRepo.findByPostId(request.getPostId())
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND));

        User user = userRepo.findUserByIdAndIsDeletedFalse(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        PostLike postLike = postLikeRepo.findByPostIdAndUserId(post.getId(), userId);
        boolean isRequestingLike = request.getLikeOrDislike().equals("like");

        if (postLike != null) {
            boolean currentlyLiked = postLike.isLike();

            if ((isRequestingLike && currentlyLiked) || (!isRequestingLike && !currentlyLiked)) {
                throw new AppException(ErrorCode.POST_LIKE_EXISTED);
            } else {
                postLike.setLike(isRequestingLike);
                postLike.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
            }

        } else {
            postLike = new PostLike();
            postLike.setPost(post);
            postLike.setUser(user);
            postLike.setLike(isRequestingLike);
            postLike.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
        }

        postLike = postLikeRepo.save(postLike);

        PostLikeResponse postLikeResponse = postMapper.toPostLikeResponse(postLike);
        postLikeResponse.setUser(postMapper.toPLUserResponse(user));
        postLikeResponse.setPost(postMapper.toPLPostResponse(post));
        postLikeResponse.setIsLike(request.getLikeOrDislike());
        return postLikeResponse;
    }

    public List<DashboardPostResponse> getListTopLiked() {
        Pageable pageable = PageRequest.of(0, 5);
        List<Object[]> listTop = postRepo.getListTopLiked(pageable);
        if (listTop.isEmpty()) {
            throw new AppException(ErrorCode.POST_NOT_FOUND);
        }
        return listTop.stream().map(objects -> {
            Long postId = (Long) objects[0];
            String title = (String) objects[1];
            int totalLikes = ((Long) objects[2]).intValue();
            DashboardPostResponse response = new DashboardPostResponse();
            response.setPostId(postId);
            response.setPostTitle(title);
            response.setTotalLikes(totalLikes);
            return response;
        }).collect(Collectors.toList());
    }
}
