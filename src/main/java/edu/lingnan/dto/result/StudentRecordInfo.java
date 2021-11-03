package edu.lingnan.dto.result;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentRecordInfo {
    @ApiModelProperty(name = "sId",value = "学号")
    private String sId;
    @ApiModelProperty(name = "sClass",value = "班级")
    private String sClass;
    @ApiModelProperty(name = "sCollege",value = "学院")
    private String sCollege;
    @ApiModelProperty(name = "rRoom",value = "教室名称")
    private String rRoom;
    @ApiModelProperty(name = "rBuilding",value = "教学楼")
    private String rBuilding;
    @ApiModelProperty(name = "seatNum",value = "座位号")
    private String seatNum;
    @ApiModelProperty(name = "reStartTime",value = "预约时间")
    private String reStartTime;
    @ApiModelProperty(name = "reDays",value = "预约天数")
    private String reDays;

}
