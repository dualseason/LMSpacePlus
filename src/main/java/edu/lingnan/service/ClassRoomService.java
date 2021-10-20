package edu.lingnan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import edu.lingnan.entity.ClassRoom;

public interface ClassRoomService extends IService<ClassRoom> {
    boolean subtract1(Integer rId);
}
