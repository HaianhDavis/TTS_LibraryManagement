package com.example.TTS_LibraryManagement.repository;


import com.example.TTS_LibraryManagement.entity.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PostLikeRepo extends JpaRepository<PostLike, Long> {

    @Query(value = "select pl from PostLike pl where pl.post.id = ?1 and pl.user.id = ?2 and pl.isDeleted = 0")
    PostLike findByPostIdAndUserId(Long postId, Long userId);
}
