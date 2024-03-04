package com.javamentor.qa.platform.models.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Schema(description = "TopTags")
public class RelatedTagDto {

    @Schema(description = "id ")
    private Long id;


    @Schema(description = "Название тега")
    private String title;

    @Schema(description = "Количество вопросов с этим тегом")
    private Long countQuestion;

}
