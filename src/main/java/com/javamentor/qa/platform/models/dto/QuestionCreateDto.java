package com.javamentor.qa.platform.models.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Schema
public class QuestionCreateDto {

    @Schema(description = "заголовок создаваемого вопроса")
    @NotNull
    private String title;

    @Schema(description = "Описание создаваемого вопроса")
    @NotNull
    private String description;

    @Schema(description = "Тэги создаваемого вопроса")
    @NotNull
    private List<TagDto> tags;

}
