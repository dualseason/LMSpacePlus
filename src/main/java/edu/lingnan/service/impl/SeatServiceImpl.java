package edu.lingnan.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.lingnan.dto.StudentReq;
import edu.lingnan.dto.result.StudentBookingInfo;
import edu.lingnan.entity.Booking;
import edu.lingnan.entity.ClassRoom;
import edu.lingnan.entity.Seat;
import edu.lingnan.mapper.BookingMapper;
import edu.lingnan.mapper.ClassRoomMapper;
import edu.lingnan.mapper.SeatMapper;
import edu.lingnan.service.ClassRoomService;
import edu.lingnan.service.SeatService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
@Transactional
public class SeatServiceImpl extends ServiceImpl<SeatMapper, Seat> implements SeatService {
    @Resource
    private SeatMapper seatMapper;
    @Resource
    private BookingMapper bookingMapper;
    @Resource
    private ClassRoomMapper classRoomMapper;
    @Override
    public List<Seat> getAccessibleSeat() {
        HashMap<String, Object> map = new HashMap<>(1);
        map.put("seat_status","1");
        return seatMapper.selectByMap(map);
    }

    @Override
    public void provideOneSeatForStudent(StudentReq studentReq) {

//        ①分配一个座位给学生，并更改该座位，生成一条预约记录
//        ②更改教室座位数
        HashMap<String, Object> map = new HashMap<>();
        map.put("r_id",studentReq.getRId());
        map.put("seat_status","1");
        List<Seat> seats = seatMapper.selectByMap(map);
        if(seats.size() > 0)
        {
            Seat seat = seats.get(0);
            seat.setSeatStatus("0");
            seatMapper.updateById(seat);
            Booking booking = new Booking();
            booking.setSId(studentReq.getSId());
            booking.setBStartTime(studentReq.getBStartTime());
            booking.setBEndTime(studentReq.getBEndTime());
            booking.setSeatId(seat.getSeatId());
            booking.setBUseful(true);
            int insert = bookingMapper.insert(booking);
            //查询某座位表中某教室的可预约座位数
            QueryWrapper<Seat> seatQueryWrapper = new QueryWrapper<>();
            seatQueryWrapper.select("count(r_id) as rId").eq("r_id",studentReq.getRId()).eq("seat_status","1");
            List<Seat> seats1 = seatMapper.selectList(seatQueryWrapper);
            Integer rId = seats1.get(0).getRId();
            //查询某座位表中某教室的可预约座位数
            UpdateWrapper<ClassRoom> wrapper = new UpdateWrapper<>();
            wrapper.eq("r_id",studentReq.getRId());
            ClassRoom classRoom = new ClassRoom();
            classRoom.setRCanables(rId);
            classRoomMapper.update(classRoom,wrapper);
        }
    }

    @Override
    public int returnSeat(Booking booking) {
        //判断是直接退选还是提前归还
        //提前归还:修改结束时间
        if(booking.getBEndTime()!=null && !"".equals(booking.getBEndTime()))
        {
            HashMap<String, Object> map = new HashMap<>();
            map.put("b_id",booking.getBId());
            List<Booking> bookings = bookingMapper.selectByMap(map);
            Booking booking1 = bookings.get(0);
//            booking1.setBUseful(false);
            booking1.setBEndTime(booking.getBEndTime());
            int i = bookingMapper.updateById(booking1);
//            booking1.setBUseful(true);
//            booking1.setBEndTime(booking.getBEndTime());
//            int i = bookingMapper.insert(booking1);
            return i;

        }
        //直接退选
        HashMap<String, Object> map = new HashMap<>();
        map.put("b_id",booking.getBId());
        List<Booking> bookings = bookingMapper.selectByMap(map);
        Booking booking1 = bookings.get(0);
        //得到昨天时间，即退选时间
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-M-d");
        Date date = new Date(new Date().getTime() - 24 * 60 * 60 * 1000);
        String format = simpleDateFormat.format(date);
        booking1.setBEndTime(format);
        booking1.setBUseful(false);
        bookingMapper.updateById(booking1);
        UpdateWrapper<Seat> wrapper = new UpdateWrapper<>();
        wrapper.eq("seat_id",booking1.getSeatId());
        Seat seat = new Seat();
        seat.setSeatStatus("1");
        seatMapper.update(seat,wrapper);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("seat_id",booking1.getSeatId());
        List<Seat> seats = seatMapper.selectByMap(hashMap);
        Seat seat1 = seats.get(0);
        HashMap<String, Object> hashMap1 = new HashMap<>();
        hashMap1.put("r_id",seat1.getRId());
        List<ClassRoom> classRooms = classRoomMapper.selectByMap(hashMap1);
        ClassRoom classRoom = classRooms.get(0);
        classRoom.setRCanables(classRoom.getRCanables()+1);
        int i = classRoomMapper.updateById(classRoom);
        return i;
    }
}
