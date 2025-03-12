package com.example.animal_feed.feedback;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring")
public interface FeedbacksMapper {
    FeedbacksMapper INSTANCE = Mappers.getMapper(FeedbacksMapper.class);

    FeedbackDTO feedbacksToFeedbackDTO(Feedbacks feedback);
    Feedbacks feedbackDTOToFeedbacks(FeedbackDTO feedbackDTO);

    FeedbackAddDTO feedbacksToFeedbackAddDTO(Feedbacks feedback);
    Feedbacks feedbackAddDTOToFeedbacks(FeedbackAddDTO feedbackAddDTO);

    default Page<FeedbackDTO> feedbacksToFeedbackDTOPage(Page<Feedbacks> feedbacks) {
        return feedbacks.map(this::feedbacksToFeedbackDTO);
    }
    default Page<Feedbacks> feedbackDTOToFeedbacksPage(Page<FeedbackDTO> feedbackDTO) {
        return feedbackDTO.map(this::feedbackDTOToFeedbacks);
    }
}
