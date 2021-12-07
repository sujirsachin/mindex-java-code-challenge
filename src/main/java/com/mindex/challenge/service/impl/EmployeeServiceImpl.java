package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private static final Logger LOG = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public Employee create(Employee employee) {
        LOG.debug("Creating employee: [{}]", employee);

        employee.setEmployeeId(UUID.randomUUID().toString());
        employeeRepository.insert(employee);

        return employee;
    }

    @Override
    public Employee read(String employeeId) {
        LOG.debug("Reading employee with employeeId: [{}]", employeeId);

        Employee employee = employeeRepository.findByEmployeeId(employeeId);

        if (employee == null) {
            throw new RuntimeException("Invalid employeeId: " + employeeId);
        }

        return employee;
    }

    @Override
    public Employee update(Employee employee) {
        LOG.debug("Updating employee: [{}]", employee);

        return employeeRepository.save(employee);
    }

    public int getNumberOfReports(String employeeId) {
        int accumulatedTotal = 0;

        Employee employee = this.read(employeeId);
        if (employee == null) {
            throw new RuntimeException("Employee not found");
        }
        List<Employee> reports = employee.getDirectReports();
        if (reports != null) {
            for (Employee reportingEmployee : reports) {
                accumulatedTotal += 1 + getNumberOfReports(reportingEmployee.getEmployeeId());
            }
        }
        
        return accumulatedTotal;
    }

}
