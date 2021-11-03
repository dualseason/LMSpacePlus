package edu.lingnan.entity;

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
@TableName("record")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Record {

    @TableId(type = IdType.AUTO)
    @JsonProperty("reId")
    private Integer reId;

    @ApiModelProperty(name = "reStartTime",value = "请假开始时间:字符串时间形式",example = "2021-10-11",required = true)
    @TableField("re_startTime")
    @JsonProperty("reStartTime")
    private String reStartTime;

    @ApiModelProperty(name = "reDays",value = "请假天数",required = true)
    @TableField("re_days")
    @JsonProperty("reDays")
    private String reDays;

    @ApiModelProperty(name = "bId",value = "预约编号",required = true)
    @TableField("b_id")
    @JsonProperty("bId")
    private Integer bId;
}
