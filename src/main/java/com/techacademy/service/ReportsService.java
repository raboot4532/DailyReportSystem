package com.techacademy.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.techacademy.constants.ErrorKinds;
import com.techacademy.entity.Employee;
import com.techacademy.entity.Reports;
import com.techacademy.repository.EmployeeRepository;
import com.techacademy.repository.ReportsRepository;

import org.springframework.transaction.annotation.Transactional;

@Service
public class ReportsService {

    private final ReportsRepository reportsRepository;
    private final EmployeeRepository employeeRepository;


    public ReportsService(ReportsRepository reportsRepository, EmployeeRepository employeeRepository) {
        this.reportsRepository = reportsRepository;
        this.employeeRepository = employeeRepository;

    }

    // 日報一覧表示処理
    public List<Reports> findAll() {
        return reportsRepository.findAll();
    }

    // 日報保存
    @Transactional
    public ErrorKinds save(Reports reports) {

        reports.setDeleteFlg(false);

        LocalDateTime now = LocalDateTime.now();
        reports.setCreatedAt(now);
        reports.setUpdatedAt(now);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetail userDetail = (UserDetail) auth.getPrincipal();

        Employee employee = employeeRepository.findById(userDetail.getEmployee().getCode()).orElseThrow();
        reports.setEmployee(employee);

        boolean exists = reportsRepository.existsByEmployeeCodeAndReportDate(employee.getCode(), reports.getReportDate());
        if (exists) {
            return ErrorKinds.DUPLICATE_ERROR;
        }


        reportsRepository.save(reports);
        return ErrorKinds.SUCCESS;
    }

}
