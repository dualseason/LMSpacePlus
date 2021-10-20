package edu.lingnan.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@TableName("teacher")
public class Teacher {

    @JsonProperty("tNum")
    @TableField("t_num")
    private String tNum;

    @JsonProperty("tPassWord")
    @TableField("t_passWord")
    private String tPassWord;

    @JsonProperty("tName")
    @TableField("t_name")
    private String tName;
}
