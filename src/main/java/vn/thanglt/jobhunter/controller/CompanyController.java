package vn.thanglt.jobhunter.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.thanglt.jobhunter.domain.Company;
import vn.thanglt.jobhunter.service.CompanyService;
import vn.thanglt.jobhunter.util.error.IdInvalidException;

import java.util.List;

@RestController
public class CompanyController {
    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @PostMapping("/companies")
    public ResponseEntity<Company> createNewCompany(@Valid @RequestBody Company postmanCompany) {
        Company newCompany = this.companyService.handleCreateCompany(postmanCompany);
        return ResponseEntity.status(HttpStatus.CREATED).body(newCompany);
    }

    @GetMapping("/companies/{id}")
    public ResponseEntity<Company> getCompany(@PathVariable("id") long id) {
        Company getCompany = this.companyService.handleGetCompanyById(id);
        return ResponseEntity.ok(getCompany);
    }

    @GetMapping("/companies")
    public ResponseEntity<List<Company>> getAllCompany(Company company) {
        return ResponseEntity.ok(this.companyService.handleGetAllCompany(company));
    }

    @PutMapping("/companies")
    public ResponseEntity<Company> updateCompany(@Valid @RequestBody Company updateCompany) {
        Company updateCompanys = this.companyService.handleUpdateCompany(updateCompany);
        return ResponseEntity.ok(updateCompanys);
    }

    @DeleteMapping("/companies/{id}")
    public ResponseEntity<String> deleteCompany(@PathVariable("id") long id) throws IdInvalidException {
        if(id >= 1500) {
            throw new IdInvalidException("id khong lon hon hoac bang 1500");
        }
        this.companyService.handleDeleteCompany(id);
        return ResponseEntity.ok("id: " + id);
    }
}
