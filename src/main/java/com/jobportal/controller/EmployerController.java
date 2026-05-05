package com.jobportal.controller;

import com.jobportal.model.Application;
import com.jobportal.model.Job;
import com.jobportal.model.User;
import com.jobportal.model.enums.ApplicationStatus;
import com.jobportal.security.CustomUserDetails;
import com.jobportal.service.ApplicationService;
import com.jobportal.service.JobService;
import com.jobportal.service.UserService;
import com.jobportal.service.EmailService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/employer")
public class EmployerController {

    private final JobService jobService;
    private final UserService userService;
    private final ApplicationService applicationService;
    private final EmailService emailService;

    @Autowired
    public EmployerController(JobService jobService, UserService userService, ApplicationService applicationService, EmailService emailService) {
        this.jobService = jobService;
        this.userService = userService;
        this.applicationService = applicationService;
        this.emailService = emailService;
    }

    @GetMapping("/dashboard")
    public String dashboard(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        User employer = userService.findByEmail(userDetails.getUsername());
        List<Job> jobs = jobService.findJobsByEmployer(employer.getId());
        long applicationCount = applicationService.countApplicationsForEmployer(employer.getId());
        
        model.addAttribute("jobs", jobs);
        model.addAttribute("jobCount", jobs.size());
        model.addAttribute("applicationCount", applicationCount);
        return "employer/dashboard";
    }

    @GetMapping("/jobs/new")
    public String newJobForm(Model model) {
        model.addAttribute("job", new Job());
        return "employer/post-job";
    }

    @PostMapping("/jobs/new")
    public String postJob(@AuthenticationPrincipal CustomUserDetails userDetails, 
                         @Valid @ModelAttribute("job") Job job, 
                         BindingResult result) {
        if (result.hasErrors()) {
            return "employer/post-job";
        }
        
        User employer = userService.findByEmail(userDetails.getUsername());
        job.setEmployer(employer);
        jobService.saveJob(job);
        
        return "redirect:/employer/dashboard?success";
    }

    @GetMapping("/jobs/{id}/edit")
    public String editJobForm(@AuthenticationPrincipal CustomUserDetails userDetails, 
                             @PathVariable Long id, Model model) {
        Job job = jobService.findById(id);
        User employer = userService.findByEmail(userDetails.getUsername());
        
        if (!job.getEmployer().getId().equals(employer.getId())) {
            return "redirect:/employer/dashboard?error=unauthorized";
        }
        
        model.addAttribute("job", job);
        return "employer/post-job";
    }

    @PostMapping("/jobs/{id}/edit")
    public String updateJob(@AuthenticationPrincipal CustomUserDetails userDetails,
                           @PathVariable Long id,
                           @Valid @ModelAttribute("job") Job job,
                           BindingResult result) {
        if (result.hasErrors()) {
            return "employer/post-job";
        }
        
        Job existingJob = jobService.findById(id);
        User employer = userService.findByEmail(userDetails.getUsername());
        
        if (!existingJob.getEmployer().getId().equals(employer.getId())) {
            return "redirect:/employer/dashboard?error=unauthorized";
        }
        
        existingJob.setTitle(job.getTitle());
        existingJob.setDescription(job.getDescription());
        existingJob.setCompany(job.getCompany());
        existingJob.setLocation(job.getLocation());
        existingJob.setCategory(job.getCategory());
        existingJob.setSkillsRequired(job.getSkillsRequired());
        existingJob.setExperienceLevel(job.getExperienceLevel());
        existingJob.setSalaryMin(job.getSalaryMin());
        existingJob.setSalaryMax(job.getSalaryMax());
        existingJob.setStatus(job.getStatus());
        existingJob.setDeadline(job.getDeadline());
        
        jobService.saveJob(existingJob);
        return "redirect:/employer/dashboard?updated";
    }

    @PostMapping("/jobs/{id}/delete")
    public String deleteJob(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable Long id) {
        User employer = userService.findByEmail(userDetails.getUsername());
        jobService.deleteJob(id, employer.getId());
        return "redirect:/employer/dashboard?deleted";
    }

    @GetMapping("/jobs/{id}/applicants")
    public String viewApplicants(@AuthenticationPrincipal CustomUserDetails userDetails,
                                @PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Job job = jobService.findById(id);
        User employer = userService.findByEmail(userDetails.getUsername());
        
        if (!job.getEmployer().getId().equals(employer.getId())) {
            redirectAttributes.addFlashAttribute("errorMessage", "Unauthorized access.");
            return "redirect:/employer/dashboard";
        }
        
        List<Application> applications = applicationService.findApplicationsForJob(id);
        model.addAttribute("job", job);
        model.addAttribute("applications", applications);
        return "employer/applicants";
    }

    @PostMapping("/applications/{appId}/status")
    public String updateApplicationStatus(@AuthenticationPrincipal CustomUserDetails userDetails,
                                         @PathVariable Long appId,
                                         @RequestParam ApplicationStatus status,
                                         @RequestParam Long jobId) {
        User employer = userService.findByEmail(userDetails.getUsername());
        applicationService.updateApplicationStatus(appId, status, employer.getId());
        
        // Fetch application to send email
        Application app = applicationService.findById(appId);
        
        // Trigger Automated Email via Mock Logger
        String message = status.name().equals("ACCEPTED") 
            ? "Congratulations " + app.getUser().getFullName() + "! The company '" + app.getJob().getCompany() + "' has ACCEPTED your application for the '" + app.getJob().getTitle() + "' role." 
            : "Dear " + app.getUser().getFullName() + ", we regret to inform you that the company '" + app.getJob().getCompany() + "' has REJECTED your application for the '" + app.getJob().getTitle() + "' role.";
            
        emailService.sendSimpleMessage(app.getUser().getEmail(), "JobNest Employer Update", message);
        
        return "redirect:/employer/jobs/" + jobId + "/applicants?statusUpdated";
    }
}
