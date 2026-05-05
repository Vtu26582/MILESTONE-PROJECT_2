package com.jobportal.controller;

import com.jobportal.model.Job;
import com.jobportal.model.User;
import com.jobportal.repository.ApplicationRepository;
import com.jobportal.service.JobService;
import com.jobportal.repository.UserRepository;
import com.jobportal.model.Application;
import com.jobportal.model.enums.ApplicationStatus;
import com.jobportal.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserRepository userRepository;
    private final JobService jobService;
    private final ApplicationRepository applicationRepository;
    private final EmailService emailService;

    @Autowired
    public AdminController(UserRepository userRepository, JobService jobService, ApplicationRepository applicationRepository, EmailService emailService) {
        this.userRepository = userRepository;
        this.jobService = jobService;
        this.applicationRepository = applicationRepository;
        this.emailService = emailService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        long userCount = userRepository.count();
        List<Job> allJobs = jobService.findAllActiveJobs();
        List<Application> applications = applicationRepository.findAll();
        
        model.addAttribute("userCount", userCount);
        model.addAttribute("jobCount", allJobs.size());
        model.addAttribute("applicationCount", applications.size());
        model.addAttribute("applications", applications);
        
        return "admin/dashboard";
    }

    @GetMapping("/users")
    public String users(Model model) {
        List<User> users = userRepository.findAll();
        model.addAttribute("users", users);
        return "admin/users";
    }

    @PostMapping("/applications/{id}/update-status")
    public String updateStatus(@PathVariable Long id, @RequestParam String status) {
        Application app = applicationRepository.findById(id).orElseThrow(() -> new RuntimeException("Application Not Found"));
        
        // Update database
        app.setStatus(ApplicationStatus.valueOf(status)); // ACCEPTED or REJECTED
        applicationRepository.save(app);

        // Trigger Automated Email via Mock Logger
        String message = status.equals("ACCEPTED") 
            ? "Congratulations " + app.getUser().getFullName() + "! Your application for '" + app.getJob().getTitle() + "' has been ACCEPTED." 
            : "Dear " + app.getUser().getFullName() + ", we regret to inform you that your application for '" + app.getJob().getTitle() + "' has been REJECTED.";
            
        emailService.sendSimpleMessage(app.getUser().getEmail(), "JobNest Application Update", message);

        return "redirect:/admin/dashboard?success";
    }
}
