package edu.lingnan.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("absence")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Absence {

    @TableId(type = IdType.AUTO)
    @JsonProperty("aId")
    private Integer aId;

    @ApiModelProperty(name = "bId",value = "预约编号")
    @TableField("b_id")
    @JsonProperty("bId")
    private Integer bId;

    @ApiModelProperty(name = "aTime",value = "缺勤时间")
    @TableField("a_time")
    @JsonProperty("aTime")
    private String aTime;
}
