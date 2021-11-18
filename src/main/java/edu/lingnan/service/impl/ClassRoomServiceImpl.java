package edu.lingnan.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import edu.lingnan.dto.ClassRoomReq;
import edu.lingnan.entity.Booking;
import edu.lingnan.entity.ClassRoom;
import edu.lingnan.entity.Seat;
import edu.lingnan.mapper.BookingMapper;
import edu.lingnan.mapper.ClassRoomMapper;
import edu.lingnan.mapper.SeatMapper;
import edu.lingnan.service.ClassRoomService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

@Service
@Transactional
public class ClassRoomServiceImpl extends ServiceImpl<ClassRoomMapper, ClassRoom> implements ClassRoomService {

    @Resource
    private ClassRoomMapper classRoomMapper;
    @Resource
    private SeatMapper seatMapper;
    @Resource
    private BookingMapper bookingMapper;
    @Override
    public boolean subtract1(Integer rId) {
        ClassRoom classRoom = classRoomMapper.selectById(rId);
        classRoom.setRCanables(classRoom.getRCanables()-1);
        int i = classRoomMapper.updateById(classRoom);
        return i==0?false:true;
    }

    @Override
    public int openOrCloseClassRoom(ClassRoom classRoom) {
        int i = -1;
        //根据教室状态来判断打开或关闭教室
        if("1".equals(classRoom.getRStatus()))
        {
            //开放教室
            ClassRoom room = classRoomMapper.selectById(classRoom.getRId());
            ClassRoom updateClassRoom = new ClassRoom();
            updateClassRoom.setRStatus(classRoom.getRStatus());
            updateClassRoom.setRCanables(room.getRNums());
            UpdateWrapper<ClassRoom> wrapper = new UpdateWrapper<>();
            wrapper.eq("r_id",classRoom.getRId());
            i = classRoomMapper.update(updateClassRoom,wrapper);
            //开放座位
            Seat seat = new Seat();
            seat.setSeatStatus(classRoom.getRStatus());
            UpdateWrapper<Seat> seatUpdateWrapper = new UpdateWrapper<>();
            seatUpdateWrapper.eq("r_id",classRoom.getRId());
            seatMapper.update(seat,seatUpdateWrapper);
        }
        if("0".equals(classRoom.getRStatus()))
        {
            //关闭教室
            ClassRoom classRoom2 = new ClassRoom();
            classRoom2.setRStatus(classRoom.getRStatus());
            classRoom2.setRCanables(0);
            UpdateWrapper<ClassRoom> roomUpdateWrapper = new UpdateWrapper<>();
            roomUpdateWrapper.eq("r_id",classRoom.getRId());
            i = classRoomMapper.update(classRoom2,roomUpdateWrapper);
            //关闭座位
            Seat seat = new Seat();
            seat.setSeatStatus(classRoom.getRStatus());
            UpdateWrapper<Seat> seatUpdateWrapper = new UpdateWrapper<>();
            seatUpdateWrapper.eq("r_id",classRoom.getRId());
            seatMapper.update(seat,seatUpdateWrapper);
        }


        return i;
    }

    @Override
    public int addOneClassRooms(ClassRoom classRoom) {
        classRoom.setRStatus("0");
        classRoom.setRCanables(0);
        classRoom.setRId(null);
        int i = -1;
        i = classRoomMapper.insert(classRoom);
        QueryWrapper<ClassRoom> wrapper = new QueryWrapper<>();
        wrapper.select("MAX(r_id) as r_id");
        List<ClassRoom> classRooms = classRoomMapper.selectList(wrapper);
        ClassRoom classRoom2 = classRooms.get(0);
        Integer rId = classRoom2.getRId();
        Seat seat = new Seat();
        seat.setRId(rId);
        seat.setSeatStatus("0");
        for (int j = 1; j <=classRoom.getRNums(); j++) {
            seat.setSeatNum(String.valueOf(j));
            seatMapper.insert(seat);
        }
        return i;
    }
    //查询所有开放的教室，用于学生预约教室
    @Override
    public List<ClassRoom> findUsefulClassRoomsList() {
        QueryWrapper<ClassRoom> wrapper = new QueryWrapper<>();
        wrapper.eq("r_status","1").gt("r_canables",0);
        List<ClassRoom> classRooms = classRoomMapper.selectList(wrapper);
        return classRooms;
    }

    @Override
    public boolean checkClassRoomCanOrNotClose(ClassRoom classRoom) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("r_id",classRoom.getRId());
        List<ClassRoom> classRooms = classRoomMapper.selectByMap(map);
        if(classRooms.size() > 0){
            ClassRoom classRoom1 = classRooms.get(0);
            if("0".equals(classRoom.getRStatus()) && classRoom1.getRCanables() == classRoom1.getRNums()&&classRoom1.getRStatus().equals("1")){
                return true;
            }
            if("1".equals(classRoom.getRStatus()) && classRoom1.getRCanables()==0 && classRoom1.getRStatus().equals("0")){
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean checkClassRoomCanOrNotAdd(ClassRoomReq classRoomReq0) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("r_building",classRoomReq0.getRBuilding());
        map.put("r_room",classRoomReq0.getRRoom());
        List<ClassRoom> classRooms = classRoomMapper.selectByMap(map);
        if(classRooms.size() > 0){
            return false;
        }
        return true;
    }
    //删除教室，首先判断该教室有没有被预约过
    @Override
    public boolean deleteClassRoom(Integer rId) {
        List<Seat> seats = bookingMapper.selectJoinList(Seat.class,
                new MPJLambdaWrapper<>()
                        .selectAs(Seat::getSeatId, Seat::getSeatId)
                        .innerJoin(Seat.class, Seat::getSeatId, Booking::getSeatId)
                        .eq(Seat::getRId, rId)
        );
        if(seats.size()>0){

            return false;
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put("r_id",rId);
        classRoomMapper.deleteByMap(map);
        seatMapper.deleteByMap(map);
        return true;
    }

    @Override
    public List<ClassRoom> findUsefulClassRoomsList2() {
        QueryWrapper<ClassRoom> wrapper = new QueryWrapper<>();
        wrapper.eq("r_status","1");
        List<ClassRoom> classRooms = classRoomMapper.selectList(wrapper);
        return classRooms;
    }

}
