package edu.lingnan.controller;

import edu.lingnan.dto.StudentCheckReq;
import edu.lingnan.dto.StudentReq;
import edu.lingnan.dto.result.StudentBookingInfo;
import edu.lingnan.dto.result.StudentLoginNoBookingInfo;
import edu.lingnan.entity.ClassRoom;
import edu.lingnan.entity.Student;
import edu.lingnan.service.BookingService;
import edu.lingnan.service.ClassRoomService;
import edu.lingnan.service.SeatService;
import edu.lingnan.service.StudentService;
import edu.lingnan.vo.Result;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@RestController
public class StudentController {
    @Autowired
    private StudentService studentService;
    @Autowired
    private BookingService bookingService;
    @Autowired
    private ClassRoomService classRoomService;
    @Autowired
    private SeatService seatService;

    @GetMapping("/students")
    public Result findAll() {
        List<Student> list = studentService.list();
        return new Result(true, list, "操作成功");
    }

    @PostMapping("/student/register")
    public Result register(@RequestBody Student student) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("s_id",student.getSId());
        Student checkStudent = studentService.checkLogin(map);
        if(checkStudent != null){
            return new Result(false,0,"该学生已存在");
        }
        student.setSStatus("2");
        boolean save = studentService.save(student);
        if (save) {
            return new Result(true, 1, "操作成功");
        } else {
            return new Result(false, 0, "操作失败");
        }
    }

    @PostMapping("/student/login")
    public Result login(@RequestBody Student student) {
        HashMap<String, Object> map = new HashMap<>(2);
        map.put("s_id", student.getSId());
        map.put("s_passWord", student.getSPassword());
        Student login = studentService.checkLogin(map);
        if (login != null && login.getSStatus().equals("1")) {
            return new Result(true, login, "操作成功");
        }
        return new Result(false, null, "操作失败");
    }

    @ApiOperation(value = "学生登录，如果登录成功，则根据该学生的预约状态返回响应的数据")
    @PostMapping("/student/login2")
    public Result studentLogin2(@RequestBody Student student) {
        boolean b = studentService.checkStudent(student);
        if(b)
        {
            boolean b1 = bookingService.queryStudentUserfulBookingInfo(student.getSId());
            if(b1)
            {
                StudentBookingInfo studentBookingInfo = studentService.queryUseFulStudentBookingInfo(student.getSId());
                return new Result(true,studentBookingInfo,"操作成功");
            }else {
                List<ClassRoom> roomsList = classRoomService.findUsefulClassRoomsList();
                HashMap<String, Object> map = new HashMap<>();
                map.put("s_id",student.getSId());
                Student student1 = studentService.checkLogin(map);
                StudentLoginNoBookingInfo info = new StudentLoginNoBookingInfo(roomsList, student1);
                return new Result(true,info,"操作成功");
            }
        }
        else {
            return new Result(false,null,"操作失败");
        }

    }

    @PostMapping("/checkStudent")
    public Result checkStudent(@RequestBody Student student) {
        boolean update = studentService.updateById(student);
        if (update) {
            return new Result(true, 1, "操作成功");
        }
        return new Result(false, 0, "操作失败");
    }
    @ApiOperation(value = "注册审核：查看所有学生的注册信息")
    @GetMapping("/student/findAllStudentRegisterInfo")
    public Result findAllStudentRegisterInfo(){
        List<Student> studentList = studentService.findAllStudentRegisterInfo();
        if(!CollectionUtils.isEmpty(studentList))
        {
            return new Result(true,studentList,"操作成功");
        }
        return new Result(false,null,"操作失败");
    }
    @ApiOperation(value = "注册审核：更新学生状态，即注册通过或注册失败")
    @PostMapping("/student/updateOrDeleteStudentStatus")
    public Result updateOrDeleteStudentStatus(@RequestBody StudentCheckReq studentCheckReq){
        Student student = new Student();
        student.setSStatus(studentCheckReq.getSStatus());
        student.setSId(studentCheckReq.getSId());
        int i = studentService.updateOrDeleteStudentStatus(student);
        if(i > 0)
        {
            return new Result(true,studentService.findAllStudentRegisterInfo(),"操作成功");
        }
        return new Result(false,"null","操作失败");
    }
    @ApiOperation(value = "给学生自动分配座位")
    @PostMapping("/student/provideStudentOneSeat")
    public Result provideStudentOneSeat(@RequestBody StudentReq studentReq){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-M-d");
        String currentTime = format.format(new Date());
        studentReq.setBStartTime(currentTime);
        studentReq.setBEndTime(format.format(new Date(new Date().getTime() + 6*24*60*60*1000)));
        boolean b = bookingService.queryStudentUserfulBookingInfo(studentReq.getSId());
        if(b){
            return new Result(false,"null","该学生已经有了预约记录");
        }
        seatService.provideOneSeatForStudent(studentReq);
        boolean b1 = bookingService.queryStudentUserfulBookingInfo(studentReq.getSId());
        if(b1)
        {
            StudentBookingInfo studentBookingInfo = studentService.queryUseFulStudentBookingInfo(studentReq.getSId());
            return new Result(true,studentBookingInfo,"操作成功");
        }
        return new Result(false,"null","操作失败");
    }
    @ApiOperation(value = "更新学生信息")
    @PostMapping("/student/updateStudentInfo")
    public Result updateStudentInfo(@RequestBody Student student){
        boolean b = studentService.updateStudentInfo(student);
        if(b){
            return new Result(true,1,"操作成功");
        }
        return new Result(false,0,"操作失败，可能改学生不存在");
    }
}
