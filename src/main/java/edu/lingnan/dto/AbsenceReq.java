package edu.lingnan.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.lingnan.entity.Seat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AbsenceReq extends Seat {
    @JsonProperty("absence")
    private Boolean absence;
    @JsonProperty("aTime")
    private String aTime;
}
