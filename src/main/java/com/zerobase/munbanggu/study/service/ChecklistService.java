package com.zerobase.munbanggu.study.service;

import com.zerobase.munbanggu.study.exception.StudyException;
import com.zerobase.munbanggu.study.model.entity.Checklist;
import com.zerobase.munbanggu.study.repository.ChecklistRepository;
import com.zerobase.munbanggu.study.type.AccessType;
import com.zerobase.munbanggu.type.ErrorCode;
import com.zerobase.munbanggu.user.exception.UserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ChecklistService {

  @Autowired
  ChecklistRepository checklistRepository;

  @Transactional
  public String createChecklist(Long userId, Long studyId, String title, AccessType ownerType){
    Checklist checklist = Checklist.builder()
        .study_id(studyId)
        .user_id(userId)
        .todo(title)
        .done(false)
        .accessType(ownerType)
        .build();

    Checklist savedChecklist = checklistRepository.save(checklist);
    return "Added Successfully\n id: "+savedChecklist.getId()+" title: "+title ;
  }


  public String editChecklist(Long userId, Long checklistId, String title){
    Checklist checklist = checklistRepository.findById(checklistId)
        .orElseThrow(() -> new StudyException(ErrorCode.CHECKLIST_NOT_EXISTS));

    if (checklist != null && userId.equals(checklist.getUser_id())) {
      checklist.setTodo(title);
      checklistRepository.save(checklist);
    }
    else
      throw new UserException(ErrorCode.TOKEN_UNMATCHED);

    return "Updated Successfully\n id: "+checklist.getId()+" title: "+title;
  }
  public String deleteChecklist(Long userId, Long checklistId){
    Checklist checklist = checklistRepository.findById(checklistId)
        .orElseThrow(() -> new StudyException(ErrorCode.CHECKLIST_NOT_EXISTS));

    if (checklist != null && userId.equals(checklist.getUser_id()))
      checklistRepository.deleteById(checklistId);
    else
      throw new UserException(ErrorCode.TOKEN_UNMATCHED);

    return "Deleted Successfully\n id: "+checklist.getId();
  }

  public String changeStatus(Long userId, Long checklistId,boolean status){
    Checklist checklist = checklistRepository.findById(checklistId)
        .orElseThrow(() -> new StudyException(ErrorCode.CHECKLIST_NOT_EXISTS));

    boolean prev = checklist.isDone(); // 초기값

    if (checklist != null && userId.equals(checklist.getUser_id())) {
      checklist.setDone(status);
      checklistRepository.save(checklist);
    }else
      throw new UserException(ErrorCode.TOKEN_UNMATCHED);

    return "Status changed Successfully\n"+prev +" -> "+ checklist.isDone();
  }
}
