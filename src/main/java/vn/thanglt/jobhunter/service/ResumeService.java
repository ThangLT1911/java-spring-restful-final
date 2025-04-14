package vn.thanglt.jobhunter.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.thanglt.jobhunter.domain.Job;
import vn.thanglt.jobhunter.domain.Resume;
import vn.thanglt.jobhunter.domain.User;
import vn.thanglt.jobhunter.domain.response.ResultPaginationDTO;
import vn.thanglt.jobhunter.domain.response.resume.ResCreateResumeDTO;
import vn.thanglt.jobhunter.domain.response.resume.ResFetchResumeDTO;
import vn.thanglt.jobhunter.domain.response.resume.ResUpdateResumeDTO;
import vn.thanglt.jobhunter.repository.JobRepository;
import vn.thanglt.jobhunter.repository.ResumeRepository;
import vn.thanglt.jobhunter.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ResumeService {
    private final ResumeRepository resumeRepository;
    private final JobRepository jobRepository;
    private final UserRepository userRepository;

    public ResumeService(ResumeRepository resumeRepository, JobRepository jobRepository, UserRepository userRepository) {
        this.resumeRepository = resumeRepository;
        this.jobRepository = jobRepository;
        this.userRepository = userRepository;
    }

    public Optional<Resume> fetchById(long id) {
        return this.resumeRepository.findById(id);
    }

    public boolean checkResumeExistByUserAndJob(Resume resume) {
        // check user by id
        if (resume.getUser() == null) {
            return false;
        }
        Optional<User> userOptional = this.userRepository.findById(resume.getUser().getId());
        if (userOptional.isEmpty()) {
            return false;
        }

        // check job by id
        if (resume.getJob() == null) {
            return false;
        }
        Optional<Job> jobOptional = this.jobRepository.findById(resume.getJob().getId());
        if (jobOptional.isEmpty()) {
            return false;
        }

        return true;
    }

    public ResCreateResumeDTO handleCreateResume(Resume resume) {
        resume = this.resumeRepository.save(resume);
        ResCreateResumeDTO res = new ResCreateResumeDTO();

        res.setId(resume.getId());
        res.setStatus(resume.getStatus());
        res.setCreatedBy(resume.getCreatedBy());
        res.setCreatedAt(resume.getCreatedAt());

        return res;
    }

    public ResUpdateResumeDTO handleUpdateResume(Resume resume) {
        resume = this.resumeRepository.save(resume);
        ResUpdateResumeDTO res = new ResUpdateResumeDTO();
        res.setUpdatedAt(resume.getUpdatedAt());
        res.setUpdatedBy(resume.getUpdatedBy());
        return res;
    }

    public void handleDeleteResume(long id) {
        this.resumeRepository.deleteById(id);
    }

    public ResultPaginationDTO handleGetAllResume(Specification<Resume> specification, Pageable pageable) {
        Page<Resume> pageResume = this.resumeRepository.findAll(specification, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();

        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());

        meta.setPages(pageResume.getTotalPages());
        meta.setTotal(pageResume.getTotalElements());

        rs.setMeta(meta);

        List<ResFetchResumeDTO> listResume = pageResume.getContent()
                .stream().map(this::convertToResResumeDTO)
                .toList();

        rs.setResult(listResume);

        return rs;
    }

    public ResFetchResumeDTO convertToResResumeDTO(Resume resume) {
        ResFetchResumeDTO res = new ResFetchResumeDTO();

        res.setId(resume.getId());
        res.setEmail(resume.getEmail());
        res.setUrl(resume.getUrl());
        res.setStatus(resume.getStatus());
        res.setCreatedAt(resume.getCreatedAt());
        res.setUpdatedAt(resume.getUpdatedAt());
        res.setCreatedBy(resume.getCreatedBy());
        res.setUpdatedBy(resume.getUpdatedBy());

        if (resume.getJob() != null) {
            res.setCompanyName(resume.getJob().getCompany().getName());
        }

        res.setUser(new ResFetchResumeDTO.UserResume(resume.getUser().getId(), resume.getUser().getName()));
        res.setJob(new ResFetchResumeDTO.JobResume(resume.getJob().getId(), resume.getJob().getName()));

        return res;
    }
}
