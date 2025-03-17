package com.nubo.masterparticipant.api.service.impl;

import com.nubo.masterparticipant.api.application.context.GlobalContext;
import com.nubo.masterparticipant.api.exception.ServiceException;
import com.nubo.masterparticipant.api.exception.error.ErrorCode;
import com.nubo.masterparticipant.api.models.dto.response.DepositoryOutDto;
import com.nubo.masterparticipant.api.models.entity.DepositoryEntity;
import com.nubo.masterparticipant.api.models.enums.PropertiesEnum;
import com.nubo.masterparticipant.api.models.enums.VersionApiEnum;
import com.nubo.masterparticipant.api.repository.DepositoryRepository;
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
class DepositoryServiceImplTest {

    @Mock
    private DepositoryRepository depositoryRepository;

    @InjectMocks
    private DepositoryServiceImpl depositoryServiceImpl;

    private void setVersion(VersionApiEnum versionApiEnum) {
        GlobalContext.setProperty(PropertiesEnum.VERSION_API.getValue(), versionApiEnum.getValue());
    }

    @Test
    @DisplayName("Find all depository - V1")
    void findAllDepositoryV1Test() {
        this.setVersion(VersionApiEnum.V1);

        DepositoryEntity depositoryEntity = createDepositoryEntityTestData();
        when(depositoryRepository.findAll()).thenReturn(Collections.singletonList(depositoryEntity));

        List<DepositoryOutDto> result = depositoryServiceImpl.findAllDepository();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(depositoryEntity.getId(), result.get(0).getId());
        assertEquals(depositoryEntity.getDescription(), result.get(0).getDescription());
        verify(depositoryRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Find all depository exception - Version Api Not Found")
    void findAllDepositoryExceptionVersionNotImplementedTest() {
        this.setVersion(VersionApiEnum.VERSION_NOT_IMPLEMENTED);

        ServiceException serviceException = assertThrows(ServiceException.class, () ->
                depositoryServiceImpl.findAllDepository());

        assertEquals(ErrorCode.VERSION_API_NOT_FOUND.name(), serviceException.getErrorCode().getName());
    }

    private DepositoryEntity createDepositoryEntityTestData() {
        String anyString = "any string";
        return DepositoryEntity.builder()
                .id("ab")
                .description(anyString)
                .build();
    }

}
