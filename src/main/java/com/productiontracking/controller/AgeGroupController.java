package com.productiontracking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.productiontracking.dto.request.AgeGroupRequest;
import com.productiontracking.dto.response.ServiceResponse;
import com.productiontracking.entity.AgeGroup;
import com.productiontracking.service.AgeGroupService;

@RestController
@RequestMapping("/api/v1")
public class AgeGroupController {
    @Autowired
    private AgeGroupService ageGroupService;

    @PostMapping("/age-group")
    public ServiceResponse<AgeGroup> createAgeGroup(@RequestBody AgeGroupRequest ageGroupRequest) {
        return ageGroupService.create(ageGroupRequest.getAge());
    }

    @PutMapping("/age-group/{id}")
    public ServiceResponse<AgeGroup> updateAgeGroup(@PathVariable Long id,
            @RequestBody AgeGroupRequest ageGroupRequest) {
        return ageGroupService.update(id, ageGroupRequest.getAge());
    }

    @DeleteMapping("/age-group/{id}")
    public ServiceResponse<AgeGroup> deleteAgeGroup(@PathVariable Long id) {
        return ageGroupService.delete(id);
    }

    @GetMapping("/age-groups")
    public ServiceResponse<AgeGroup> getAllAgeGroup() {
        return ageGroupService.getAll();
    }

}
