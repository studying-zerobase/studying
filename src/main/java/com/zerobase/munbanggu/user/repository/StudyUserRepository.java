package com.zerobase.munbanggu.user.repository;

import com.zerobase.munbanggu.study.model.entity.Study;
import com.zerobase.munbanggu.user.model.entity.StudyUser;
import com.zerobase.munbanggu.user.model.entity.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudyUserRepository extends JpaRepository<StudyUser,Long> {
  List<StudyUser> findByUser(User user);
}
