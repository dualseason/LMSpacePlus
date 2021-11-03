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
@TableName("classroom")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ClassRoom {
    @ApiModelProperty(name = "rId",value = "教室编号",required = true)
    @TableId(type = IdType.AUTO)
    @JsonProperty("rId")
    private Integer rId;

    @TableField("r_room")
    @JsonProperty("rRoom")
    private String rRoom;

    @ApiModelProperty(name="rStatus",value = "教室状态，1代表开放，0代表关闭",required = true)
    @TableField("r_status")
    @JsonProperty("rStatus")
    private String rStatus;

    @TableField("r_building")
    @JsonProperty("rBuilding")
    private String rBuilding;

    @ApiModelProperty(name="rCanables",value = "教室可预约座位数")
    @TableField("r_canables")
    @JsonProperty("rCanables")
    private Integer rCanables;

    @ApiModelProperty(name="rNums",value = "教室总的座位数")
    @TableField("r_nums")
    @JsonProperty("rNums")
    private Integer rNums;
}
