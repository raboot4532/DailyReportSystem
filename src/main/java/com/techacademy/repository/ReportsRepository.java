package com.techacademy.repository;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;

import com.techacademy.entity.Reports;

public interface ReportsRepository extends JpaRepository<Reports, Integer> {

    boolean existsByEmployeeCodeAndReportDate(String code, LocalDate reportDate);
}