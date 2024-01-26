package com.productiontracking.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.productiontracking.dto.response.ServiceResponse;
import com.productiontracking.entity.AgeGroup;
import com.productiontracking.repository.AgeGroupRepository;

@Service
public class AgeGroupServiceImpl implements AgeGroupService {

    @Autowired
    private AgeGroupRepository ageGroupRepository;

    @Override
    public ServiceResponse<AgeGroup> create(String Name) {
        ServiceResponse<AgeGroup> response = new ServiceResponse<>();
        try {
            AgeGroup ageGroup = new AgeGroup();
            ageGroup.setAge(Name);
            ageGroupRepository.save(ageGroup);
            response.setEntity(ageGroup).setIsSuccessful(true);
        } catch (Exception e) {
            response.setExceptionMessage(e.getMessage());
        }
        return response;
    }

    @Override
    public ServiceResponse<AgeGroup> update(Long id, String Name) {
        ServiceResponse<AgeGroup> response = new ServiceResponse<>();
        try {
            AgeGroup ageGroup = ageGroupRepository.findById(id).get();
            ageGroup.setAge(Name);
            ageGroupRepository.save(ageGroup);
            response.setEntity(ageGroup).setIsSuccessful(true);
        } catch (Exception e) {
            response.setExceptionMessage(e.getMessage());
        }
        return response;
    }

    @Override
    public ServiceResponse<AgeGroup> delete(Long id) {
        ServiceResponse<AgeGroup> response = new ServiceResponse<>();
        try {
            AgeGroup ageGroup = ageGroupRepository.findById(id).get();
            ageGroup.setIsDeleted(true);
            ageGroupRepository.save(ageGroup);
            response.setIsSuccessful(true);
        } catch (Exception e) {
            response.setExceptionMessage(e.getMessage());
        }
        return response;
    }

    @Override
    public ServiceResponse<AgeGroup> getAll() {
        ServiceResponse<AgeGroup> response = new ServiceResponse<>();
        try {
            List<AgeGroup> ageGroup = ageGroupRepository.findAll().stream().filter(x -> !x.getIsDeleted())
                    .collect(java.util.stream.Collectors.toList());
            response.setList(ageGroup).setIsSuccessful(true);
        } catch (Exception e) {
            response.setExceptionMessage(e.getMessage());
        }
        return response;
    }

}
