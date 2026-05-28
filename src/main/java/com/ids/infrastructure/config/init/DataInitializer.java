package com.ids.infrastructure.config.init;

import com.ids.infrastructure.config.databases.IdsDbConfig;
import com.ids.infrastructure.config.databases.UsersDbConfig;
import com.ids.infrastructure.persistence.entity.ids.IdsAlertStatusJpaEntity;
import com.ids.infrastructure.persistence.entity.ids.IdsRiskLevelJpaEntity;
import com.ids.infrastructure.persistence.entity.usr.UsrRoleJpaEntity;
import com.ids.infrastructure.persistence.entity.usr.UsrUserJpaEntity;
import com.ids.infrastructure.persistence.repository.ids.IdsAlertStatusJpaRepository;
import com.ids.infrastructure.persistence.repository.ids.IdsRiskLevelJpaRepository;
import com.ids.infrastructure.persistence.repository.usr.UsrRoleJpaRepository;
import com.ids.infrastructure.persistence.repository.usr.UsrUserJpaRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.math.BigDecimal;
import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {
    private final UsrRoleJpaRepository roleRepository;
    private final UsrUserJpaRepository userRepository;
    private final IdsRiskLevelJpaRepository idsRiskLevelRepository;
    private final IdsAlertStatusJpaRepository statusRepository;
    private final PasswordEncoder passwordEncoder;
    private final PlatformTransactionManager usersTransactionManager;
    private final PlatformTransactionManager idsTransactionManager;

    public DataInitializer(
            UsrRoleJpaRepository roleRepository,
            UsrUserJpaRepository userRepository,
            IdsRiskLevelJpaRepository idsRiskLevelRepository,
            IdsAlertStatusJpaRepository statusRepository,
            PasswordEncoder passwordEncoder,
            @Qualifier(UsersDbConfig.TX_BEAN) PlatformTransactionManager usersTransactionManager,
            @Qualifier(IdsDbConfig.TX_BEAN) PlatformTransactionManager idsTransactionManager
    ) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.idsRiskLevelRepository = idsRiskLevelRepository;
        this.statusRepository = statusRepository;
        this.passwordEncoder = passwordEncoder;
        this.usersTransactionManager = usersTransactionManager;
        this.idsTransactionManager = idsTransactionManager;
    }

    @Override
    public void run(String... args) {
        new TransactionTemplate(usersTransactionManager).executeWithoutResult(status -> initUsers());
        new TransactionTemplate(idsTransactionManager).executeWithoutResult(status -> initMainLookups());
    }

    private void initUsers() {
        UsrRoleJpaEntity admin = upsertRole("ROLE_ADMIN", "System administrator");

        createUserIfMissing("admin", "admin@ids.local", "admin123", Set.of(admin));
    }

    private UsrRoleJpaEntity upsertRole(String name, String description) {
        UsrRoleJpaEntity role = roleRepository.findByName(name).orElseGet(UsrRoleJpaEntity::new);
        role.setName(name);
        role.setDescription(description);
        return roleRepository.save(role);
    }

    private void createUserIfMissing(String username, String email, String password, Set<UsrRoleJpaEntity> roles) {
        if (userRepository.existsByUsernameIgnoreCase(username)) {
            return;
        }
        UsrUserJpaEntity user = new UsrUserJpaEntity();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setEnabled(true);
        user.setRoles(roles);
        userRepository.save(user);
    }

    private void initMainLookups() {
        upsertRisk("LOW", "Low risk", new BigDecimal("0.00"), new BigDecimal("49.99"), 1);
        upsertRisk("MEDIUM", "Medium risk", new BigDecimal("50.00"), new BigDecimal("79.99"), 2);
        upsertRisk("HIGH", "High risk", new BigDecimal("80.00"), null, 3);

        upsertStatus("NEW", "New", 1);
        upsertStatus("VERIFIED", "Verified", 2);
        upsertStatus("SAFE", "Safe", 3);
    }

    private void upsertRisk(String code, String label, BigDecimal min, BigDecimal max, int order) {
        IdsRiskLevelJpaEntity risk = idsRiskLevelRepository.findByCode(code).orElseGet(IdsRiskLevelJpaEntity::new);
        risk.setCode(code);
        risk.setLabel(label);
        risk.setMinConfidence(min);
        risk.setMaxConfidence(max);
        risk.setSeverityOrder(order);
        idsRiskLevelRepository.save(risk);
    }

    private void upsertStatus(String code, String label, int order) {
        IdsAlertStatusJpaEntity idsAlertStatus = statusRepository.findByCode(code).orElseGet(IdsAlertStatusJpaEntity::new);
        idsAlertStatus.setCode(code);
        idsAlertStatus.setLabel(label);
        idsAlertStatus.setSortOrder(order);
        statusRepository.save(idsAlertStatus);
    }
}
