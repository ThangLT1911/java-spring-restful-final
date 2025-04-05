package vn.thanglt.jobhunter.controller;

import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.thanglt.jobhunter.domain.Job;
import vn.thanglt.jobhunter.domain.response.ResultPaginationDTO;
import vn.thanglt.jobhunter.domain.response.job.ResCreateJobDTO;
import vn.thanglt.jobhunter.domain.response.job.ResUpdateJobDTO;
import vn.thanglt.jobhunter.service.JobService;
import vn.thanglt.jobhunter.util.annotation.ApiMessage;
import vn.thanglt.jobhunter.util.error.IdInvalidException;

import java.util.Optional;

@RestController
@RequestMapping("api/v1")
public class JobController {
    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @PostMapping("/jobs")
    @ApiMessage("Create a new job")
    public ResponseEntity<ResCreateJobDTO> createNewJob(@Valid @RequestBody Job job) throws IdInvalidException {
        boolean isJobExist = this.jobService.checkJobExistByName(job.getName());
        if (isJobExist) {
            throw new IdInvalidException("Job " + job.getName() + " da ton tai");
        }
        ResCreateJobDTO newJob = this.jobService.handleCreateJob(job);
        return ResponseEntity.status(HttpStatus.CREATED).body(newJob);
    }

    @PutMapping("/jobs")
    @ApiMessage("Update job")
    public ResponseEntity<ResUpdateJobDTO> updateJob(@Valid @RequestBody Job job) throws IdInvalidException {
        Optional<Job> currentJob = this.jobService.fetchJobById(job.getId());
        if (currentJob.isEmpty()) {
            throw new IdInvalidException("Job not found");
        }
        return ResponseEntity.ok().body(this.jobService.handleUpdateJob(job));
    }

    @DeleteMapping("/jobs/{id}")
    @ApiMessage("Delete a skill")
    public ResponseEntity<Void> deleteJob(@PathVariable("id") long id) throws IdInvalidException {
        Optional<Job> currentJob = this.jobService.fetchJobById(id);
        if (currentJob.isEmpty()) {
            throw new IdInvalidException("Job not found");
        }
        this.jobService.deleteJob(id);
        return ResponseEntity.ok(null);
    }

    @GetMapping("/jobs/{id}")
    @ApiMessage("Get Skill")
    public ResponseEntity<Job> getJob(@PathVariable("id") long id) throws IdInvalidException {
        Optional<Job> getJob = this.jobService.fetchJobById(id);
        if (getJob.isEmpty()) {
            throw new IdInvalidException("Job khong ton tai");
        }
        return ResponseEntity.ok().body(getJob.get());
    }

    @GetMapping("/jobs")
    @ApiMessage("Get all job")
    public ResponseEntity<ResultPaginationDTO> getAllJob(@Filter Specification<Job> spec, Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(this.jobService.handleGetAllJob(spec, pageable));
    }
}
