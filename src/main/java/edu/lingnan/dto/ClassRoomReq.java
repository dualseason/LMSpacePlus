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
@TableName("classroom")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ClassRoomReq {


    @ApiModelProperty(name="rRoom",value = "教室名称",required = true)
    @TableField("r_room")
    @JsonProperty("rRoom")
    private String rRoom;



    @ApiModelProperty(name="rBuilding",value = "教学楼",required = true)
    @TableField("r_building")
    @JsonProperty("rBuilding")
    private String rBuilding;



    @ApiModelProperty(name="rNums",value = "教室总的座位数(必须为数字)",required = true)
    @TableField("r_nums")
    @JsonProperty("rNums")
    private Integer rNums;
}
