package com.zerobase.munbanggu.study.service;

import com.zerobase.munbanggu.study.exception.StudyException;
import com.zerobase.munbanggu.study.model.entity.Checklist;
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
}
