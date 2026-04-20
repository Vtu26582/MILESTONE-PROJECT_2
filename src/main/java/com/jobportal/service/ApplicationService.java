package com.jobportal.service;

import com.jobportal.model.Application;
import com.jobportal.model.Job;
import com.jobportal.model.User;
import com.jobportal.model.enums.ApplicationStatus;
import com.jobportal.repository.ApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ApplicationService {

    private final ApplicationRepository applicationRepository;

    @Autowired
    public ApplicationService(ApplicationRepository applicationRepository) {
        this.applicationRepository = applicationRepository;
    }

    @Transactional
    public Application applyForJob(User user, Job job, String coverLetter) {
        Optional<Application> existing = applicationRepository.findByUserIdAndJobId(user.getId(), job.getId());
        if (existing.isPresent()) {
            throw new RuntimeException("You have already applied for this job.");
        }

        Application application = new Application();
        application.setUser(user);
        application.setJob(job);
        application.setCoverLetter(coverLetter);
        application.setStatus(ApplicationStatus.PENDING);
        
        return applicationRepository.save(application);
    }

    public List<Application> findStudentApplications(Long studentId) {
        return applicationRepository.findByUserIdOrderByAppliedAtDesc(studentId);
    }

    public List<Application> findApplicationsForJob(Long jobId) {
        return applicationRepository.findByJobIdOrderByAppliedAtDesc(jobId);
    }

    public List<Application> findApplicationsForEmployer(Long employerId) {
        return applicationRepository.findByJobEmployerIdOrderByAppliedAtDesc(employerId);
    }

    public Application findById(Long id) {
        return applicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Application not found"));
    }

    @Transactional
    public void updateApplicationStatus(Long applicationId, ApplicationStatus status, Long employerId) {
        Application app = findById(applicationId);
        if (!app.getJob().getEmployer().getId().equals(employerId)) {
            throw new RuntimeException("Unauthorized to update this application");
        }
        app.setStatus(status);
        applicationRepository.save(app);
    }
    
    public boolean hasApplied(Long userId, Long jobId) {
        return applicationRepository.findByUserIdAndJobId(userId, jobId).isPresent();
    }
    
    public long countApplicationsForEmployer(Long employerId) {
        return applicationRepository.countByJobEmployerId(employerId);
    }
}
