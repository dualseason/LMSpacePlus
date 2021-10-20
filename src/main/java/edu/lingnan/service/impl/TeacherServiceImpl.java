package edu.lingnan.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.lingnan.entity.Teacher;
import edu.lingnan.mapper.TeacherMapper;
import edu.lingnan.service.TeacherService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class TeacherServiceImpl extends ServiceImpl<TeacherMapper, Teacher> implements TeacherService {
    @Resource
    private TeacherMapper teacherMapper;

    @Override
    public Teacher checkLogin(Map<String, Object> map) {
        List<Teacher> teachers = teacherMapper.selectByMap(map);
        if (CollectionUtils.isEmpty(teachers)) {
            return null;
        } else {
            return teachers.get(0);
        }
    }
}
