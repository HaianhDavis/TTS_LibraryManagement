package com.example.TTS_LibraryManagement.repository;

import com.example.TTS_LibraryManagement.entity.Comment;
import com.example.TTS_LibraryManagement.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepo extends JpaRepository<Comment, Long> {
    @Query(value = "select c from User u join Comment c on c.user.id = u.id join Post p on p.id = c.post.id where p.isDeleted = 0 and u.isDeleted = 0 and c.isDeleted = 0")
    List<Comment> findAllIsDeletedFalse();

    @Query(value = "select c from User u join Comment c on c.user.id = u.id join Post p on p.id = c.post.id where c.id = ?1 and p.isDeleted = 0 and u.isDeleted = 0 and c.isDeleted = 0")
    Optional<Comment> findByCommentId(Long id);
}
