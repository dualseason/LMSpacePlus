package edu.lingnan.dto.result;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingInfo {
    @ApiModelProperty(name = "rId",value = "教室编号",example = "")
    private Integer rId;
    @ApiModelProperty(name="bId",value = "预约编号",example = "")
    private Integer bId;
    @ApiModelProperty(name = "seatId",value = "座位编号",example = "")
    private Integer seatId;
    @ApiModelProperty(name = "rRoom",value = "教室名称",example = "")
    private String rRoom;
    @ApiModelProperty(name = "rBuilding",value = "教学楼名称",example = "")
    private String rBuilding;
    @ApiModelProperty(name = "todayStatus",value = "当天状态",example = "")
    private boolean todayStatus;
    @ApiModelProperty(name="todayRecord",value = "当天缺勤状态",example = "")
    private boolean todayRecord;
}
