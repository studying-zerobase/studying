package com.zerobase.munbanggu.study.controller;

import com.zerobase.munbanggu.study.model.entity.Study;
import com.zerobase.munbanggu.study.service.ChecklistService;
import com.zerobase.munbanggu.study.service.StudyService;
import com.zerobase.munbanggu.study.type.AccessType;
import com.zerobase.munbanggu.type.ErrorCode;
import com.zerobase.munbanggu.user.exception.UserException;
import com.zerobase.munbanggu.user.model.entity.User;
import com.zerobase.munbanggu.user.repository.UserRepository;
import com.zerobase.munbanggu.user.service.UserService;
import com.zerobase.munbanggu.util.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/study")
public class StudyController {
    private static final String AUTH_HEADER = "Authorization";
    private final UserService userService;
    private final StudyService studyService;
    private final JwtService jwtService;
    private final ChecklistService checkListService;
    private final UserRepository userRepository;

    public User verifyUserNStudy(String token, Long studyId){
        User user = userService.getUser(jwtService.getIdFromToken(token))
            .orElseThrow(() -> new UserException(ErrorCode.INVALID_TOKEN));

        Study study = studyService.getStudy(studyId);
        if (user != null && study!= null)
            return user;
        else
            return null;
    }
    public User verifyUserToken(String token, Long userId, Long studyId){
        User userFromToken = verifyUserNStudy(token, studyId);

        if( userFromToken != null )
            return userService.findByIdAndToken(userFromToken.getId(), userId);
        return null;
    }

    /**
     * 스터디 체크리스트 추가하기
     * @param studyId
     * @param title
     * @param token
     * @return 성공/실패여부
     */
    @PostMapping("/{study_id}/mission")
    public ResponseEntity<String> createList(
        @PathVariable("study_id") Long studyId,
        @RequestParam String title,
        @RequestHeader(name = AUTH_HEADER) String token) {

        User user = verifyUserNStudy(token,studyId);

        if (user != null)
            return ResponseEntity.ok(
                checkListService.createChecklist(user.getId(), studyId, title, AccessType.STUDY));
        return ResponseEntity.ok("Adding study list Failed");
        }

    /**
     * 개인 체크리스트 추가하기
     * @param userId
     * @param studyId
     * @param title
     * @param token
     * @return 성공/실패여부
     */
    @PostMapping("/user/{user_id}/study/{study_id}/mission")
    public ResponseEntity<String> addPrivateList(
        @PathVariable("user_id") Long userId,
        @PathVariable("study_id") Long studyId,
        @RequestParam String title,
        @RequestHeader("Authorization") String token) {

        if ( verifyUserToken(token, userId, studyId) != null)
            return ResponseEntity.ok(
                checkListService.createChecklist(userId, studyId, title, AccessType.USER));
        return ResponseEntity.ok("Adding private list Failed");
        }

    /**
     * 체크리스트 수정하기
     * @param studyId
     * @param checklistId
     * @param title - 체크리스트 내용
     * @param token
     * @return 성공/실패여부
     */
    @PatchMapping("/{study_id}/mission/{mission_id}")
    public ResponseEntity<String> editList(@PathVariable("study_id") Long studyId,
        @PathVariable("mission_id") Long checklistId,
        @RequestParam String title,
        @RequestHeader("Authorization") String token) {

        User user = verifyUserNStudy(token,studyId);

        if(user != null)
            return ResponseEntity.ok(checkListService.editChecklist(user.getId(), checklistId, title));
        return ResponseEntity.ok("Edit Failed");
    }

    /**
     * 체크리스트 삭제 기능
     * @param studyId
     * @param checklistId
     * @param token
     * @return
     */
    @DeleteMapping("/{study_id}/mission/{mission_id}")
    public ResponseEntity<String> deleteList(
        @PathVariable("study_id") Long studyId,
        @PathVariable("mission_id") Long checklistId,
        @RequestHeader("Authorization") String token) {

        User user = verifyUserNStudy(token,studyId);

        if(user != null)
            return ResponseEntity.ok(checkListService.deleteChecklist(user.getId(), checklistId));
        return ResponseEntity.ok("Delete Failed");
    }

    /**
     * 체크리스트 상태 변경 (할일 완료 여부 체크)
     * @param studyId
     * @param userId
     * @param checklistId
     * @param status
     * @param token
     * @return
     */
    @PatchMapping("/{study_id}/user/{user_id}/mission/{mission_id}")
    public ResponseEntity<String> changeStatus(
        @PathVariable("study_id") Long studyId,
        @PathVariable("user_id") Long userId,
        @PathVariable("mission_id") Long checklistId,
        @RequestParam boolean status,
        @RequestHeader("Authorization") String token) {

        /*  if (token.getId() == (@PathVariable) userId == checklist.getUserId ) -> 상태변경
            else -> 하나라도 틀리면 변경 x */
        if ( verifyUserToken(token, userId, studyId) != null)
            return ResponseEntity.ok(checkListService.changeStatus(userId, checklistId,status));
        return ResponseEntity.ok("Changing status Failed");
    }
}
