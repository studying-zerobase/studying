package com.zerobase.munbanggu.study.service;

import com.zerobase.munbanggu.study.exception.StudyException;
import com.zerobase.munbanggu.study.model.entity.Checklist;
import com.zerobase.munbanggu.study.repository.ChecklistRepository;
import com.zerobase.munbanggu.study.type.AccessType;
import com.zerobase.munbanggu.type.ErrorCode;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ChecklistServiceTest {

  @Autowired
  private ChecklistRepository checklistRepository;
  @Autowired
  private ChecklistService checklistService;

  Long userId = 1L;
  Long checklistId = 1L;
  Long studyId = 1L;

  @Test
  void createChecklistTest() {
    String title = "Test Checklist";
    AccessType ownerType = AccessType.STUDY;

    String result = checklistService.createChecklist(userId, studyId, title, ownerType);

    assert (result != null);
    System.out.println("\n>>> result ::: " + result);
  }

  @Test
  void editChecklist() {

    createChecklistTest();
    String newTitle = "TEST22";

    String result = checklistService.editChecklist(userId,checklistId, newTitle);
    assert (result != null);

    Checklist checklist = checklistRepository.findById(checklistId)
        .orElseThrow(() -> new StudyException(ErrorCode.CHECKLIST_NOT_EXISTS));
    assert(!checklist.getTodo().equals(newTitle));
  }

  @Test
  void deleteChecklist() {
    createChecklistTest();

    System.out.println("\nPREV "+checklistRepository.findById(checklistId).get().getTodo());
    checklistService.deleteChecklist(userId,checklistId);
    System.out.println("\nAFTER "+checklistRepository.findById(checklistId).isEmpty());
  }

  @Test
  void changeStatus() {
    createChecklistTest();

    Checklist checklist = checklistRepository.findById(checklistId).orElseThrow(() -> new StudyException(ErrorCode.CHECKLIST_NOT_EXISTS));
    System.out.println("\n>> PREV : "+ checklist.isDone());
    checklist.setDone(true);
    checklistRepository.save(checklist);
    System.out.println("\n>> AFTER: "+ checklist.isDone());
  }
}