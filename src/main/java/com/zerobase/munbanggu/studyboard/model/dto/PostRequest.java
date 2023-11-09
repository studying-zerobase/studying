package com.zerobase.munbanggu.studyboard.model.dto;

import com.zerobase.munbanggu.studyboard.type.Type;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostRequest {

    private Type type;

    private String title;

    private String content;

    private Long userId;

    private VoteRequest vote;

}
