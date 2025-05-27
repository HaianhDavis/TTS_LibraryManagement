package com.example.TTS_LibraryManagement.repository;

import com.example.TTS_LibraryManagement.entity.InvalidatedToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvalidatedTokenRepo extends JpaRepository<InvalidatedToken, String> {

}
