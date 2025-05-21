package com.example.TTS_LibraryManagement.repository;

import com.example.TTS_LibraryManagement.entity.Book;
import com.example.TTS_LibraryManagement.entity.BorrowReturnBook;
import com.example.TTS_LibraryManagement.entity.Post;
import com.example.TTS_LibraryManagement.entity.PostLike;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepo extends JpaRepository<Post, Long> {
    @Query(value = "select p from User u join Post p on p.user.id = u.id join Book b on b.id = p.book.id where b.isDeleted = 0 and u.isDeleted = 0 and p.isDeleted = 0")
    List<Post> findAllIsDeletedFalse();

    @Query(value = "select p from User u join Post p on u.id = p.user.id join Book b on b.id = p.book.id where p.id = ?1 and b.isDeleted = 0 and u.isDeleted = 0 and p.isDeleted = 0")
    Optional<Post> findByPostId(Long id);

    @Query(value = "select p from User u join Comment c on u.id = c.user.id join Post p on p.id = c.post.id where c.id = ?1 and u.isDeleted = 0 and c.isDeleted = 0 and p.isDeleted = 0")
    Optional<Post> findPostByCommentIdAndIsDeletedFalse(Long id);

    @Query(value = "select p from Post p where p.id = ?1 and p.isDeleted = 0")
    Optional<Post> findPostByIdAndIsDeletedFalse(Long id);

    @Query("select count(pl) from PostLike pl where pl.post.id = ?1 and pl.isLike = true and pl.isDeleted = 0")
    int findTotalLikesByPostId(Long postId);

   @Query("select p.id, p.title, count(pl) " +
          "from Post p left join PostLike pl on pl.post.id = p.id and pl.isLike = true and pl.isDeleted = 0 " +
          "where p.isDeleted = 0 " +
          "group by p.id, p.title " +
          "order by count(pl) desc")
   List<Object[]> getListTopLiked(Pageable pageable);
}
