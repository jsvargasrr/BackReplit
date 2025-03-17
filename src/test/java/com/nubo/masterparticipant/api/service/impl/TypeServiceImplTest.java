package com.nubo.masterparticipant.api.service.impl;

import com.nubo.masterparticipant.api.application.context.GlobalContext;
import com.nubo.masterparticipant.api.exception.ServiceException;
import com.nubo.masterparticipant.api.exception.error.ErrorCode;
import com.nubo.masterparticipant.api.models.dto.response.TypeOutDto;
import com.nubo.masterparticipant.api.models.entity.TypeEntity;
import com.nubo.masterparticipant.api.models.enums.PropertiesEnum;
import com.nubo.masterparticipant.api.models.enums.VersionApiEnum;
import com.nubo.masterparticipant.api.repository.TypeRepository;
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
class TypeServiceImplTest {

    @Mock
    private TypeRepository typeRepository;

    @InjectMocks
    private TypeServiceImpl typeServiceImpl;

    private void setVersion(VersionApiEnum versionApiEnum) {
        GlobalContext.setProperty(PropertiesEnum.VERSION_API.getValue(), versionApiEnum.getValue());
    }

    @Test
    @DisplayName("Find all type - V1")
    void findAllTypeV1Test() {
        this.setVersion(VersionApiEnum.V1);

        TypeEntity typeEntity = createTypeEntityTestData();
        when(typeRepository.findAll()).thenReturn(Collections.singletonList(typeEntity));

        List<TypeOutDto> result = typeServiceImpl.findAllType();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(typeEntity.getId(), result.get(0).getId());
        assertEquals(typeEntity.getDescription(), result.get(0).getDescription());
        verify(typeRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Find all type exception - Version Api Not Found")
    void findAllTypeExceptionVersionNotImplementedTest() {
        this.setVersion(VersionApiEnum.VERSION_NOT_IMPLEMENTED);

        ServiceException serviceException = assertThrows(ServiceException.class, () ->
                typeServiceImpl.findAllType());

        assertEquals(ErrorCode.VERSION_API_NOT_FOUND.name(), serviceException.getErrorCode().getName());
    }

    private TypeEntity createTypeEntityTestData() {
        String anyString = "any string";
        return TypeEntity.builder()
                .id(1L)
                .description(anyString)
                .build();
    }

}
