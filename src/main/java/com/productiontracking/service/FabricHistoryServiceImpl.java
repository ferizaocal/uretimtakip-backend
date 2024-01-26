package com.productiontracking.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.productiontracking.dto.request.CreateFabricHistoryRequest;
import com.productiontracking.dto.response.FabricHistoryResponse;
import com.productiontracking.dto.response.ServiceResponse;
import com.productiontracking.entity.FabricHistory;
import com.productiontracking.mapper.ModelMapperService;
import com.productiontracking.repository.FabricHistoryRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FabricHistoryServiceImpl implements FabricHistoryService {

    @Autowired
    private ModelMapperService modelMapperService;

    @Autowired
    private FabricHistoryRepository fabricHistoryRepository;

    @Override
    public ServiceResponse<FabricHistoryResponse> getFabricHistories(Long fabricId) {
        ServiceResponse<FabricHistoryResponse> response = new ServiceResponse<>();
        try {
            List<FabricHistory> fabricHistories = fabricHistoryRepository.findByFabricId(fabricId);
            List<FabricHistoryResponse> fabricHistoryResponses = fabricHistories.stream()
                    .filter(c -> c.getIsDeleted() == false).map(fabricHistory -> {
                        FabricHistoryResponse fabricHistoryResponse = modelMapperService.forResponse().map(
                                fabricHistory,
                                FabricHistoryResponse.class);

                        return fabricHistoryResponse;
                    }).collect(java.util.stream.Collectors.toList());
            response.setList(fabricHistoryResponses).setIsSuccessful(true);
        } catch (Exception e) {
            response.setExceptionMessage(e.getMessage().toString());
        }
        return response;
    }

    @Override
    public ServiceResponse<FabricHistoryResponse> addFabricHistoryEntry(CreateFabricHistoryRequest request) {
        ServiceResponse<FabricHistoryResponse> response = new ServiceResponse<>();
        try {
            FabricHistory fabricHistory = new FabricHistory();
            fabricHistory.setFabricId(request.getFabricId());
            fabricHistory.setQuantity(request.getQuantity());
            fabricHistory.setType(FabricHistory.Type.IN.toString());
            fabricHistoryRepository.save(fabricHistory);
            log.info("Fabric history entry added successfully");
            FabricHistoryResponse fabricHistoryResponse = modelMapperService.forResponse().map(fabricHistory,
                    FabricHistoryResponse.class);
            response.setIsSuccessful(true).setEntity(fabricHistoryResponse);
        } catch (Exception e) {
            response.setExceptionMessage(e.getMessage().toString());
        }
        return response;
    }

    @Override
    public ServiceResponse<FabricHistoryResponse> deleteFabricHistoryEntry(Long fabricHistoryId) {
        ServiceResponse<FabricHistoryResponse> response = new ServiceResponse<>();
        try {
            FabricHistory fabricHistory = fabricHistoryRepository.findById(fabricHistoryId).get();
            fabricHistory.setIsDeleted(true);
            fabricHistoryRepository.save(fabricHistory);
            log.info("Fabric history entry deleted successfully");
            response.setIsSuccessful(true);
        } catch (Exception e) {
            response.setExceptionMessage(e.getMessage().toString());
        }
        return response;
    }

}
