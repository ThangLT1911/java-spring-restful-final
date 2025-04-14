package vn.thanglt.jobhunter.domain.response.resume;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.thanglt.jobhunter.util.constant.StatusEnum;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResCreateResumeDTO {
    private long id;
    private StatusEnum status;
    private Instant createdAt;
    private String createdBy;
}
