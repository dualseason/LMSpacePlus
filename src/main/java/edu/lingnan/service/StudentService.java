package edu.lingnan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import edu.lingnan.entity.Student;

import java.util.Map;

public interface StudentService extends IService<Student> {
    Student checkLogin(Map<String,Object> map);
}
