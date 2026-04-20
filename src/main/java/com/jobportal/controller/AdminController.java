package com.jobportal.controller;

import com.jobportal.model.Job;
import com.jobportal.model.User;
import com.jobportal.repository.ApplicationRepository;
import com.jobportal.service.JobService;
import com.jobportal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserRepository userRepository;
    private final JobService jobService;
    private final ApplicationRepository applicationRepository;

    @Autowired
    public AdminController(UserRepository userRepository, JobService jobService, ApplicationRepository applicationRepository) {
        this.userRepository = userRepository;
        this.jobService = jobService;
        this.applicationRepository = applicationRepository;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        long userCount = userRepository.count();
        List<Job> allJobs = jobService.findAllActiveJobs();
        long applicationCount = applicationRepository.count();
        
        model.addAttribute("userCount", userCount);
        model.addAttribute("jobCount", allJobs.size());
        model.addAttribute("applicationCount", applicationCount);
        
        return "admin/dashboard";
    }

    @GetMapping("/users")
    public String users(Model model) {
        List<User> users = userRepository.findAll();
        model.addAttribute("users", users);
        return "admin/users";
    }
}
