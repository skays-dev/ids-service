package com.ids.infrastructure.config;

import com.ids.infrastructure.persistence.main.entity.AlertStatusJpaEntity;
import com.ids.infrastructure.persistence.main.entity.RiskLevelJpaEntity;
import com.ids.infrastructure.persistence.main.repository.JpaAlertStatusRepository;
import com.ids.infrastructure.persistence.main.repository.JpaRiskLevelRepository;
import com.ids.infrastructure.persistence.users.entity.AppRoleJpaEntity;
import com.ids.infrastructure.persistence.users.entity.AppUserJpaEntity;
import com.ids.infrastructure.persistence.users.repository.JpaAppRoleRepository;
import com.ids.infrastructure.persistence.users.repository.JpaAppUserRepository;
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
    private final JpaAppRoleRepository roleRepository;
    private final JpaAppUserRepository userRepository;
    private final JpaRiskLevelRepository riskLevelRepository;
    private final JpaAlertStatusRepository statusRepository;
    private final PasswordEncoder passwordEncoder;
    private final PlatformTransactionManager usersTransactionManager;
    private final PlatformTransactionManager mainTransactionManager;

    public DataInitializer(JpaAppRoleRepository roleRepository,
                           JpaAppUserRepository userRepository,
                           JpaRiskLevelRepository riskLevelRepository,
                           JpaAlertStatusRepository statusRepository,
                           PasswordEncoder passwordEncoder,
                           @Qualifier("usersTransactionManager") PlatformTransactionManager usersTransactionManager,
                           @Qualifier("mainTransactionManager") PlatformTransactionManager mainTransactionManager) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.riskLevelRepository = riskLevelRepository;
        this.statusRepository = statusRepository;
        this.passwordEncoder = passwordEncoder;
        this.usersTransactionManager = usersTransactionManager;
        this.mainTransactionManager = mainTransactionManager;
    }

    @Override
    public void run(String... args) {
        new TransactionTemplate(usersTransactionManager).executeWithoutResult(status -> initUsers());
        new TransactionTemplate(mainTransactionManager).executeWithoutResult(status -> initMainLookups());
    }

    private void initUsers() {
        AppRoleJpaEntity admin = upsertRole("ROLE_ADMIN", "System administrator");

        createUserIfMissing("admin", "admin@ids.local", "admin123", Set.of(admin));
    }

    private AppRoleJpaEntity upsertRole(String name, String description) {
        AppRoleJpaEntity role = roleRepository.findByName(name).orElseGet(AppRoleJpaEntity::new);
        role.setName(name);
        role.setDescription(description);
        return roleRepository.save(role);
    }

    private void createUserIfMissing(String username, String email, String password, Set<AppRoleJpaEntity> roles) {
        if (userRepository.existsByUsernameIgnoreCase(username)) {
            return;
        }
        AppUserJpaEntity user = new AppUserJpaEntity();
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
        RiskLevelJpaEntity risk = riskLevelRepository.findByCode(code).orElseGet(RiskLevelJpaEntity::new);
        risk.setCode(code);
        risk.setLabel(label);
        risk.setMinConfidence(min);
        risk.setMaxConfidence(max);
        risk.setSeverityOrder(order);
        riskLevelRepository.save(risk);
    }

    private void upsertStatus(String code, String label, int order) {
        AlertStatusJpaEntity alertStatus = statusRepository.findByCode(code).orElseGet(AlertStatusJpaEntity::new);
        alertStatus.setCode(code);
        alertStatus.setLabel(label);
        alertStatus.setSortOrder(order);
        statusRepository.save(alertStatus);
    }
}
