package com.productiontracking.service;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import com.productiontracking.dto.request.CreateUserRequest;
import com.productiontracking.entity.Operation;
import com.productiontracking.entity.ProductionModel;
import com.productiontracking.entity.Role;
import com.productiontracking.repository.OperationRepository;
import com.productiontracking.repository.ProductionModelRepository;
import com.productiontracking.repository.RoleRepository;

@Service
public class StartupService {
    Logger logger = org.slf4j.LoggerFactory.getLogger(StartupService.class);

    RoleRepository roleRepository;
    UserService userService;
    ProductionModelRepository productionModelRepository;
    OperationRepository operationRepository;

    public StartupService(RoleRepository roleRepository, UserService userService,
            ProductionModelRepository productionModelRepository, OperationRepository operationRepository) {
        this.roleRepository = roleRepository;
        this.userService = userService;
        this.productionModelRepository = productionModelRepository;
        this.operationRepository = operationRepository;
    }

    @PostConstruct
    public void init() {

        initRole();
        initUser();
        initOperationAndProductionModel();
    }

    public void initUser() {
        Long count = userService.count();
        if (count == 0) {
            logger.info("No user found, creating default user");
            userService.create(
                    new CreateUserRequest("Feriza", "Öcal", "ferizaocal60@gmail.com", "1234567",
                            "admin",
                            Role.RoleName.Admin.toString()));
            logger.info("Default user created");
        }
    }

    public void initRole() {
        Long count = roleRepository.count();
        if (count == 0) {
            logger.info("No role found, creating default role");
            roleRepository.save(new Role(Role.RoleName.Admin.toString()));
            logger.info("Default role created");

        }
    }

    public void initOperationAndProductionModel() {

        Long productionModelCount = productionModelRepository.count();
        Long operationCount = operationRepository.count();
        if (productionModelCount == 0 && operationCount == 0) {
            ProductionModel productionModel = new ProductionModel();
            productionModel.setName("Bebek Pijama");
            productionModel.setIcon("Bebek Pijama");
            productionModel.setStatus(ProductionModel.Status.ACTIVE.toString());
            productionModelRepository.save(productionModel);

            Operation cutting = new Operation();
            cutting.setOperationName("Kesim");
            cutting.setOperationNumber(1);
            cutting.setProductionModelId(productionModel.getId());
            cutting.setStatus(Operation.Status.ACTIVE.toString());
            operationRepository.save(cutting);

            Operation tailor = new Operation();
            tailor.setOperationName("Terzi");
            tailor.setOperationNumber(2);
            tailor.setProductionModelId(productionModel.getId());
            tailor.setStatus(Operation.Status.ACTIVE.toString());
            operationRepository.save(tailor);

            Operation washing = new Operation();
            washing.setOperationName("Yıkama");
            washing.setOperationNumber(3);
            washing.setProductionModelId(productionModel.getId());
            washing.setStatus(Operation.Status.ACTIVE.toString());
            operationRepository.save(washing);

            Operation pack = new Operation();
            pack.setOperationName("Paketleme");
            pack.setOperationNumber(4);
            pack.setProductionModelId(productionModel.getId());
            pack.setStatus(Operation.Status.ACTIVE.toString());
            operationRepository.save(pack);

            Operation store = new Operation();
            store.setOperationName("Mağaza");
            store.setOperationNumber(5);
            store.setProductionModelId(productionModel.getId());
            store.setStatus(Operation.Status.ACTIVE.toString());
            operationRepository.save(store);

        }

    }

}
