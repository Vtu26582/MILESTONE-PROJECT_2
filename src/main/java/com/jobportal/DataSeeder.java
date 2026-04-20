package com.jobportal;

import com.jobportal.model.Application;
import com.jobportal.model.Job;
import com.jobportal.model.User;
import com.jobportal.model.enums.ApplicationStatus;
import com.jobportal.model.enums.JobStatus;
import com.jobportal.model.enums.Role;
import com.jobportal.repository.ApplicationRepository;
import com.jobportal.repository.JobRepository;
import com.jobportal.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;

@Configuration
public class DataSeeder {

    @Bean
    public CommandLineRunner loadData(UserRepository userRepository, 
                                     JobRepository jobRepository, 
                                     ApplicationRepository applicationRepository,
                                     PasswordEncoder passwordEncoder) {
        return args -> {
            if (userRepository.count() == 0) {
                // Create Admin
                User admin = new User();
                admin.setFullName("System Administrator");
                admin.setEmail("admin@jobportal.com");
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setRole(Role.ADMIN);
                userRepository.save(admin);

                // Create Employer
                User employer1 = new User();
                employer1.setFullName("TechCorp Inc.");
                employer1.setEmail("employer1@techcorp.com");
                employer1.setPassword(passwordEncoder.encode("pass123"));
                employer1.setRole(Role.EMPLOYER);
                employer1.setLocation("San Francisco, CA");
                userRepository.save(employer1);

                // Create Student
                User student1 = new User();
                student1.setFullName("John Doe");
                student1.setEmail("student1@email.com");
                student1.setPassword(passwordEncoder.encode("pass123"));
                student1.setRole(Role.STUDENT);
                student1.setSkills("Java, Spring Boot, React");
                student1.setLocation("New York, NY");
                userRepository.save(student1);

                // Create Job
                Job job1 = new Job();
                job1.setTitle("Senior Java Developer");
                job1.setDescription("Looking for an experienced Java developer with Spring Boot knowledge.");
                job1.setCompany(employer1.getFullName());
                job1.setLocation("Remote");
                job1.setCategory("Software Development");
                job1.setSkillsRequired("Java, Spring Boot, Microservices");
                job1.setExperienceLevel("Senior (5+ years)");
                job1.setSalaryMin(new BigDecimal("120000"));
                job1.setSalaryMax(new BigDecimal("160000"));
                job1.setStatus(JobStatus.ACTIVE);
                job1.setEmployer(employer1);
                job1.setDeadline(LocalDateTime.now().plusDays(30));
                jobRepository.save(job1);
                
                // Create another Job
                Job job2 = new Job();
                job2.setTitle("Frontend Specialist");
                job2.setDescription("Create beautiful user interfaces using React and modern CSS.");
                job2.setCompany("DesignX");
                job2.setLocation("San Francisco, CA");
                job2.setCategory("Design & UI/UX");
                job2.setSkillsRequired("React, CSS, Figma");
                job2.setExperienceLevel("Mid-level (3+ years)");
                job2.setSalaryMin(new BigDecimal("90000"));
                job2.setSalaryMax(new BigDecimal("130000"));
                job2.setStatus(JobStatus.ACTIVE);
                job2.setEmployer(employer1);
                job2.setDeadline(LocalDateTime.now().plusDays(15));
                jobRepository.save(job2);

                Job job3 = new Job();
                job3.setTitle("Backend Engineer");
                job3.setDescription("We're looking for a strong Backend Engineer proficient in Python and Django to build scalable APIs.");
                job3.setCompany("CloudSystems");
                job3.setLocation("Austin, TX");
                job3.setCategory("Software Development");
                job3.setSkillsRequired("Python, Django, PostgreSQL, AWS");
                job3.setExperienceLevel("Mid-level (3-5 years)");
                job3.setSalaryMin(new BigDecimal("100000"));
                job3.setSalaryMax(new BigDecimal("145000"));
                job3.setStatus(JobStatus.ACTIVE);
                job3.setEmployer(employer1);
                job3.setDeadline(LocalDateTime.now().plusDays(20));
                jobRepository.save(job3);

                Job job4 = new Job();
                job4.setTitle("Product Manager");
                job4.setDescription("Lead cross-functional teams to deliver enterprise SaaS products. Strong agile background required.");
                job4.setCompany("TechCorp Inc.");
                job4.setLocation("Remote");
                job4.setCategory("Product");
                job4.setSkillsRequired("Agile, Jira, Roadmap Planning, Communication");
                job4.setExperienceLevel("Senior (5+ years)");
                job4.setSalaryMin(new BigDecimal("130000"));
                job4.setSalaryMax(new BigDecimal("180000"));
                job4.setStatus(JobStatus.ACTIVE);
                job4.setEmployer(employer1);
                job4.setDeadline(LocalDateTime.now().plusDays(10));
                jobRepository.save(job4);

                Job job5 = new Job();
                job5.setTitle("Data Analyst");
                job5.setDescription("Analyze large datasets to drive business decisions. Experience with SQL and Tableau is essential.");
                job5.setCompany("DataMinds Labs");
                job5.setLocation("New York, NY");
                job5.setCategory("Data");
                job5.setSkillsRequired("SQL, Tableau, Python, Excel");
                job5.setExperienceLevel("Entry-level (1-2 years)");
                job5.setSalaryMin(new BigDecimal("70000"));
                job5.setSalaryMax(new BigDecimal("95000"));
                job5.setStatus(JobStatus.ACTIVE);
                job5.setEmployer(employer1);
                job5.setDeadline(LocalDateTime.now().plusDays(25));
                jobRepository.save(job5);

                Job job6 = new Job();
                job6.setTitle("UX Researcher");
                job6.setDescription("Conduct user interviews and usability testing to help shape our product design.");
                job6.setCompany("DesignX");
                job6.setLocation("London, UK (Hybrid)");
                job6.setCategory("Design & UI/UX");
                job6.setSkillsRequired("User Interviews, Usability Testing, Figma");
                job6.setExperienceLevel("Mid-level (3+ years)");
                job6.setStatus(JobStatus.ACTIVE);
                job6.setEmployer(employer1);
                job6.setDeadline(LocalDateTime.now().plusDays(14));
                jobRepository.save(job6);

                Job job7 = new Job();
                job7.setTitle("DevOps Engineer");
                job7.setDescription("Manage cloud infrastructure, CI/CD pipelines, and Kubernetes clusters.");
                job7.setCompany("CloudSystems");
                job7.setLocation("Remote");
                job7.setCategory("Operations");
                job7.setSkillsRequired("AWS, Kubernetes, Terraform, Docker, CI/CD");
                job7.setExperienceLevel("Mid-level (3-5 years)");
                job7.setSalaryMin(new BigDecimal("115000"));
                job7.setSalaryMax(new BigDecimal("155000"));
                job7.setStatus(JobStatus.ACTIVE);
                job7.setEmployer(employer1);
                job7.setDeadline(LocalDateTime.now().plusDays(18));
                jobRepository.save(job7);

                Job job8 = new Job();
                job8.setTitle("Digital Marketing Specialist");
                job8.setDescription("Run paid ad campaigns, optimize SEO, and manage social media channels.");
                job8.setCompany("MarketBoost");
                job8.setLocation("Chicago, IL");
                job8.setCategory("Marketing");
                job8.setSkillsRequired("Google Ads, SEO, Social Media Management");
                job8.setExperienceLevel("Entry-level (1-3 years)");
                job8.setSalaryMin(new BigDecimal("60000"));
                job8.setSalaryMax(new BigDecimal("80000"));
                job8.setStatus(JobStatus.ACTIVE);
                job8.setEmployer(employer1);
                job8.setDeadline(LocalDateTime.now().plusDays(40));
                jobRepository.save(job8);

                Job job9 = new Job();
                job9.setTitle("Full Stack Developer");
                job9.setDescription("End-to-end development using React and Node.js. Join a fast-paced startup environment.");
                job9.setCompany("StartupGen");
                job9.setLocation("Berlin, Germany");
                job9.setCategory("Software Development");
                job9.setSkillsRequired("React, Node.js, MongoDB, Express");
                job9.setExperienceLevel("Mid-level (3+ years)");
                job9.setSalaryMin(new BigDecimal("80000"));
                job9.setSalaryMax(new BigDecimal("120000"));
                job9.setStatus(JobStatus.ACTIVE);
                job9.setEmployer(employer1);
                job9.setDeadline(LocalDateTime.now().plusDays(21));
                jobRepository.save(job9);

                Job job10 = new Job();
                job10.setTitle("HR Business Partner");
                job10.setDescription("Lead talent acquisition, employee relations, and performance management initiatives.");
                job10.setCompany("TechCorp Inc.");
                job10.setLocation("San Francisco, CA");
                job10.setCategory("Human Resources");
                job10.setSkillsRequired("Talent Acquisition, Employee Relations, Communication");
                job10.setExperienceLevel("Senior (5+ years)");
                job10.setSalaryMin(new BigDecimal("95000"));
                job10.setSalaryMax(new BigDecimal("135000"));
                job10.setStatus(JobStatus.ACTIVE);
                job10.setEmployer(employer1);
                job10.setDeadline(LocalDateTime.now().plusDays(12));
                jobRepository.save(job10);

                // Create Application
                Application app1 = new Application();
                app1.setUser(student1);
                app1.setJob(job1);
                app1.setCoverLetter("I am very interested in this Senior Java Developer position.");
                app1.setStatus(ApplicationStatus.PENDING);
                applicationRepository.save(app1);
            }
        };
    }
}
