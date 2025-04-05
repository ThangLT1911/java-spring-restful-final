package vn.thanglt.jobhunter.controller;

import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.thanglt.jobhunter.domain.Skill;
import vn.thanglt.jobhunter.domain.response.ResultPaginationDTO;
import vn.thanglt.jobhunter.service.SkillService;
import vn.thanglt.jobhunter.util.annotation.ApiMessage;
import vn.thanglt.jobhunter.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1")
public class SkillController {
    private final SkillService skillService;


    public SkillController(SkillService service, SkillService skillService) {
        this.skillService = skillService;
    }


    @PostMapping("/skills")
    @ApiMessage("Create a new skill")
    public ResponseEntity<Skill> handleCreateSkill(@Valid @RequestBody Skill skill) throws IdInvalidException {
        if (this.skillService.existsByName(skill.getName())) {
            throw new IdInvalidException("Skill " + skill.getName() + " da ton tai");
        }
        Skill newSkill = this.skillService.handleCreateSkill(skill);
        return ResponseEntity.status(HttpStatus.CREATED).body(newSkill);
    }

    @PutMapping("/skills")
    @ApiMessage("Update a new skill")
    public ResponseEntity<Skill> handleUpdateSkill(@Valid @RequestBody Skill updateSkill) throws IdInvalidException {
        // check id
        Skill currentSkill = this.skillService.handleGetSkillById(updateSkill.getId());
        if (currentSkill == null) {
            throw new IdInvalidException("Skill id = " + updateSkill.getId() + " không tồn tại");
        }

        // check name
        if (updateSkill.getName() != null && this.skillService.existsByName(updateSkill.getName())) {
            throw new IdInvalidException("Skill name = " + updateSkill.getName() + " đã tồn tại");
        }

        currentSkill.setName(updateSkill.getName());
        return ResponseEntity.ok().body(this.skillService.handleUpdateSkill(currentSkill));
    }

    @GetMapping("/skills/{id}")
    public ResponseEntity<Skill> getSkill(@PathVariable("id") long id) throws IdInvalidException {
        Skill getSkill = this.skillService.handleGetSkillById(id);
        if (getSkill == null) {
            throw new IdInvalidException("id khong ton tai");
        }
        return ResponseEntity.ok(getSkill);
    }

    @GetMapping("/skills")
    @ApiMessage("Get all skill")
    public ResponseEntity<ResultPaginationDTO> getAllSkill(@Filter Specification<Skill> spec, Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(this.skillService.handleGetAllSkill(spec, pageable));
    }

    @DeleteMapping("/skills/{id}")
    @ApiMessage("Delete a skill")
    public ResponseEntity<Void> deleteSkill(@PathVariable("id") long id) throws IdInvalidException {
        Skill currentSkill = this.skillService.handleGetSkillById(id);
        if (currentSkill == null) {
            throw new IdInvalidException("Skill id " + id + " khong ton tai");
        }
        this.skillService.handleDeleteSkill(id);
        return ResponseEntity.ok(null);
    }
}
