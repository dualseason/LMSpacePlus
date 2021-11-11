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
@TableName("student")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Student {
    @ApiModelProperty(name = "sId",value = "学号",required = true)
    @JsonProperty("sId")
    @TableField("s_id")
    private String sId;

    @ApiModelProperty(name = "sPassword",value = "密码",required = true)
    @TableField("s_passWord")
    @JsonProperty("sPassword")
    private String sPassword;

    @TableField("s_status")
    @JsonProperty("sStatus")
    private String sStatus;

    @ApiModelProperty(name="sName",value = "名称")
    @TableField("s_name")
    @JsonProperty("sName")
    private String sName;

    @ApiModelProperty(name="sGrade",value = "年级")
    @TableField("s_grade")
    @JsonProperty("sGrade")
    private String sGrade;

    @ApiModelProperty(name="sClass",value = "班级")
    @TableField("s_class")
    @JsonProperty("sClass")
    private String sClass;

    @ApiModelProperty(name="sCollege",value = "学院")
    @TableField("s_college")
    @JsonProperty("sCollege")
    private String sCollege;
}
