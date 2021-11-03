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
import edu.lingnan.service.RecordService;
import edu.lingnan.service.StudentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
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
        QueryWrapper<Record> wrapper = new QueryWrapper<>();
        wrapper.in("b_id",bIds);
        List<Record> records = recordMapper.selectList(wrapper);
        long recordDays = 0;
        for (Record record : records) {
            long aLong = Long.parseLong(record.getReDays());
            recordDays += aLong;
        }
        return recordDays;
    }
}
