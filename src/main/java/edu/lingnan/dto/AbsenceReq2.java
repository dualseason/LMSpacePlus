package edu.lingnan.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
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
@TableName("absence")
@JsonIgnoreProperties(ignoreUnknown = true)
public class AbsenceReq2 {


    @ApiModelProperty(name = "bId",value = "预约编号",required = true)
    @JsonProperty("bId")
    private Integer bId;


    @ApiModelProperty(name = "todayStatus",value = "当天状态 true:未缺勤，false：缺勤",required = true)
    @JsonProperty("todayStatus")
    private String todayStatus;
}
