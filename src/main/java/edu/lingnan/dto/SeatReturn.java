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
@JsonIgnoreProperties(ignoreUnknown = true)
public class SeatReturn {
    @ApiModelProperty(name = "bId",value = "预约编号",required = true)
    @JsonProperty("bId")
    private Integer bId;


    @ApiModelProperty(name = "bEndTime",value = "归还时间，如果没有代表直接退选")
    @JsonProperty("bEndTime")
    private String bEndTime;

}
