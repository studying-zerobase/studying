package com.zerobase.munbanggu.studyboard.service;

import com.zerobase.munbanggu.studyboard.model.dto.PostRequest;
import com.zerobase.munbanggu.studyboard.model.dto.PostResponse;
import com.zerobase.munbanggu.studyboard.model.dto.VoteOptionRequest;
import com.zerobase.munbanggu.studyboard.model.dto.VoteRequest;
import com.zerobase.munbanggu.studyboard.repository.StudyBoardPostRepository;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.jdbc.SqlGroup;

@SpringBootTest
@SqlGroup({
        @Sql(value = "/sql/service-test-data.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD),
})
class StudyBoardServiceTest {

    @Autowired
    private StudyBoardService studyBoardService;

    @Autowired
    private StudyBoardPostRepository studyBoardPostRepository;

    @Test
    void testStudyBoardPostVote() {

        // given
        List<VoteOptionRequest> optionRequests = new ArrayList<>();
        VoteOptionRequest optionRequest = new VoteOptionRequest();
        optionRequest.setOptionText("option1");
        optionRequests.add(optionRequest);
        optionRequest = new VoteOptionRequest();
        optionRequest.setOptionText("option2");
        optionRequests.add(optionRequest);

        String dateTimeAsString = "2023-11-11T11:00:00";
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
        LocalDateTime localDateTime = java.time.LocalDateTime.parse(dateTimeAsString, formatter);

        VoteRequest voteRequest = new VoteRequest("투표 제목", optionRequests, localDateTime);
        PostRequest request = PostRequest.builder()
                .title("게시글 제목")
                .content("게시글 내용")
                .vote(voteRequest)
                .userId(1L)
                .build();

        // when
        PostResponse response = studyBoardService.create(request);

        // then
        Assertions.assertThat(response.getTitle()).isEqualTo("게시글 제목");

    }


}