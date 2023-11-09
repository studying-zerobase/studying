package com.zerobase.munbanggu.studyboard.controller;

import com.zerobase.munbanggu.dto.ErrorResponse;
import com.zerobase.munbanggu.studyboard.model.dto.PostRequest;
import com.zerobase.munbanggu.studyboard.service.StudyBoardService;
import com.zerobase.munbanggu.type.ErrorCode;
import java.util.HashMap;
import java.util.Map;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/study")
@RequiredArgsConstructor
public class StudyBoardController {

    private final StudyBoardService studyBoardService;

    @PostMapping("/{study_id}/post")
    public ResponseEntity<?> create(@PathVariable("study_id") Long id,
            @Valid @RequestBody PostRequest request, BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errorMap = buildErrorMap(result);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ErrorResponse.of(ErrorCode.INVALID_REQUEST_BODY, errorMap));
        }
        return ResponseEntity.ok().body(studyBoardService.create(request));
    }

    private static Map<String, String> buildErrorMap(BindingResult result) {
        Map<String, String> errorMap = new HashMap<>();
        for (FieldError fieldError : result.getFieldErrors()) {
            String fieldName = fieldError.getField();
            String[] fieldPath = fieldName.split("\\.");
            String simpleFieldName = fieldPath.length > 1 ? fieldPath[1] : fieldPath[0];
            errorMap.put(simpleFieldName, fieldError.getDefaultMessage());
        }
        return errorMap;
    }

}