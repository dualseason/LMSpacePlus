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
@TableName("booking")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Booking {

    @TableId(type = IdType.AUTO)
    @JsonProperty("bId")
    private Integer bId;

    @TableField("s_id")
    @JsonProperty("sId")
    private String sId;

    @TableField("b_startTime")
    @JsonProperty("bStartTime")
    private String bStartTime;

    @TableField("b_endTime")
    @JsonProperty("bEndTime")
    private String bEndTime;

    @TableField("seat_id")
    @JsonProperty("seatId")
    private Integer seatId;

    @TableField("b_useFul")
    @JsonProperty("bUseful")
    private Boolean bUseful;
}
