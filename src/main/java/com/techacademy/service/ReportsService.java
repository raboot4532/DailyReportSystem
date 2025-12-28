package com.techacademy.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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

 // 1件を検索
    public Reports findById(Integer id) {
        // findByIdで検索
        Optional<Reports> option = reportsRepository.findById(id);
        // 取得できなかった場合はnullを返す
        Reports reports = option.orElse(null);
        return reports;
    }

    // 日報保存
    @Transactional
    public ErrorKinds save(Reports reports) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetail userDetail = (UserDetail) auth.getPrincipal();

        Employee employee = employeeRepository.findById(userDetail.getEmployee().getCode()).orElseThrow();
        reports.setEmployee(employee);
        boolean exists = reportsRepository.existsByEmployeeCodeAndReportDate(employee.getCode(), reports.getReportDate());
        if (exists) {
            return ErrorKinds.DATECHECK_ERROR;
        }


        reports.setDeleteFlg(false);

        LocalDateTime now = LocalDateTime.now();
        reports.setCreatedAt(now);
        reports.setUpdatedAt(now);



        reportsRepository.save(reports);
        return ErrorKinds.SUCCESS;
    }

    // 日報更新
    @Transactional
    public ErrorKinds update(Reports reports) {
        Reports rep = findById(reports.getId());
        //重複チェック
        boolean exists = reportsRepository.existsByEmployeeCodeAndReportDateAndIdNot(rep.getEmployee().getCode(), reports.getReportDate(), reports.getId());

            if (exists) {
                return ErrorKinds.DATECHECK_ERROR;
            }


        reports.setDeleteFlg(false);
        reports.setCreatedAt(rep.getCreatedAt());
        LocalDateTime now = LocalDateTime.now();
        reports.setUpdatedAt(now);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetail userDetail = (UserDetail) auth.getPrincipal();

        Employee employee = employeeRepository.findById(userDetail.getEmployee().getCode()).orElseThrow();
        reports.setEmployee(employee);

        reportsRepository.save(reports);
        return ErrorKinds.SUCCESS;
    }

 // 日報削除
    @Transactional
    public ErrorKinds delete(int id, UserDetail userDetail) {

        Reports reports = findById(id);
        LocalDateTime now = LocalDateTime.now();
        reports.setUpdatedAt(now);
        reports.setDeleteFlg(true);

        return ErrorKinds.SUCCESS;
    }

}
