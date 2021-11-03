package edu.lingnan.dto.result;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentBookingInfo {
    @ApiModelProperty(name = "sId",value = "学号")
    private String sId;
    @ApiModelProperty(name = "sName",value = "姓名")
    private String sName;
    @ApiModelProperty(name = "sClass",value = "班级")
    private String sClass;
    @ApiModelProperty(name = "sPassWord",value = "密码")
    private String sPassWord;
    @ApiModelProperty(name = "seatNum",value = "座位号")
    private Integer seatNum;
    @ApiModelProperty(name = "rBuilding",value = "教学楼")
    private String rBuilding;
    @ApiModelProperty(name = "rRoom",value = "教室名称")
    private String rRoom;
    @ApiModelProperty(name = "bId",value = "预约编号")
    private Integer bId;
    @ApiModelProperty(name = "bStartTime",value = "预约开始时间")
    private String bStartTime;
    @ApiModelProperty(name = "bEndTime",value = "预约结束时间")
    private String bEndTime;
    @ApiModelProperty(name = "totalBookingDays",value = "总的预约天数")
    private Long totalBookingDays;
    @ApiModelProperty(name = "actualBookingDays",value = "实际自习天数")
    private Long actualBookingDays;
}
