package com.zerobase.munbanggu.studyboard.model.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@Setter
public class VoteRequest {

    private String title;

    private List<VoteOptionRequest> options;

//    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endDate;

    @JsonCreator
    public VoteRequest(@JsonProperty("title") String title,
            @JsonProperty("options") List<VoteOptionRequest> options,
            @JsonProperty("endDate") LocalDateTime endDate) {

        this.title = title;
        this.options = options;
        this.endDate = endDate;

    }


}
