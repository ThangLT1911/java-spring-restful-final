package vn.thanglt.jobhunter.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.thanglt.jobhunter.domain.Company;
import vn.thanglt.jobhunter.domain.Job;
import vn.thanglt.jobhunter.domain.Role;
import vn.thanglt.jobhunter.domain.Skill;
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

    public Job fetchJobById(long id) {
        Optional<Job> jobOptional = this.jobRepository.findById(id);
        return jobOptional.orElse(null);
    }

    public ResCreateJobDTO handleCreateJob(Job job) {
        // check skill
        if (job.getSkills() != null) {
            // lay list id skill
            List<Long> reqSkills = job.getSkills()
                    .stream().map(x -> x.getId())
                    .collect(Collectors.toList());
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

        ResCreateJobDTO res = new ResCreateJobDTO();
        res.setId(currentJob.getId());
        res.setName(currentJob.getName());
        res.setLocation(currentJob.getLocation());
        res.setSalary(currentJob.getSalary());
        res.setQuantity(currentJob.getQuantity());
        res.setLevel(currentJob.getLevel());
        res.setStartDate(currentJob.getStartDate());
        res.setEndDate(currentJob.getEndDate());
        res.setActive(currentJob.isActive());
        res.setCreatedAt(currentJob.getCreatedAt());
        res.setCreatedBy(currentJob.getCreatedBy());

        if (currentJob.getSkills() != null) {
            List<String> skills = currentJob.getSkills()
                    .stream().map(item -> item.getName())
                    .collect(Collectors.toList());
            res.setSkills(skills);
        }

        return res;
    }

    public ResUpdateJobDTO handleUpdateJob(Job job, Job jobInDb) {
        // check job
        if (job.getSkills() != null) {
            List<Long> reqSkills = job.getSkills().stream().map(Skill::getId).toList();

            List<Skill> dbSkill = this.skillRepository.findByIdIn(reqSkills);
            jobInDb.setSkills(dbSkill);
        }

        // check company
        if (job.getCompany() != null) {
            Optional<Company> companyOptional = this.companyRepository.findById(job.getCompany().getId());
            if (companyOptional.isPresent()) {
                jobInDb.setCompany(companyOptional.get());
            }
        }

        // update correct info
        jobInDb.setName(job.getName());
        jobInDb.setSalary(job.getSalary());
        jobInDb.setQuantity(job.getQuantity());
        jobInDb.setLocation(job.getLocation());
        jobInDb.setLevel(job.getLevel());
        jobInDb.setStartDate(job.getStartDate());
        jobInDb.setEndDate(job.getEndDate());
        jobInDb.setActive(job.isActive());

        // update job
        Job currentJob = this.jobRepository.save(jobInDb);

        // convert response
        ResUpdateJobDTO dto = new ResUpdateJobDTO();
        dto.setId(currentJob.getId());
        dto.setName(currentJob.getName());
        dto.setSalary(currentJob.getSalary());
        dto.setQuantity(currentJob.getQuantity());
        dto.setLocation(currentJob.getLocation());
        dto.setLevel(currentJob.getLevel());
        dto.setStartDate(currentJob.getStartDate());
        dto.setEndDate(currentJob.getEndDate());
        dto.setActive(currentJob.isActive());
        dto.setUpdatedAt(currentJob.getUpdatedAt());
        dto.setUpdatedBy(currentJob.getUpdatedBy());

        if (currentJob.getSkills() != null) {
            List<String> skills = currentJob.getSkills().stream().map(Skill::getName).toList();
            dto.setSkills(skills);
        }

        return dto;
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
