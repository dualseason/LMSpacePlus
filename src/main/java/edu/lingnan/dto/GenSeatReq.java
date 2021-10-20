package edu.lingnan.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class GenSeatReq {
    @JsonProperty("rId")
    private Integer rId;
    @JsonProperty("sId")
    private String sId;
    @JsonProperty("days")
    private Integer days;
}
