package rw.arsene.erp.v1.security.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import rw.arsene.erp.v1.entity.Employee;
import rw.arsene.erp.v1.exception.ResourceNotFoundException;
import rw.arsene.erp.v1.repository.EmployeeRepository;

@Service
@RequiredArgsConstructor
public class SecurityService {
    
    private final EmployeeRepository employeeRepository;
    
    public UserDetailsImpl getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetailsImpl) {
            return (UserDetailsImpl) authentication.getPrincipal();
        }
        throw new RuntimeException("No authenticated user found");
    }
    
    public Employee getCurrentEmployee() {
        UserDetailsImpl userDetails = getCurrentUser();
        return employeeRepository.findById(userDetails.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", userDetails.getId()));
    }
    
    public Long getCurrentEmployeeId() {
        return getCurrentUser().getId();
    }
    
    public String getCurrentEmployeeEmail() {
        return getCurrentUser().getEmail();
    }
    
    public boolean isCurrentUser(Long employeeId) {
        return getCurrentEmployeeId().equals(employeeId);
    }
    
    public boolean hasRole(String role) {
        UserDetailsImpl userDetails = getCurrentUser();
        return userDetails.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals(role));
    }
    
    public boolean isAdmin() {
        return hasRole("ROLE_ADMIN");
    }
    
    public boolean isManager() {
        return hasRole("ROLE_MANAGER");
    }
    
    public boolean isEmployee() {
        return hasRole("ROLE_EMPLOYEE");
    }
}
