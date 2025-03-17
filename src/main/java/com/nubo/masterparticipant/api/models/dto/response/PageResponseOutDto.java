package com.nubo.masterparticipant.api.models.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.ALWAYS)
public class PageResponseOutDto<T> {

    private List<T> content;

    private Integer pageSize;

    private Integer pageNumber;

    private Long totalElements;

    private Integer totalPages;
}
