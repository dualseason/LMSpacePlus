package edu.lingnan.controller;

import edu.lingnan.entity.Student;
import edu.lingnan.service.StudentService;
import edu.lingnan.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

/**
 * @author dualseason
 */
@CrossOrigin(origins = "*",maxAge = 3600)
@RestController
public class StudentController {
    @Autowired
    private StudentService studentService;

    @GetMapping("/students")
    public Result findAll() {
        List<Student> list = studentService.list();
        return new Result(true, list, "操作成功");
    }

    @PostMapping("/student/register")
    public Result register(@RequestBody Student student) {
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
        if (login != null) {
            return new Result(true, login, "操作成功");
        }
        return new Result(false, null, "操作失败");
    }

    @PostMapping("/checkStudent")
    public Result checkStudent(@RequestBody Student student) {
        boolean update = studentService.updateById(student);
        if (update) {
            return new Result(true, 1, "操作成功");
        }
        return new Result(false, 0, "操作失败");
    }
}
