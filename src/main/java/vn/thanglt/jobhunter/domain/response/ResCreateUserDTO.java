package vn.thanglt.jobhunter.domain.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import vn.thanglt.jobhunter.domain.Company;
import vn.thanglt.jobhunter.util.constant.GenderEnum;

import java.time.Instant;

@Getter
@Setter
public class ResCreateUserDTO {
    private long id;
    private String name;
    private String email;
    private GenderEnum gender;
    private String address;
    private int age;
    private Instant createdAt;
    private CompanyUser company;


    @Getter
    @Setter
    public static class CompanyUser {
        private long id;
        private String name;
    }
}
