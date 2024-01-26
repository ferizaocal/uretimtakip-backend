package com.productiontracking.service;

import com.productiontracking.dto.response.ServiceResponse;
import com.productiontracking.entity.AgeGroup;

public interface AgeGroupService {
    ServiceResponse<AgeGroup> create(String Name);

    ServiceResponse<AgeGroup> update(Long id, String Name);

    ServiceResponse<AgeGroup> delete(Long id);

    ServiceResponse<AgeGroup> getAll();
}
