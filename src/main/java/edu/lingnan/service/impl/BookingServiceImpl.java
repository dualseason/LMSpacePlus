package edu.lingnan.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import edu.lingnan.dto.result.BookingInfo;
import edu.lingnan.entity.*;
import edu.lingnan.mapper.AbsenceMapper;
import edu.lingnan.mapper.BookingMapper;
import edu.lingnan.mapper.ClassRoomMapper;
import edu.lingnan.mapper.SeatMapper;
import edu.lingnan.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
@Transactional
public class BookingServiceImpl extends ServiceImpl<BookingMapper, Booking> implements BookingService {
    @Resource
    private BookingMapper bookingMapper;
    @Resource
    private AbsenceMapper absenceMapper;
    @Resource
    private AbsenceService absenceService;
    @Resource
    private RecordService recordService;
    @Resource
    private SeatMapper seatMapper;
    @Resource
    private ClassRoomMapper classRoomMapper;
    @Override
    public List<BookingInfo> queryUserfulBookingList() {
        //查询所有有效的预约记录及座位教室信息
        List<BookingInfo> bookingInfos = bookingMapper.selectJoinList(BookingInfo.class,
                new MPJLambdaWrapper<>()
                        .selectAs(Booking::getBId, BookingInfo::getBId)//参数，要查询的字段，并指定别名
                        .selectAs(Seat::getSeatNum, BookingInfo::getSeatId)
                        .selectAs(ClassRoom::getRRoom, BookingInfo::getRRoom)
                        .selectAs(ClassRoom::getRBuilding, BookingInfo::getRBuilding)
                        .selectAs(ClassRoom::getRId,BookingInfo::getRId)
                        .selectAs(Student::getSId,BookingInfo::getSId)
                        .selectAs(Student::getSName,BookingInfo::getSName)
                        .selectAs(Student::getSCollege,BookingInfo::getSCollege)
                        .selectAs(Student::getSGrade,BookingInfo::getSGrade)
                        .selectAs(Student::getSClass,BookingInfo::getSClass)
                        .innerJoin(Seat.class,Seat::getSeatId,Booking::getSeatId)//指定要连接的表，设置表相等字段
                        .innerJoin(ClassRoom.class, ClassRoom::getRId,Seat::getRId)
                        .innerJoin(Student.class,Student::getSId,Booking::getSId)
                        .eq(Booking::getBUseful,1)
                        .eq(ClassRoom::getRStatus,1)
//
        );
//         absenceService.getCurrentNoRecordBookingList(bookingInfos);
//        List<Absence> absences = absenceMapper.selectList(null);
//        /*
//         * 该for循环用于添加正在生效的预约记录的当天考勤状态，通过缺勤记录和预约记录中的相同预约编号，然后在判断缺勤记录的时间和当天时间是否一样
//         * 如果缺勤记录的时间和当天时间一样的话，则该考勤状态为false（缺勤）
//         * */
//        for (int i = 0; i < bookingInfos.size(); i++) {
//            bookingInfos.get(i).setTodayStatus(true);
//            for (int j = 0; j < absences.size(); j++) {
//                if(absences.get(j).getBId()==bookingInfos.get(i).getBId())
//                {
//                    SimpleDateFormat format = new SimpleDateFormat("yyyy-M-d");
//                    String s = format.format(new Date());
//                    if(s.equals(absences.get(j).getATime()))
//                    {
//                        bookingInfos.get(i).setTodayStatus(false);
//                        break;
//                    }
//
//                }
////                if(j==absences.size()-1){
////                    bookingInfos.get(i).setTodayStatus(true);
////                }
//            }
//        }
        return bookingInfos;
    }

    @Override
    public List<BookingInfo> queryUserfulBookingList2() {
        //查询所有有效的预约记录及座位教室信息
        List<BookingInfo> bookingInfos = bookingMapper.selectJoinList(BookingInfo.class,
                new MPJLambdaWrapper<>()
                        .selectAs(Booking::getBId, BookingInfo::getBId)//参数，要查询的字段，并指定别名
                        .selectAs(Seat::getSeatNum, BookingInfo::getSeatId)
                        .selectAs(ClassRoom::getRRoom, BookingInfo::getRRoom)
                        .selectAs(ClassRoom::getRBuilding, BookingInfo::getRBuilding)
                        .selectAs(ClassRoom::getRId,BookingInfo::getRId)
                        .selectAs(Student::getSId,BookingInfo::getSId)
                        .selectAs(Student::getSName,BookingInfo::getSName)
                        .selectAs(Student::getSCollege,BookingInfo::getSCollege)
                        .selectAs(Student::getSGrade,BookingInfo::getSGrade)
                        .selectAs(Student::getSClass,BookingInfo::getSClass)
                        .innerJoin(Seat.class,Seat::getSeatId,Booking::getSeatId)//指定要连接的表，设置表相等字段
                        .innerJoin(ClassRoom.class, ClassRoom::getRId,Seat::getRId)
                        .innerJoin(Student.class,Student::getSId,Booking::getSId)
                        .eq(Booking::getBUseful,1)
                        .eq(ClassRoom::getRStatus,1)
//
        );

        List<Absence> absences = absenceMapper.selectList(null);
        /*
         * 该for循环用于添加正在生效的预约记录的当天考勤状态，通过缺勤记录和预约记录中的相同预约编号，然后在判断缺勤记录的时间和当天时间是否一样
         * 如果缺勤记录的时间和当天时间一样的话，则该考勤状态为false（缺勤）
         * */
        for (int i = 0; i < bookingInfos.size(); i++) {
            bookingInfos.get(i).setTodayStatus(true);
            for (int j = 0; j < absences.size(); j++) {
                if(absences.get(j).getBId()==bookingInfos.get(i).getBId())
                {
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-M-d");
                    String s = format.format(new Date());
                    if(s.equals(absences.get(j).getATime()))
                    {
                        bookingInfos.get(i).setTodayStatus(false);
                        break;
                    }

                }
//                if(j==absences.size()-1){
//                    bookingInfos.get(i).setTodayStatus(true);
//                }
            }
        }
        absenceService.getCurrentNoRecordBookingList2(bookingInfos);
        return bookingInfos;
    }

    //    判断学生是否有预约生效的记录
    @Override
    public boolean queryStudentUserfulBookingInfo(String sId) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("s_id",sId);
        map.put("b_useFul",1);
        List<Booking> bookings = bookingMapper.selectByMap(map);
        if(bookings.size() > 0)
        {
            return true;
        }
        return false;
    }
//得到总的预约天数
    @Override
    public long[] getTotalBookingDays(String sId) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("s_id",sId);
        List<Booking> bookings = bookingMapper.selectByMap(map);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-M-d");
        long totalDays = 0;
        ArrayList<Integer> integers = new ArrayList<>();
        for (Booking booking : bookings) {
            String startTime = booking.getBStartTime();
            String endTime = booking.getBEndTime();
            integers.add(booking.getBId());
            try {
                Date parse = simpleDateFormat.parse(startTime);
                Date parse2 = simpleDateFormat.parse(endTime);
                long days = (parse2.getTime() - parse.getTime()) /24/60/60/1000 + 1;
                totalDays +=  days;
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        Long totalAbsenceDays = absenceService.getTotalAbsenceDays(integers);
        Long allRecordDays = recordService.getAllRecordDays(integers);
        long[] longs = new long[2];
        longs[0] = totalDays;
        longs[1] = totalAbsenceDays + allRecordDays;
        return longs;
    }

    @Override
    public long[] getTotalBookingDays2(String sId) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("s_id",sId);
        List<Booking> bookings = bookingMapper.selectByMap(map);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-M-d");
        long totalDays = 0;
        Integer currentBid = 0;
        ArrayList<Integer> integers = new ArrayList<>();
        for (Booking booking : bookings) {
            String startTime = booking.getBStartTime();
            String endTime = booking.getBEndTime();
            if(booking.getBUseful()==false){
                integers.add(booking.getBId());
            }else {
                currentBid = booking.getBId();
            }

            try {
                Date parse = simpleDateFormat.parse(startTime);
                Date parse2 = simpleDateFormat.parse(endTime);
                long days = (parse2.getTime() - parse.getTime()) /24/60/60/1000 + 1;
                totalDays +=  days;
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        Long totalAbsenceDays = absenceService.getTotalAbsenceDays(integers) + absenceService.getUsefulBookingAbsenceDays(currentBid);
        Long allRecordDays = recordService.getAllRecordDays(integers) + recordService.getUsefulBookingRecordDays(currentBid);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-M-d");
        HashMap<String, Object> map1 = new HashMap<>();
        map1.put("b_id",currentBid);
        List<Booking> map2 = bookingMapper.selectByMap(map1);
        String endTime = map2.get(0).getBEndTime();
        String format1 = format.format(new Date());
        long days = 0;
        try {
            Date date1 = format.parse(endTime);
            Date parse2 = format.parse(format1);
            days = (date1.getTime() - parse2.getTime())/24/60/60/1000 + 1;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long[] longs = new long[2];
        longs[0] = totalDays;
        longs[1] = totalAbsenceDays + allRecordDays + days;
        return longs;
    }

    //学生续约，延长学生预约座位时间
    @Override
    public int renewal(Integer bId) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("b_id",bId);
        List<Booking> bookings = bookingMapper.selectByMap(map);
        Booking booking = bookings.get(0);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-M-d");
        int i = -1;
        try {
            Date parse = format.parse(booking.getBEndTime());
            Date date = new Date(parse.getTime() + 7 * 24 * 60 * 60 * 1000);
            String format1 = format.format(date);
            booking.setBEndTime(format1);
            i = bookingMapper.updateById(booking);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return i;
    }

    @Override
    public boolean queryBookingAbleUseful(HashMap<String, Object> map) {
        List<Booking> bookings = bookingMapper.selectByMap(map);
        if(bookings.size() > 0){
            return true;
        }
        return false;
    }
    @Scheduled(cron ="0 0 1 * * ?")
//@Scheduled(cron ="0 0 17 * * ?")
    //定时更新预约信息
    @Override
    public List<Booking> updateBookingInfoTiming() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("b_useFul",1);
        List<Booking> bookings = bookingMapper.selectByMap(map);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-M-d");
        String format1 = format.format(new Date());
        try {
            //当天时间
            Date parse = format.parse(format1);
            for (int i = 0; i < bookings.size(); i++) {
                Booking booking = bookings.get(i);
                if(parse.getTime() > format.parse(booking.getBEndTime()).getTime()){
                    booking.setBUseful(false);
                    bookingMapper.updateById(booking);
                    //***********************
                    UpdateWrapper<Seat> wrapper = new UpdateWrapper<>();
                    wrapper.eq("seat_id",booking.getSeatId());
                    Seat seat = new Seat();
                    seat.setSeatStatus("1");
                    seatMapper.update(seat,wrapper);
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("seat_id",booking.getSeatId());
                    List<Seat> seats = seatMapper.selectByMap(hashMap);
                    Seat seat1 = seats.get(0);
                    HashMap<String, Object> hashMap1 = new HashMap<>();
                    hashMap1.put("r_id",seat1.getRId());
                    List<ClassRoom> classRooms = classRoomMapper.selectByMap(hashMap1);
                    ClassRoom classRoom = classRooms.get(0);
                    classRoom.setRCanables(classRoom.getRCanables()+1);
                    classRoomMapper.updateById(classRoom);
                    continue;
                }
                bookings.remove(i);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return bookings;
    }
}
