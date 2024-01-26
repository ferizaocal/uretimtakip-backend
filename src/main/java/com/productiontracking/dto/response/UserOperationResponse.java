package com.productiontracking.dto.response;

import java.util.Date;

import com.productiontracking.entity.Operation;

import lombok.Data;

@Data
public class UserOperationResponse {
    private Long id;
    private Long userId;
    private String firstName;
    private String lastName;
    private Operation operation;
    private Boolean isCompleted;
    private Date createdDate;

}
