package edu.lingnan.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("student")
@JsonIgnoreProperties(ignoreUnknown = true)
public class StudentCheckReq {
    @ApiModelProperty(name = "sId",value = "学号",required = true)
    @JsonProperty("sId")
    @TableField("s_id")
    private String sId;

    @ApiModelProperty(name = "sStatus",value = "学生状态 1代表通过 0代表不通过",required = true)
    @TableField("s_status")
    @JsonProperty("sStatus")
    private String sStatus;

}
