package com.zerobase.munbanggu.study.service;

import com.zerobase.munbanggu.study.exception.StudyException;
import com.zerobase.munbanggu.study.model.entity.Study;
import com.zerobase.munbanggu.study.repository.StudyRepository;
import com.zerobase.munbanggu.type.ErrorCode;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudyService {

  @Autowired
  private StudyRepository studyRepository;

  public Study getStudy(Long id){
      return studyRepository.findById(id)
          .orElseThrow(() -> new StudyException(ErrorCode.STUDY_NOT_EXISTS));
  }

  /**
   * 사용자가 참여하고있는 스터디의 ID 목록을 조회
   * @param userId 사용자ID
   * @return 참여하고 있는 스터디ID 목록
   */
  public List<Study> findStudiesByUserId(Long userId) {
    return studyRepository.findStudyIdByUserId(userId);
  }
}
