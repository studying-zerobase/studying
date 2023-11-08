package com.zerobase.munbanggu.studyboard.service;

import com.zerobase.munbanggu.studyboard.model.dto.StudyBoardPostRequest;
import com.zerobase.munbanggu.studyboard.model.dto.StudyBoardPostResponse;
import com.zerobase.munbanggu.studyboard.model.dto.VoteOptionRequest;
import com.zerobase.munbanggu.studyboard.model.entity.StudyBoardPost;
import com.zerobase.munbanggu.studyboard.model.entity.Vote;
import com.zerobase.munbanggu.studyboard.model.entity.VoteOption;
import com.zerobase.munbanggu.studyboard.repository.StudyBoardPostRepository;
import com.zerobase.munbanggu.studyboard.repository.VoteRepository;
import com.zerobase.munbanggu.studyboard.type.Type;
import com.zerobase.munbanggu.user.exception.ErrorCode;
import com.zerobase.munbanggu.user.exception.NotFoundUserException;
import com.zerobase.munbanggu.user.model.entity.User;
import com.zerobase.munbanggu.user.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StudyBoardPostService {

    private final StudyBoardPostRepository studyBoardPostRepository;
    private final VoteRepository voteRepository;
    private final UserRepository userRepository;

    @Transactional
    public StudyBoardPostResponse createStudyBoardPost(StudyBoardPostRequest request) {
        StudyBoardPost post = buildStudyBoardPost(request);
        if (request.getType() == Type.VOTE) {
            Vote vote = buildVote(request);
            vote = voteRepository.save(vote);
            post.setVote(vote);
        }
        return StudyBoardPostResponse.from(studyBoardPostRepository.save(post));
    }

    private User findUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundUserException(ErrorCode.NOT_FOUND_USER_ID));
    }

    private StudyBoardPost buildStudyBoardPost(StudyBoardPostRequest request) {
        User user = findUser(request.getUserId());

        return StudyBoardPost.builder()
                .type(request.getType())
                .title(request.getTitle())
                .content(request.getContent())
                .user(user)
                .build();
    }

    private Vote buildVote(StudyBoardPostRequest request) {
        Vote vote = Vote.builder()
                .title(request.getVote().getTitle())
                .endDate(request.getVote().getEndDate())
                .build();
        List<VoteOptionRequest> optionRequests = request.getVote().getOptions();
        optionRequests.forEach(optionRequest -> {
            VoteOption option = VoteOption.builder()
                    .optionText(optionRequest.getOptionText())
                    .build();
            vote.addVoteOption(option);
        });
        return vote;
    }

}
