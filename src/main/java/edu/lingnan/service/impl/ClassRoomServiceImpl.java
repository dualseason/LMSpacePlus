package edu.lingnan.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.lingnan.entity.ClassRoom;
import edu.lingnan.mapper.ClassRoomMapper;
import edu.lingnan.service.ClassRoomService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
@Transactional
public class ClassRoomServiceImpl extends ServiceImpl<ClassRoomMapper, ClassRoom> implements ClassRoomService {

    @Resource
    private ClassRoomMapper classRoomMapper;

    @Override
    public boolean subtract1(Integer rId) {
        ClassRoom classRoom = classRoomMapper.selectById(rId);
        classRoom.setRCanables(classRoom.getRCanables()-1);
        int i = classRoomMapper.updateById(classRoom);
        return i==0?false:true;
    }
}
