package com.zerobase.munbanggu.studyboard.repository;

import com.zerobase.munbanggu.studyboard.model.entity.StudyBoardPost;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StudyBoardPostRepository extends JpaRepository<StudyBoardPost, Long> {

//    Page<StudyBoardPost> findByTitle(String title, Pageable pageable);
@Query("SELECT DISTINCT p FROM StudyBoardPost p LEFT JOIN p.vote v WHERE p.title LIKE %:keyword% OR v.title LIKE %:keyword%")
Page<StudyBoardPost> findByTitleOrVoteTitleContaining(@Param("keyword") String keyword, Pageable pageable);
}
