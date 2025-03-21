package vn.thanglt.jobhunter.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.PreUpdate;
import lombok.Getter;
import lombok.Setter;
import vn.thanglt.jobhunter.util.SecurityUtil;
import vn.thanglt.jobhunter.util.constant.GenderEnum;

import java.time.Instant;

@Getter
@Setter
public class ResUpdateUserDTO {
    private long id;
    private String name;
    private GenderEnum gender;
    private String address;
    private int age;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss a", timezone = "GMT+7")
    private Instant updatedAt;



}
