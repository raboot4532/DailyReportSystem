package com.techacademy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.techacademy.constants.ErrorKinds;
import com.techacademy.entity.Employee;
import com.techacademy.entity.Reports;
import com.techacademy.service.ReportsService;

@Controller
@RequestMapping("reports")
public class ReportsController {

    private final ReportsService reportsService;

    @Autowired
    public ReportsController(ReportsService reportsService) {
        this.reportsService = reportsService;
    }



    // 日報一覧画面
    @GetMapping
    public String list(Model model) {

        model.addAttribute("listSize", reportsService.findAll().size());
        model.addAttribute("reportsList", reportsService.findAll());


        return "reports/list";
    }

 // 日報新規登録画面
    @GetMapping(value = "/add")
    public String create(@ModelAttribute Reports reports) {



        return "reports/new";
    }

 // 日報新規登録処理
    @PostMapping(value = "/add")
    public String add(@Validated Reports reports, BindingResult res, Model model) {
        ErrorKinds result = reportsService.save(reports);


        if (result == ErrorKinds.DUPLICATE_ERROR) {
            model.addAttribute("errorMessage", "既に登録されている日付です");
            return create(reports);
        }


        // 入力チェック
        if (res.hasErrors()) {
            return create(reports);
        }
        reportsService.save(reports);


        return "redirect:/reports";
    }


}