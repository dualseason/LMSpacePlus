package edu.lingnan.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.lingnan.dto.StudentReq;
import edu.lingnan.dto.result.StudentBookingInfo;
import edu.lingnan.entity.Booking;
import edu.lingnan.entity.ClassRoom;
import edu.lingnan.entity.Record;
import edu.lingnan.entity.Seat;
import edu.lingnan.mapper.*;
import edu.lingnan.service.ClassRoomService;
import edu.lingnan.service.SeatService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.ParseException;
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
    @Resource
    private RecordMapper recordMapper;
    @Resource
    private AbsenceMapper absenceMapper;
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
        //如果当天选当天退选，则删除该记录
        String format1 = simpleDateFormat.format(new Date());
        if(format1.equals(booking1.getBStartTime())){
            int i = bookingMapper.deleteByMap(map);

        }else {
            Date date = new Date(new Date().getTime() - 24 * 60 * 60 * 1000);
            String format = simpleDateFormat.format(date);
            booking1.setBEndTime(format);
            booking1.setBUseful(false);
            bookingMapper.updateById(booking1);
        }

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

    @Override
    public int returnSeat2(Booking booking) {
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
        //如果当天选当天退选，则删除该记录
        String format1 = simpleDateFormat.format(new Date());
        /**
         * 如果请假期间退选了之后如何处理？
         * ①首先判断归还当天是不是预约开始时间，如果是则根据预约编号删除所有的请假记录
         * 如果不是则进行第二步操作
         * ②通过预约编号找到所有的请假记录，将每条请假记录和归还时间进行比较
         * 如果请假开始时间》=归还时间，则删除该记录
         * 如果归还时间》请假开始时间，归还时间《=请假结束时间，则修改请假结束时间为归还时间的前一天（修改请假天数）
         */
        if(format1.equals(booking1.getBStartTime())){
            int i = bookingMapper.deleteByMap(map);
            HashMap<String, Object> map1 = new HashMap<>();
            map1.put("b_id",booking1.getBId());
            map1.put("a_time",format1);
            absenceMapper.deleteByMap(map1);
            recordMapper.deleteByMap(map);
        }else {
            Date date = new Date(new Date().getTime() - 24 * 60 * 60 * 1000);
            String format = simpleDateFormat.format(date);
            booking1.setBEndTime(format);
            booking1.setBUseful(false);
            bookingMapper.updateById(booking1);
            //查询所有的请假记录
            List<Record> records = recordMapper.selectByMap(map);
            for (int i = 0; i < records.size(); i++) {
                Record record = records.get(i);
                //请假开始时间
                try {
                    Date parse = simpleDateFormat.parse(record.getReStartTime());
                    long endTime = parse.getTime()+(Integer.parseInt(record.getReDays())-1)*24*60*60*1000;
                    //当天时间
                    Date parse1 = simpleDateFormat.parse(simpleDateFormat.format(new Date()));

//                    如果请假开始时间》=归还时间，则删除该记录
                    if(parse.getTime() >= parse1.getTime()){
                        HashMap<String, Object> map1 = new HashMap<>();
                        map1.put("re_id",record.getReId());
                        recordMapper.deleteByMap(map1);
                    }else if(parse1.getTime() <= endTime){
//                        如果归还时间》请假开始时间，归还时间《=请假结束时间，则修改请假结束时间为归还时间的前一天（修改请假天数）
                        long days = (parse1.getTime()-24*60*60*1000-parse.getTime())/24/60/60/1000 + 1;
                        record.setReDays(String.valueOf(days));
                        recordMapper.updateById(record);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }

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
