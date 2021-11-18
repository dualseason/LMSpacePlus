package edu.lingnan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import edu.lingnan.dto.ClassRoomReq;
import edu.lingnan.entity.ClassRoom;

import java.util.List;

public interface ClassRoomService extends IService<ClassRoom> {
    boolean subtract1(Integer rId);
    int openOrCloseClassRoom(ClassRoom classRoom);
    int addOneClassRooms(ClassRoom classRoom);
    List<ClassRoom> findUsefulClassRoomsList();
    boolean checkClassRoomCanOrNotClose(ClassRoom classRoom);
    boolean checkClassRoomCanOrNotAdd(ClassRoomReq classRoomReq0);
    boolean deleteClassRoom(Integer rId);
    List<ClassRoom> findUsefulClassRoomsList2();
}
