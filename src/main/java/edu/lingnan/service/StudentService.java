package edu.lingnan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import edu.lingnan.dto.result.StudentBookingInfo;
import edu.lingnan.entity.Student;

import java.util.List;
import java.util.Map;

public interface StudentService extends IService<Student> {
    Student checkLogin(Map<String,Object> map);
    List<Student> findAllStudentRegisterInfo();
    int updateOrDeleteStudentStatus(Student student);
    boolean checkStudent(Student student);
    StudentBookingInfo queryUseFulStudentBookingInfo(String sId);
    public boolean updateStudentInfo(Student student);
}
