package edu.lingnan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import edu.lingnan.entity.Teacher;

import java.util.Map;

public interface TeacherService extends IService<Teacher> {
    Teacher checkLogin(Map<String,Object> map);
    boolean checkTeacher(Map<String,Object> map);
}
