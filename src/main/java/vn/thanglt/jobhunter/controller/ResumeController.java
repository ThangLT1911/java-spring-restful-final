package vn.thanglt.jobhunter.controller;

import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.thanglt.jobhunter.domain.Resume;
import vn.thanglt.jobhunter.domain.response.ResultPaginationDTO;
import vn.thanglt.jobhunter.domain.response.resume.ResCreateResumeDTO;
import vn.thanglt.jobhunter.domain.response.resume.ResFetchResumeDTO;
import vn.thanglt.jobhunter.domain.response.resume.ResUpdateResumeDTO;
import vn.thanglt.jobhunter.service.ResumeService;
import vn.thanglt.jobhunter.util.annotation.ApiMessage;
import vn.thanglt.jobhunter.util.error.IdInvalidException;

import java.util.Optional;

@RestController
@RequestMapping("api/v1")
public class ResumeController {
    private final ResumeService resumeService;

    public ResumeController(ResumeService resumeService) {
        this.resumeService = resumeService;
    }

    @PostMapping("/resumes")
    @ApiMessage("Create a new CV")
    public ResponseEntity<ResCreateResumeDTO> createNewResume(@Valid @RequestBody Resume resume) throws IdInvalidException {
        boolean checkExist = this.resumeService.checkResumeExistByUserAndJob(resume);
        if (!checkExist) {
            throw new IdInvalidException("User/job khong ton tai");
        }
        ResCreateResumeDTO newResume = this.resumeService.handleCreateResume(resume);
        return ResponseEntity.status(HttpStatus.CREATED).body(newResume);
    }

    @PutMapping("/resumes")
    @ApiMessage("Update resume")
    public ResponseEntity<ResUpdateResumeDTO> updateResume(@RequestBody Resume resume) throws IdInvalidException {
        Optional<Resume> reqResumeOptional = this.resumeService.fetchById(resume.getId());
        if (reqResumeOptional.isEmpty()) {
            throw new IdInvalidException("Resume not exist");
        }

        Resume reqResume = reqResumeOptional.get();
        reqResume.setStatus(resume.getStatus());

        return ResponseEntity.ok().body(this.resumeService.handleUpdateResume(reqResume));
    }

    @DeleteMapping("resumes/{id}")
    @ApiMessage("Delete a resume")
    public ResponseEntity<String> deleteResume(@PathVariable("id") long id) throws IdInvalidException {
        Optional<Resume> reqResumeOptional = this.resumeService.fetchById(id);
        if (reqResumeOptional.isEmpty()) {
            throw new IdInvalidException("Resume co id: " + id + " khong ton tai");
        }

        this.resumeService.handleDeleteResume(id);
        return ResponseEntity.ok("id: " + id);
    }

    @GetMapping("/resumes/{id}")
    @ApiMessage("Get resume by id")
    public ResponseEntity<ResFetchResumeDTO> getResume(@PathVariable("id") long id) throws IdInvalidException {
        Optional<Resume> reqResumeOptional = this.resumeService.fetchById(id);
        if (reqResumeOptional.isEmpty()) {
            throw new IdInvalidException("Id khong ton tai");
        }
        return ResponseEntity.ok().body(this.resumeService.convertToResResumeDTO(reqResumeOptional.get()));
    }

    @GetMapping("/resumes")
    @ApiMessage("Get all resume")
    public ResponseEntity<ResultPaginationDTO> getAllResume(@Filter Specification<Resume> spec, Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(this.resumeService.handleGetAllResume(spec, pageable));
    }
}
