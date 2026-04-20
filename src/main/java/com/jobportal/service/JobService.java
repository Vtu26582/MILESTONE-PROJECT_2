package com.jobportal.service;

import com.jobportal.model.Job;
import com.jobportal.model.enums.JobStatus;
import com.jobportal.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class JobService {

    private final JobRepository jobRepository;

    @Autowired
    public JobService(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    public List<Job> findAllActiveJobs() {
        return jobRepository.findByStatusOrderByPostedAtDesc(JobStatus.ACTIVE);
    }

    public List<Job> searchJobs(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return findAllActiveJobs();
        }
        return jobRepository.searchJobs(keyword.trim());
    }

    public Job findById(Long id) {
        return jobRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job not found: " + id));
    }

    public List<Job> findJobsByEmployer(Long employerId) {
        return jobRepository.findByEmployerIdOrderByPostedAtDesc(employerId);
    }

    @Transactional
    public Job saveJob(Job job) {
        return jobRepository.save(job);
    }

    @Transactional
    public void deleteJob(Long id, Long employerId) {
        Job job = findById(id);
        if (!job.getEmployer().getId().equals(employerId)) {
            throw new RuntimeException("Unauthorized to delete this job");
        }
        jobRepository.deleteById(id);
    }
}
