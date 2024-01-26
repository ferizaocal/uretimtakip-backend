package com.productiontracking.dto.request;

import lombok.Data;

@Data
public class UpdateUserRequest {
    private Long id;
    private String firstName;
    private String lastName;

}
