package com.jobportal.controller;

import com.jobportal.model.Application;
import com.jobportal.model.Job;
import com.jobportal.model.User;
import com.jobportal.security.CustomUserDetails;
import com.jobportal.service.ApplicationService;
import com.jobportal.service.FileStorageService;
import com.jobportal.service.JobService;
import com.jobportal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/student")
public class StudentController {

    private final UserService userService;
    private final JobService jobService;
    private final ApplicationService applicationService;
    private final FileStorageService fileStorageService;

    @Autowired
    public StudentController(UserService userService, JobService jobService, 
                           ApplicationService applicationService, FileStorageService fileStorageService) {
        this.userService = userService;
        this.jobService = jobService;
        this.applicationService = applicationService;
        this.fileStorageService = fileStorageService;
    }

    @GetMapping("/dashboard")
    public String dashboard(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        User user = userService.findByEmail(userDetails.getUsername());
        List<Application> applications = applicationService.findStudentApplications(user.getId());
        model.addAttribute("applications", applications);
        model.addAttribute("user", user);
        return "student/dashboard";
    }

    @GetMapping("/profile")
    public String profile(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        User user = userService.findByEmail(userDetails.getUsername());
        model.addAttribute("user", user);
        return "student/profile";
    }

    @PostMapping("/profile")
    public String updateProfile(@AuthenticationPrincipal CustomUserDetails userDetails, 
                               @ModelAttribute User userForm, RedirectAttributes redirectAttributes) {
        User user = userService.findByEmail(userDetails.getUsername());
        userService.updateUserProfile(user.getId(), userForm);
        redirectAttributes.addFlashAttribute("successMessage", "Profile updated successfully!");
        return "redirect:/student/profile";
    }

    @PostMapping("/upload-resume")
    public String uploadResume(@AuthenticationPrincipal CustomUserDetails userDetails,
                              @RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Please select a file to upload.");
            return "redirect:/student/profile";
        }

        try {
            User user = userService.findByEmail(userDetails.getUsername());
            String fileName = fileStorageService.storeFile(file);
            userService.updateResumePath(user.getId(), fileName);
            redirectAttributes.addFlashAttribute("successMessage", "Resume uploaded successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to upload file: " + e.getMessage());
        }

        return "redirect:/student/profile";
    }

    @PostMapping("/apply/{jobId}")
    public String applyForJob(@AuthenticationPrincipal CustomUserDetails userDetails,
                             @PathVariable Long jobId, 
                             @RequestParam("coverLetter") String coverLetter,
                             RedirectAttributes redirectAttributes) {
        try {
            User user = userService.findByEmail(userDetails.getUsername());
            Job job = jobService.findById(jobId);
            
            applicationService.applyForJob(user, job, coverLetter);
            redirectAttributes.addFlashAttribute("successMessage", "Successfully applied for the job!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        
        return "redirect:/jobs/" + jobId;
    }

    @GetMapping("/applications")
    public String myApplications(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        User user = userService.findByEmail(userDetails.getUsername());
        model.addAttribute("applications", applicationService.findStudentApplications(user.getId()));
        return "student/applications";
    }
}
