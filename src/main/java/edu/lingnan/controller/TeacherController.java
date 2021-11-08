package edu.lingnan.controller;

import edu.lingnan.entity.Teacher;
import edu.lingnan.service.TeacherService;
import edu.lingnan.vo.Result;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

@RestController
public class TeacherController {
    @Autowired
    private TeacherService teacherService;

    @GetMapping("/teachers")
    public Result findAll() {
        List<Teacher> list = teacherService.list();
        return new Result(true, list, "操作成功");
    }
    @PostMapping("/teacher/login")
    public Result login(@RequestBody Teacher teacher) {
        HashMap<String, Object> map = new HashMap<>(2);
        map.put("t_num", teacher.getTNum());
        map.put("t_passWord", teacher.getTPassWord());
        Teacher login = teacherService.checkLogin(map);
        if (login != null) {
            return new Result(true, login, "操作成功");
        } else {
            return new Result(false, null, "操作失败");
        }
    }

    @PostMapping("/teacher/register")
    public Result register(@RequestBody Teacher teacher) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("t_num",teacher.getTNum());
        boolean b = teacherService.checkTeacher(map);
        if(b)return new Result(false,0,"该老师已经存在");
        boolean save = teacherService.save(teacher);
        if (save) {
            return new Result(true, 1, "操作成功");
        } else {
            return new Result(false, 0, "操作失败");
        }
    }
}
