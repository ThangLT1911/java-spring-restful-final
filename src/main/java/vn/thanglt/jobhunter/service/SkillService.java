package vn.thanglt.jobhunter.service;

import jakarta.validation.Valid;
import org.springframework.data.domain.Limit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.thanglt.jobhunter.domain.Skill;
import vn.thanglt.jobhunter.domain.response.ResSkillDTO;
import vn.thanglt.jobhunter.domain.response.ResultPaginationDTO;
import vn.thanglt.jobhunter.repository.SkillRepository;
import vn.thanglt.jobhunter.util.error.IdInvalidException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SkillService {
    private final SkillRepository skillRepository;

    public SkillService(SkillRepository skillRepository) {
        this.skillRepository = skillRepository;
    }

    public Skill handleCreateSkill(@Valid Skill skill) {
        return this.skillRepository.save(skill);
    }

    public boolean existsByName(String name) {
        return this.skillRepository.existsByName(name);
    }

    public Skill handleGetSkillById(long id) {
        Optional<Skill> skillOptional = this.skillRepository.findById(id);
        return skillOptional.orElse(null);
    }

    public Skill handleUpdateSkill(Skill skill) throws IdInvalidException {
        Skill currentSkill = this.handleGetSkillById(skill.getId());
        if(currentSkill != null) {
            currentSkill.setName(skill.getName());
            currentSkill.setUpdatedAt(skill.getUpdatedAt());
            currentSkill.setUpdatedBy(skill.getUpdatedBy());

            currentSkill = this.skillRepository.save(currentSkill);
        } else {
            throw new IdInvalidException("Id " + skill.getId() + " khong ton tai");
        }

        return currentSkill;
    }

    public ResultPaginationDTO handleGetAllSkill(Specification<Skill> specification, Pageable pageable) {
        Page<Skill> pageSkill = this.skillRepository.findAll(specification, pageable);

        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();

        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());

        meta.setPages(pageSkill.getTotalPages());
        meta.setTotal(pageSkill.getTotalElements());

        rs.setMeta(meta);

        List<ResSkillDTO> listSkill = pageSkill.getContent()
                .stream().map(this::convertToResSkillDTO)
                .collect(Collectors.toList());

        rs.setResult(listSkill);

        return rs;
    }

    public ResSkillDTO convertToResSkillDTO(Skill skill) {
        ResSkillDTO res = new ResSkillDTO();

        res.setId(skill.getId());
        res.setName(skill.getName());
        res.setCreatedAt(skill.getCreatedAt());
        res.setCreatedBy(skill.getCreatedBy());
        res.setUpdatedAt(skill.getUpdatedAt());
        res.setUpdatedBy(skill.getUpdatedBy());

        return res;
    }

    public void handleDeleteSkill(long id) {
        Optional<Skill> skillOptional = this.skillRepository.findById(id);
        Skill currentSkill = skillOptional.get();
        currentSkill.getJobs().forEach(job -> job.getSkills().remove(currentSkill));

        this.skillRepository.deleteById(id);
    }
}
