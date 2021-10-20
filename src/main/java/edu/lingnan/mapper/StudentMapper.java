package edu.lingnan.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import edu.lingnan.entity.Student;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface StudentMapper extends BaseMapper<Student> {
}
