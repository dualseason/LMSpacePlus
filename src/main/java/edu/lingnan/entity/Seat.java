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
@TableName("seat")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Seat {

    @TableId(type = IdType.AUTO)
    @JsonProperty("seatId")
    private Integer seatId;

    @TableField("seat_num")
    @JsonProperty("seatNum")
    private String seatNum;

    @TableField("r_id")
    @JsonProperty("rId")
    private Integer rId;

    @TableField("seat_status")
    @JsonProperty("seatStatus")
    private String seatStatus;
}
