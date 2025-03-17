package com.nubo.masterparticipant.api.helpers;

import com.nubo.masterparticipant.api.models.dto.response.PageResponseOutDto;
import org.springframework.data.domain.Page;

import java.util.List;

public class MapperPageUtil {

    private MapperPageUtil() {
    }

    public static <T, U> PageResponseOutDto<T> pageToPageResponseOutDto(List<T> list, Page<U> page) {
        PageResponseOutDto<T> paginationResponseDto = new PageResponseOutDto<>();
        paginationResponseDto.setContent(list);
        paginationResponseDto.setPageSize(page.getSize());
        paginationResponseDto.setPageNumber(page.getNumber());
        paginationResponseDto.setTotalElements(page.getTotalElements());
        paginationResponseDto.setTotalPages(page.getTotalPages());

        return paginationResponseDto;
    }
}
