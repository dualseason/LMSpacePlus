package edu.lingnan.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class StudentReq {
    @ApiModelProperty(name = "sId",value = "学号",required = true)
    @JsonProperty("sId")
    private String sId;

    @ApiModelProperty(name = "rId",value = "教室编号",required = true)
    @JsonProperty("rId")
    private Integer rId;

    @JsonProperty("bStartTime")
    @ApiModelProperty(name = "bStartTime",value = "预约开始时间")
    private String bStartTime;

    @JsonProperty("bEndTime")
    @ApiModelProperty(name = "bEndTime",value = "预约结束时间")
    private String bEndTime;
}
