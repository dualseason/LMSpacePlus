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
@TableName("student")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Student {

    @JsonProperty("sId")
    @TableField("s_id")
    private String sId;

    @TableField("s_passWord")
    @JsonProperty("sPassword")
    private String sPassword;

    @TableField("s_status")
    @JsonProperty("sStatus")
    private String sStatus;

    @TableField("s_name")
    @JsonProperty("sName")
    private String sName;

    @TableField("s_grade")
    @JsonProperty("sGrade")
    private String sGrade;

    @TableField("s_class")
    @JsonProperty("sClass")
    private String sClass;

    @TableField("s_college")
    @JsonProperty("sCollege")
    private String sCollege;
}
