package com.productiontracking.dto.response;

import java.util.Date;

import lombok.Data;

@Data
public class BaseResponse {
    private Long id;
    private Date createdDate;
}
