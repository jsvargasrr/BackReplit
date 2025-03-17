package com.nubo.masterparticipant.api.service.impl;

import com.nubo.masterparticipant.api.application.context.GlobalContext;
import com.nubo.masterparticipant.api.exception.ServiceException;
import com.nubo.masterparticipant.api.exception.error.ErrorCode;
import com.nubo.masterparticipant.api.models.dto.response.StatusOutDto;
import com.nubo.masterparticipant.api.models.entity.StatusEntity;
import com.nubo.masterparticipant.api.models.enums.PropertiesEnum;
import com.nubo.masterparticipant.api.models.enums.VersionApiEnum;
import com.nubo.masterparticipant.api.repository.StatusRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StatusServiceImplTest {

    @Mock
    private StatusRepository statusRepository;

    @InjectMocks
    private StatusServiceImpl statusServiceImpl;

    private void setVersion(VersionApiEnum versionApiEnum) {
        GlobalContext.setProperty(PropertiesEnum.VERSION_API.getValue(), versionApiEnum.getValue());
    }

    @Test
    @DisplayName("Find all status - V1")
    void findAllStatusV1Test() {
        this.setVersion(VersionApiEnum.V1);

        StatusEntity statusEntity = createStatusEntityTestData();
        when(statusRepository.findAll()).thenReturn(Collections.singletonList(statusEntity));

        List<StatusOutDto> result = statusServiceImpl.findAllStatus();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(statusEntity.getId(), result.get(0).getId());
        assertEquals(statusEntity.getDescription(), result.get(0).getDescription());
        verify(statusRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Find all status exception - Version Api Not Found")
    void findAllStatusExceptionVersionNotImplementedTest() {
        this.setVersion(VersionApiEnum.VERSION_NOT_IMPLEMENTED);

        ServiceException serviceException = assertThrows(ServiceException.class, () ->
                statusServiceImpl.findAllStatus());

        assertEquals(ErrorCode.VERSION_API_NOT_FOUND.name(), serviceException.getErrorCode().getName());
    }

    private StatusEntity createStatusEntityTestData() {
        String anyString = "any string";
        return StatusEntity.builder()
                .id(1L)
                .description(anyString)
                .build();
    }

}
