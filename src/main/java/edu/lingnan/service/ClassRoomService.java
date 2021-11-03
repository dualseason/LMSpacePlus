package edu.lingnan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import edu.lingnan.entity.ClassRoom;

import java.util.List;

public interface ClassRoomService extends IService<ClassRoom> {
    boolean subtract1(Integer rId);
    int openOrCloseClassRoom(ClassRoom classRoom);
    int addOneClassRooms(ClassRoom classRoom);
    List<ClassRoom> findUsefulClassRoomsList();
}
