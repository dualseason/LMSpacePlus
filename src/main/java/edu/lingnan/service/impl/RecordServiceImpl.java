package edu.lingnan.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import edu.lingnan.dto.result.StudentBookingInfo;
import edu.lingnan.dto.result.StudentRecordInfo;
import edu.lingnan.entity.*;
import edu.lingnan.entity.Record;
import edu.lingnan.mapper.BookingMapper;
import edu.lingnan.mapper.RecordMapper;
import edu.lingnan.service.BookingService;
import edu.lingnan.service.RecordService;
import edu.lingnan.service.StudentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class RecordServiceImpl extends ServiceImpl<RecordMapper, Record> implements RecordService {

    @Resource
    private RecordMapper recordMapper;

    @Resource
    private BookingMapper bookingMapper;

    @Resource
    private StudentService studentService;

    @Override
    public List<Record> getRecordBySId(String id) {
        HashMap<String, Object> map = new HashMap<>(1);
        HashMap<String, Object> hashMap = new HashMap<>(1);
        List<Record> records = new ArrayList<Record>();
        map.put("s_id",id);
        List<Booking> bookings = bookingMapper.selectByMap(map);
        bookings.stream()
                .map(Booking::getBId)
                .forEach(integer -> {
                    hashMap.clear();
                    hashMap.put("b_id",integer);
                    records.addAll(recordMapper.selectByMap(hashMap));
                } );
        return records;
    }
    //得到要请假学生的预约信息
    @Override
    public StudentBookingInfo getStudentBookingInfo(String sId) {
        StudentBookingInfo studentBookingInfo = studentService.queryUseFulStudentBookingInfo(sId);
        return studentBookingInfo;
    }

    @Override
    public List<StudentRecordInfo> getStudentRecordList(String sId) {
        List<StudentRecordInfo> studentRecordInfos = bookingMapper.selectJoinList(StudentRecordInfo.class,
                new MPJLambdaWrapper<>()
                        .selectAs(Student::getSId, StudentRecordInfo::getSId)
                        .selectAs(Student::getSClass, StudentRecordInfo::getSClass)
                        .selectAs(Student::getSCollege, StudentRecordInfo::getSCollege)
                        .selectAs(ClassRoom::getRBuilding, StudentRecordInfo::getRBuilding)
                        .selectAs(ClassRoom::getRRoom, StudentRecordInfo::getRRoom)
                        .selectAs(Seat::getSeatNum, StudentRecordInfo::getSeatNum)
                        .selectAs(Record::getReStartTime, StudentRecordInfo::getReStartTime)
                        .selectAs(Record::getReDays, StudentRecordInfo::getReDays)
                        .innerJoin(Student.class, Student::getSId, Booking::getSId)
                        .innerJoin(Record.class, Record::getBId, Booking::getBId)
                        .innerJoin(Seat.class, Seat::getSeatId, Booking::getSeatId)
                        .innerJoin(ClassRoom.class, ClassRoom::getRId, Seat::getRId)
                        .eq(Student::getSId, sId));
        return studentRecordInfos;
    }

    @Override
    public Long getAllRecordDays(Collection<Integer> bIds) {
        long recordDays = 0;
        if(bIds.size() > 0){
            QueryWrapper<Record> wrapper = new QueryWrapper<>();
            wrapper.in("b_id",bIds);
            List<Record> records = recordMapper.selectList(wrapper);
            for (Record record : records) {
                long aLong = Long.parseLong(record.getReDays());
                recordDays += aLong;
            }
        }

        return recordDays;
    }

    @Override
    public Long getUsefulBookingRecordDays(Integer bId) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("b_id",bId);
        Long recordDays = (long) 0;
        //①通过预约编号查请假表
        List<Record> records = recordMapper.selectByMap(map);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-M-d");
        String format1 = format.format(new Date());
        try {
            Date currentTime = format.parse(format1);
            for (Record record : records) {
                Date startTime = format.parse(record.getReStartTime());
//                ②找请假开始时间小于当天时间
                if(startTime.getTime() < currentTime.getTime()){
                    long endTime = startTime.getTime() + (Integer.valueOf(record.getReDays())-1)*24*60*60*1000;
//                    ③如果结束时间小于当天时间
                    if(endTime < currentTime.getTime()){
                        recordDays += Integer.valueOf(record.getReDays());
                    }else {
                        long days = (currentTime.getTime() - startTime.getTime())/24/60/60/1000;
                        recordDays += days;
                    }
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return recordDays;
    }

    //查询该预约编号对应的学生能否请假，如果该学生已在请假的时间段内请过假，就请假失败，防止学生无限请假
    @Override
    public Boolean queryStudentCanOrNotRecord(Record record) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("b_id",record.getBId());
        List<Record> records = recordMapper.selectByMap(map);

        SimpleDateFormat format = new SimpleDateFormat("yyyy-M-d");
        try {
            Date parse = format.parse(record.getReStartTime());
            Long endTime = parse.getTime() + (Integer.valueOf(record.getReDays()) - 1)*24*60*60*1000;
            for (Record record1 : records) {
                String startTime = record1.getReStartTime();
                String days = record1.getReDays();
                Date parse1 = format.parse(startTime);
                if((parse1.getTime()+(Integer.valueOf(days) - 1)*24*60*60*1000)>=parse.getTime() && endTime >=parse1.getTime()){
                    return false;
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return true;
    }
}
