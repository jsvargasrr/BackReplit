package com.nubo.masterparticipant.api.exception.error;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorValidation {

    private String id;
    private String description;

}
