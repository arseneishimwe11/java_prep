package rw.arsene.erp.v1.security.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rw.arsene.erp.v1.entity.Employee;
import rw.arsene.erp.v1.repository.EmployeeRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    
    @Autowired
    EmployeeRepository employeeRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Employee employee = employeeRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Employee Not Found with email: " + email));

        return UserDetailsImpl.build(employee);
    }
}
