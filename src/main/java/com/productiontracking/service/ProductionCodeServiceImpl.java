package com.productiontracking.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.productiontracking.dto.request.ProductionCodeRequest;
import com.productiontracking.dto.response.ServiceResponse;
import com.productiontracking.entity.ProductionCode;
import com.productiontracking.entity.User;
import com.productiontracking.exception.DuplicateProductionCodeException;
import com.productiontracking.exception.NotFoundException;
import com.productiontracking.repository.ProductionCodeRepository;
import com.productiontracking.repository.UserRepository;

@Service
public class ProductionCodeServiceImpl implements ProductionCodeService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductionCodeRepository productionCodeRepository;

    @Override
    public ServiceResponse<ProductionCode> create(ProductionCodeRequest _pProductionCode, Long createdBy) {
        ServiceResponse<ProductionCode> response = new ServiceResponse<>();
        try {
            ProductionCode isExits = productionCodeRepository.findByCode(_pProductionCode.getName());
            if (isExits != null) {
                throw new DuplicateProductionCodeException(_pProductionCode.getName());
            }
            Optional<User> user = userRepository.findById(createdBy);
            if (!user.isPresent()) {
                throw new NotFoundException("User not found");
            }
            if (user.get().getActiveProductionModelId() == null) {
                throw new NotFoundException("User does not have an active production model");
            }
            ProductionCode productionCode = new ProductionCode();
            productionCode.setProductionModelId(user.get().getActiveProductionModelId());
            productionCode.setCode(_pProductionCode.getName());
            productionCodeRepository.save(productionCode);
            response.setEntity(productionCode).setIsSuccessful(true);
        } catch (Exception e) {
            response.setExceptionMessage(e.getMessage());
        }
        return response;
    }

    @Override
    public ServiceResponse<ProductionCode> update(ProductionCodeRequest productionCode, Long id) {
        ServiceResponse<ProductionCode> response = new ServiceResponse<>();
        try {
            Optional<ProductionCode> productionCodeOptional = productionCodeRepository.findById(id);
            if (!productionCodeOptional.isPresent()) {
                throw new NotFoundException("Production code not found");
            }
            ProductionCode productionCodeEntity = productionCodeOptional.get();
            productionCodeEntity.setCode(productionCode.getName());
            productionCodeRepository.save(productionCodeEntity);
            response.setEntity(productionCodeEntity).setIsSuccessful(true);
        } catch (Exception e) {
            response.setExceptionMessage(e.getMessage());
        }
        return response;
    }

    @Override
    public ServiceResponse<ProductionCode> delete(Long id) {
        ServiceResponse<ProductionCode> response = new ServiceResponse<>();
        try {
            Optional<ProductionCode> productionCodeOptional = productionCodeRepository.findById(id);
            if (!productionCodeOptional.isPresent()) {
                throw new NotFoundException("Production code not found");
            }
            ProductionCode productionCodeEntity = productionCodeOptional.get();
            productionCodeEntity.setIsDeleted(true);
            productionCodeRepository.save(productionCodeEntity);
            response.setEntity(productionCodeEntity).setIsSuccessful(true);
        } catch (Exception e) {
            response.setExceptionMessage(e.getMessage());
        }
        return response;
    }

    @Override
    public ServiceResponse<ProductionCode> getAll(Long userId) {
        ServiceResponse<ProductionCode> response = new ServiceResponse<>();
        try {
            User admin = userRepository.findById(userId).get();

            if (admin.getActiveProductionModelId() == null) {
                throw new NotFoundException("User does not have an active production model");
            }
            List<ProductionCode> productionCodes = productionCodeRepository.findAll().stream()
                    .filter(x -> x.getIsDeleted() == false
                            && x.getProductionModelId().equals(admin.getActiveProductionModelId()))
                    .collect(Collectors.toList());
            response.setList(productionCodes).setIsSuccessful(true);
        } catch (Exception e) {
            response.setExceptionMessage(e.getMessage());
        }
        return response;
    }

}
