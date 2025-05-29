package com.example.TTS_LibraryManagement.repository;

import com.example.TTS_LibraryManagement.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.Date;

@Repository
public interface RefreshTokenRepo extends JpaRepository<RefreshToken, String> {
    @Modifying
    @Query("DELETE FROM RefreshToken rt WHERE rt.expiredTime < :timestamp")
    void deleteByExpiredTimeBefore(Timestamp timestamp);
}
