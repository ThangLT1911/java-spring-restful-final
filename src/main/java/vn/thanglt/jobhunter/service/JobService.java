package vn.thanglt.jobhunter.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.thanglt.jobhunter.domain.Company;
import vn.thanglt.jobhunter.domain.Job;
import vn.thanglt.jobhunter.domain.Skill;
import vn.thanglt.jobhunter.domain.response.ResSkillDTO;
import vn.thanglt.jobhunter.domain.response.ResultPaginationDTO;
import vn.thanglt.jobhunter.domain.response.job.ResCreateJobDTO;
import vn.thanglt.jobhunter.domain.response.job.ResUpdateJobDTO;
import vn.thanglt.jobhunter.repository.CompanyRepository;
import vn.thanglt.jobhunter.repository.JobRepository;
import vn.thanglt.jobhunter.repository.SkillRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class JobService {
    private final JobRepository jobRepository;
    private final CompanyRepository companyRepository;
    private final SkillRepository skillRepository;

    public JobService(JobRepository jobRepository, CompanyRepository companyRepository,
            SkillRepository skillRepository) {
        this.jobRepository = jobRepository;
        this.companyRepository = companyRepository;
        this.skillRepository = skillRepository;
    }

    public boolean checkJobExistByName(String name) {
        return this.jobRepository.existsByName(name);
    }

    public boolean checkCompanyExistById(long id) {
        return this.companyRepository.existsById(id);
    }

    public Optional<Job> fetchJobById(long id) {
        return this.jobRepository.findById(id);
    }

    public ResCreateJobDTO handleCreateJob(Job job) {
        ResCreateJobDTO res = new ResCreateJobDTO();

        // check skill
        if (job.getSkills() != null) {
            // lay list id skill
            List<Long> reqSkills = job.getSkills().stream().map(Skill::getId).toList();

            List<Skill> dbSkills = this.skillRepository.findByIdIn(reqSkills);
            job.setSkills(dbSkills);
        }

        // check company
        if (job.getCompany() != null) {
            Optional<Company> companyOptional = this.companyRepository.findById(job.getCompany().getId());
            if (companyOptional.isPresent()) {
                job.setCompany(companyOptional.get());
            }
        }

        // create job
        Job currentJob = this.jobRepository.save(job);

        res.setId(currentJob.getId());
        res.setName(currentJob.getName());
        res.setLocation(currentJob.getLocation());
        res.setSalary(currentJob.getSalary());
        res.setQuantity(currentJob.getQuantity());
        res.setLevel(currentJob.getLevel());
        res.setEndDate(currentJob.getEndDate());
        res.setActive(currentJob.isActive());
        res.setCreatedAt(currentJob.getCreatedAt());
        res.setCreatedBy(currentJob.getCreatedBy());

        if (currentJob.getSkills() != null) {
            List<String> skills = currentJob.getSkills().stream().map(Skill::getName).toList();
            res.setSkills(skills);
        }

        return res;
    }

    public ResUpdateJobDTO handleUpdateJob(Job job) {
        Optional<Job> existingJob = this.jobRepository.findById(job.getId());
        if (existingJob.isPresent()) {
            // Giữ lại các giá trị created
            job.setCreatedAt(existingJob.get().getCreatedAt());
            job.setCreatedBy(existingJob.get().getCreatedBy());
        }

        if (job.getSkills() != null) {
            List<Long> reqSkills = job.getSkills().stream().map(Skill::getId).toList();

            List<Skill> dbSkill = this.skillRepository.findByIdIn(reqSkills);
            job.setSkills(dbSkill);
        }

        // update job
        Job currentJob = this.jobRepository.save(job);

        // convert response
        ResUpdateJobDTO res = new ResUpdateJobDTO();
        res.setId(currentJob.getId());
        res.setName(currentJob.getName());
        res.setLocation(currentJob.getLocation());
        res.setSalary(currentJob.getSalary());
        res.setQuantity(currentJob.getQuantity());
        res.setLevel(currentJob.getLevel());
        res.setEndDate(currentJob.getEndDate());
        res.setActive(currentJob.isActive());
        res.setUpdatedAt(currentJob.getUpdatedAt());
        res.setCreatedAt(currentJob.getCreatedAt());
        res.setUpdatedBy(currentJob.getUpdatedBy());
        res.setUpdatedAt(currentJob.getUpdatedAt());

        if (currentJob.getSkills() != null) {
            List<String> skills = currentJob.getSkills().stream().map(Skill::getName).toList();
            res.setSkills(skills);
        }

        return res;
    }

    public void deleteJob(long id) {
        this.jobRepository.deleteById(id);
    }

    public ResultPaginationDTO handleGetAllJob(Specification<Job> specification, Pageable pageable) {
        Page<Job> pageJob = this.jobRepository.findAll(specification, pageable);

        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();

        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());

        meta.setPages(pageJob.getTotalPages());
        meta.setTotal(pageJob.getTotalElements());

        rs.setMeta(meta);

        rs.setResult(pageJob.getContent());

        return rs;
    }
}
