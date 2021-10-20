package edu.lingnan.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
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

    @TableField("re_startTime")
    @JsonProperty("reStartTime")
    private String reStartTime;

    @TableField("re_days")
    @JsonProperty("reDays")
    private String reDays;

    @TableField("b_id")
    @JsonProperty("bId")
    private Integer bId;
}
