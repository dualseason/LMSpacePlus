package edu.lingnan.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.lingnan.entity.Student;
import edu.lingnan.mapper.StudentMapper;
import edu.lingnan.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements StudentService {
    @Resource
    private StudentMapper studentMapper;

    @Override
    public Student checkLogin(Map<String, Object> map) {
        List<Student> students = studentMapper.selectByMap(map);
        if (CollectionUtils.isEmpty(students)) {
            return null;
        } else {
            return students.get(0);
        }
    }
}
