package com.zerobase.munbanggu.studyboard.controller;

import com.zerobase.munbanggu.studyboard.model.dto.StudyBoardPostRequest;
import com.zerobase.munbanggu.studyboard.model.dto.StudyBoardPostResponse;
import com.zerobase.munbanggu.studyboard.service.StudyBoardPostService;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/study")
@RequiredArgsConstructor
public class StudyBoardPostController {

    private final StudyBoardPostService studyBoardPostService;

    @PostMapping("/{study_id}/post")
    public ResponseEntity<StudyBoardPostResponse> createStudyBoardPost(@PathVariable("study_id") Long id,
            @RequestBody StudyBoardPostRequest request) throws IOException {
        return ResponseEntity.ok().body(studyBoardPostService.createStudyBoardPost(request));
    }

}
