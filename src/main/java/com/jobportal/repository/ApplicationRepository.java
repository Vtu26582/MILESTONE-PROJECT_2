package com.jobportal.repository;

import com.jobportal.model.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {
    
    List<Application> findByUserIdOrderByAppliedAtDesc(Long userId);
    
    List<Application> findByJobIdOrderByAppliedAtDesc(Long jobId);
    
    List<Application> findByJobEmployerIdOrderByAppliedAtDesc(Long employerId);
    
    Optional<Application> findByUserIdAndJobId(Long userId, Long jobId);
    
    long countByJobEmployerId(Long employerId);
}
