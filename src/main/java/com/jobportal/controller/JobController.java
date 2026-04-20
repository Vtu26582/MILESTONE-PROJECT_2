package com.jobportal.controller;

import com.jobportal.model.Job;
import com.jobportal.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class JobController {

    private final JobService jobService;

    @Autowired
    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @GetMapping("/")
    public String index(Model model) {
        List<Job> featuredJobs = jobService.findAllActiveJobs();
        if (featuredJobs.size() > 6) {
            featuredJobs = featuredJobs.subList(0, 6);
        }
        model.addAttribute("jobs", featuredJobs);
        return "index";
    }

    @GetMapping("/jobs")
    public String listJobs(@RequestParam(required = false) String keyword, Model model) {
        List<Job> jobs = jobService.searchJobs(keyword);
        model.addAttribute("jobs", jobs);
        model.addAttribute("keyword", keyword);
        return "jobs";
    }

    @GetMapping("/jobs/{id}")
    public String jobDetail(@PathVariable Long id, Model model) {
        Job job = jobService.findById(id);
        model.addAttribute("job", job);
        return "job-detail";
    }
}
