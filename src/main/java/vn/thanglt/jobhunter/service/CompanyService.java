package vn.thanglt.jobhunter.service;

import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import vn.thanglt.jobhunter.domain.Company;
import vn.thanglt.jobhunter.repository.CompanyRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyService {
    private final CompanyRepository companyRepository;

    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public Company handleCreateCompany(@Valid Company company) {
        return this.companyRepository.save(company);
    }

    public List<Company> handleGetAllCompany(Company company) {
        return this.companyRepository.findAll();
    }

    public void handleDeleteCompany(long id) {
        this.companyRepository.deleteById(id);
    }

    public Company handleGetCompanyById(long id) {
        Optional<Company> companyOptional = this.companyRepository.findById(id);
        return companyOptional.orElse(null);
    }

    public Company handleUpdateCompany(Company c) {
        Company currentCompany = this.handleGetCompanyById(c.getId());
        if(currentCompany != null) {
            currentCompany.setName(c.getName());
            currentCompany.setDescription(c.getDescription());
            currentCompany.setAddress(c.getAddress());
            currentCompany.setLogo(c.getLogo());

            currentCompany = this.companyRepository.save(currentCompany);
        }

        return currentCompany;
    }

}
