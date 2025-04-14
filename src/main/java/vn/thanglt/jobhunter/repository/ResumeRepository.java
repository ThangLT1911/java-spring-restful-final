package vn.thanglt.jobhunter.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import vn.thanglt.jobhunter.domain.Resume;
import vn.thanglt.jobhunter.domain.Skill;
import vn.thanglt.jobhunter.domain.response.ResultPaginationDTO;

import java.awt.print.Pageable;
import java.util.Optional;

public interface ResumeRepository extends JpaRepository<Resume, Long>, JpaSpecificationExecutor<Resume> {
    Optional<Resume> findById(long id);

}
