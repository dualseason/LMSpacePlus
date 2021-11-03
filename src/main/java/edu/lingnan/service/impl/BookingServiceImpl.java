package edu.lingnan.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import edu.lingnan.dto.result.BookingInfo;
import edu.lingnan.entity.Absence;
import edu.lingnan.entity.Booking;
import edu.lingnan.entity.ClassRoom;
import edu.lingnan.entity.Seat;
import edu.lingnan.mapper.AbsenceMapper;
import edu.lingnan.mapper.BookingMapper;
import edu.lingnan.service.AbsenceService;
import edu.lingnan.service.BookingService;
import edu.lingnan.service.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
@Transactional
public class BookingServiceImpl extends ServiceImpl<BookingMapper, Booking> implements BookingService {
    @Autowired
    private BookingMapper bookingMapper;
    @Autowired
    private AbsenceMapper absenceMapper;
    @Autowired
    private AbsenceService absenceService;
    @Autowired
    private RecordService recordService;
    @Override
    public List<BookingInfo> queryUserfulBookingList() {
        //查询所有有效的预约记录及座位教室信息
        List<BookingInfo> bookingInfos = bookingMapper.selectJoinList(BookingInfo.class,
                new MPJLambdaWrapper<>()
                        .selectAs(Booking::getBId, BookingInfo::getBId)//参数，要查询的字段，并指定别名
                        .selectAs(Booking::getSeatId, BookingInfo::getSeatId)
                        .selectAs(ClassRoom::getRRoom, BookingInfo::getRRoom)
                        .selectAs(ClassRoom::getRBuilding, BookingInfo::getRBuilding)
//                        .leftJoin(Booking.class, Booking::getSeatId, Seat::getSeatId)
                        .innerJoin(Seat.class,Seat::getSeatId,Booking::getSeatId)//指定要连接的表，设置表相等字段
                        .innerJoin(ClassRoom.class, ClassRoom::getRId,Seat::getRId)
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
                if(j==absences.size()-1){
                    bookingInfos.get(i).setTodayStatus(true);
                }
            }
        }
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
            Date date = new Date(parse.getTime() + 6 * 24 * 60 * 60 * 1000);
            String format1 = format.format(date);
            booking.setBEndTime(format1);
            i = bookingMapper.updateById(booking);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return i;
    }
}
