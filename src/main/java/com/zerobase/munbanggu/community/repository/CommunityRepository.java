package com.zerobase.munbanggu.community.repository;

import com.zerobase.munbanggu.community.model.entity.Community;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommunityRepository extends JpaRepository<Community, Long> {
    List<Community> findAllByHashtags_Name(String hashtagName);
}