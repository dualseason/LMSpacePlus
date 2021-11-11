package edu.lingnan.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import edu.lingnan.dto.result.StudentBookingInfo;
import edu.lingnan.entity.Booking;
import edu.lingnan.entity.ClassRoom;
import edu.lingnan.entity.Seat;
import edu.lingnan.entity.Student;
import edu.lingnan.mapper.BookingMapper;
import edu.lingnan.mapper.StudentMapper;
import edu.lingnan.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements StudentService {
    @Resource
    private StudentMapper studentMapper;
    @Resource
    private BookingMapper bookingMapper;
    @Override
    public Student checkLogin(Map<String, Object> map) {
        List<Student> students = studentMapper.selectByMap(map);
        if (CollectionUtils.isEmpty(students)) {
            return null;
        } else {
            return students.get(0);
        }
    }

    @Override
    public List<Student> findAllStudentRegisterInfo() {
        QueryWrapper<Student> wrapper = new QueryWrapper<>();
        wrapper.select("s_id","s_name").eq("s_status","2");
        List<Student> students = studentMapper.selectList(wrapper);
        return students;
    }

    @Override
    public int updateOrDeleteStudentStatus(Student student) {
        int i = -1;
        if("1".equals(student.getSStatus()))
        {
            UpdateWrapper<Student> wrapper = new UpdateWrapper<>();
            wrapper.eq("s_id",student.getSId());
            Student student2 = new Student();
            student2.setSStatus("1");
            i = studentMapper.update(student2, wrapper);
        }
        if("0".equals(student.getSStatus()))
        {
            HashMap<String, Object> map = new HashMap<>();
            map.put("s_id",student.getSId());
            i = studentMapper.deleteByMap(map);
        }
        return i;
    }
//    判断该学生是否已经注册成功并通过审核
    @Override
    public boolean checkStudent(Student student) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("s_id",student.getSId());
        List<Student> students = studentMapper.selectByMap(map);
        Student student1 = null;
        if(students.size() > 0)
        {
            student1 = students.get(0);
        }
        if(student1 != null)
        {
            if(student1.getSPassword().equals(student.getSPassword()) && student1.getSStatus().equals("1"))
            {
                return true;
            }else {
                return false;
            }
        }

        return false;
    }
//    查询学生有效预约记录的相关信息，前提条件，该学生已经预约到座位并且该座位正在生效
    @Override
    public StudentBookingInfo queryUseFulStudentBookingInfo(String sId) {
        List<StudentBookingInfo> infoList = bookingMapper.selectJoinList(StudentBookingInfo.class,
                new MPJLambdaWrapper<>()
                        .selectAs(Student::getSId, StudentBookingInfo::getSId)
                        .selectAs(Student::getSName, StudentBookingInfo::getSName)
                        .selectAs(Student::getSPassword, StudentBookingInfo::getSPassWord)
                        .selectAs(Student::getSClass, StudentBookingInfo::getSClass)
                        .selectAs(Seat::getSeatNum, StudentBookingInfo::getSeatNum)
                        .selectAs(ClassRoom::getRBuilding, StudentBookingInfo::getRBuilding)
                        .selectAs(ClassRoom::getRRoom, StudentBookingInfo::getRRoom)
                        .selectAs(Booking::getBId, StudentBookingInfo::getBId)
                        .selectAs(Booking::getBStartTime, StudentBookingInfo::getBStartTime)
                        .selectAs(Booking::getBEndTime, StudentBookingInfo::getBEndTime)
                        .innerJoin(Student.class, Student::getSId, Booking::getSId)
                        .innerJoin(Seat.class, Seat::getSeatId, Booking::getSeatId)
                        .innerJoin(ClassRoom.class, ClassRoom::getRId, Seat::getRId)
                        .eq(Booking::getBUseful, 1)
                        .eq(Student::getSId,sId));
        StudentBookingInfo studentBookingInfo = null;
        if(infoList.size() > 0){
           studentBookingInfo = infoList.get(0);
        }

        return studentBookingInfo;
    }
    @Override
    public boolean updateStudentInfo(Student student){
        UpdateWrapper<Student> wrapper = new UpdateWrapper<>();
        wrapper.eq("s_id",student.getSId());
        if(student.getSPassword()!=null &&student.getSPassword()!=""){
            wrapper.set("s_passWord",student.getSPassword());
        }
        if(student.getSClass()!=null &&student.getSClass()!=""){
            wrapper.set("s_class",student.getSClass());
        }
        if(student.getSCollege()!=null&&student.getSCollege()!="")
        {
            wrapper.set("s_college",student.getSCollege());
        }
        if(student.getSGrade()!=null&&student.getSGrade()!=""){
            wrapper.set("s_grade",student.getSGrade());
        }
        if(student.getSName()!=null&&student.getSName()!=""){
            wrapper.set("s_name",student.getSName());
        }
        boolean update = this.update(wrapper);
        if(update){
            return true;
        }
        return false;
    }
}
