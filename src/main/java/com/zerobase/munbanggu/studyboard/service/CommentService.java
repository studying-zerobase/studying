package com.zerobase.munbanggu.studyboard.service;

import com.zerobase.munbanggu.config.auth.TokenProvider;
import com.zerobase.munbanggu.studyboard.exception.NotFoundPostException;
import com.zerobase.munbanggu.studyboard.model.dto.CommentRequest;
import com.zerobase.munbanggu.studyboard.model.entity.Comment;
import com.zerobase.munbanggu.studyboard.model.entity.StudyBoardPost;
import com.zerobase.munbanggu.studyboard.repository.CommentRepository;
import com.zerobase.munbanggu.studyboard.repository.StudyBoardPostRepository;
import com.zerobase.munbanggu.type.ErrorCode;
import com.zerobase.munbanggu.user.exception.NotFoundUserException;
import com.zerobase.munbanggu.user.model.entity.User;
import com.zerobase.munbanggu.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final TokenProvider tokenProvider;
    private final UserRepository userRepository;
    private final StudyBoardPostRepository studyBoardPostRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public void create(Long postId, CommentRequest commentRequest, String token) {
        User user = findUser(tokenProvider.getId(token));
        StudyBoardPost post = findPost(postId);

        Comment comment = Comment.builder()
                .content(commentRequest.getContent())
                .user(user)
                .studyBoardPost(post)
                .build();

        Comment parentComment;
        if (commentRequest.getParentId() != null) {
            parentComment = commentRepository.findById(commentRequest.getParentId())
                    .orElseThrow(() -> new NotFoundPostException(ErrorCode.NOT_FOUND_COMMENT));

            comment.updateParent(parentComment);
        }
        commentRepository.save(comment);
    }

    private User findUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundUserException(ErrorCode.NOT_FOUND_USER_ID));
    }

    private StudyBoardPost findPost(Long postId) {
        return studyBoardPostRepository.findById(postId)
                .orElseThrow(() -> new NotFoundPostException(ErrorCode.NOT_FOUND_POST));
    }

}
