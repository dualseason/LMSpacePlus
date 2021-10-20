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
@TableName("classroom")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ClassRoom {

    @TableId(type = IdType.AUTO)
    @JsonProperty("rId")
    private Integer rId;

    @TableField("r_room")
    @JsonProperty("rRoom")
    private String rRoom;

    @TableField("r_status")
    @JsonProperty("rStatus")
    private String rStatus;

    @TableField("r_building")
    @JsonProperty("rBuilding")
    private String rBuilding;

    @TableField("r_canables")
    @JsonProperty("rCanables")
    private Integer rCanables;

    @TableField("r_nums")
    @JsonProperty("rNums")
    private Integer rNums;
}
