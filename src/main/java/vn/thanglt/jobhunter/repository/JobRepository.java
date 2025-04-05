package vn.thanglt.jobhunter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import vn.thanglt.jobhunter.domain.Job;
import vn.thanglt.jobhunter.domain.Skill;

import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job, Long>, JpaSpecificationExecutor<Job> {
    boolean existsByName(String name);
    List<Job> findBySkillsIn(List<Skill> skills);
}
