package edu.lingnan.entity;

import com.baomidou.mybatisplus.annotation.TableField;
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
@JsonIgnoreProperties(ignoreUnknown = true)
@TableName("teacher")
public class Teacher {
    @ApiModelProperty(name = "tNum",value = "教师账号",required = true)
    @JsonProperty("tNum")
    @TableField("t_num")
    private String tNum;

    @ApiModelProperty(name = "tPassWord",value = "教师密码",required = true)
    @JsonProperty("tPassWord")
    @TableField("t_passWord")
    private String tPassWord;

    @JsonProperty("tName")
    @TableField("t_name")
    private String tName;
}
