package com.techacademy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.techacademy.constants.ErrorKinds;
import com.techacademy.constants.ErrorMessage;
import com.techacademy.entity.Employee;
import com.techacademy.entity.Reports;
import com.techacademy.service.ReportsService;
import com.techacademy.service.UserDetail;

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

 // 日報詳細画面
    @GetMapping(value = "/{id}/")
    public String detail(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("reports", reportsService.findById(id));

        return "reports/detail";
    }

 // 日報新規登録画面
    @GetMapping(value = "/add")
    public String create(@ModelAttribute Reports reports) {

        return "reports/new";
    }

 // 日報新規登録処理
    @PostMapping(value = "/add")
    public String add(@Validated Reports reports, BindingResult res, Model model) {
     // 入力チェック
        if (res.hasErrors()) {
            return create(reports);
        }
        ErrorKinds result = reportsService.save(reports);

        if (result == ErrorKinds.DUPLICATE_ERROR) {
            model.addAttribute("errorMessage", "既に登録されている日付です");
            return create(reports);
        }

        return "redirect:/reports";
    }

 // 日報削除処理
    @PostMapping(value = "/{id}/delete")
    public String delete(@PathVariable("id") Integer id, @AuthenticationPrincipal UserDetail userDetail,
            Model model) {

            reportsService.delete(id, userDetail);


        return "redirect:/reports";
    }


}