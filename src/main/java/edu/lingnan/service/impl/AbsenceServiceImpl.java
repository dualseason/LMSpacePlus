package edu.lingnan.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.lingnan.dto.result.BookingInfo;
import edu.lingnan.entity.Absence;
import edu.lingnan.entity.Record;
import edu.lingnan.mapper.AbsenceMapper;
import edu.lingnan.mapper.RecordMapper;
import edu.lingnan.service.AbsenceService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Transactional
public class AbsenceServiceImpl extends ServiceImpl<AbsenceMapper, Absence> implements AbsenceService {
    @Resource
    private AbsenceMapper absenceMapper;
    @Resource
    private RecordMapper recordMapper;
    @Override
    public Long getTotalAbsenceDays(Collection<Integer> bIds) {
        long totalDays = 0;
        if(bIds.size() > 0){
            QueryWrapper<Absence> wrapper = new QueryWrapper<>();
            wrapper.in("b_id",bIds);
            List<Absence> absences = absenceMapper.selectList(wrapper);
            totalDays = (long) absences.size();
        }

        return totalDays;
    }

    @Override
    public Long getUsefulBookingAbsenceDays(Integer bid) {

        Long absenceDays = (long)0;
        HashMap<String, Object> map = new HashMap<>();
        map.put("b_id",bid);
        List<Absence> absences = absenceMapper.selectByMap(map);
        absenceDays =(long) absences.size();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-M-d");
        String format1 = format.format(new Date());
        for (int i = 0; i < absences.size(); i++) {
            if(format1.equals(absences.get(i).getATime())){
                absenceDays -= 1;
            }
        }
        return absenceDays;
    }

    //从当天的预约列表中得到当天没有请假的预约列表，因为只有当天没有请假的预约才能够让其缺勤
    @Override
    public List<BookingInfo> getCurrentNoRecordBookingList(List<BookingInfo> bookingInfos) {
        HashMap<String, Object> map = new HashMap<>();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-M-d");
        int count = bookingInfos.size();
        for (int i = 0; i < bookingInfos.size(); i++) {
            BookingInfo bookingInfo = bookingInfos.get(i);
            map.put("b_id",bookingInfo.getBId());
            List<Record> records = recordMapper.selectByMap(map);
            String format1 = format.format(new Date());

            for (int j = 0; j < records.size(); j++) {
                try {
                    Date parse = format.parse(records.get(j).getReStartTime());
                    Date parse1 = format.parse(format1);
                    //如果当天时间在请假时间之间
                    if((parse.getTime() + Integer.valueOf(records.get(j).getReDays())*24*60*60*1000) >= parse1.getTime()&&parse1.getTime()>=parse.getTime()){
                        bookingInfos.remove(i);
                        i--;
                        break;
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        return bookingInfos;
    }

    @Override
    public List<BookingInfo> getCurrentNoRecordBookingList2(List<BookingInfo> bookingInfos) {
        HashMap<String, Object> map = new HashMap<>();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-M-d");
        int count = bookingInfos.size();
        for (int i = 0; i < bookingInfos.size(); i++) {
            BookingInfo bookingInfo = bookingInfos.get(i);
            bookingInfo.setTodayRecord(false);
            map.put("b_id",bookingInfo.getBId());
            List<Record> records = recordMapper.selectByMap(map);
            String format1 = format.format(new Date());

            for (int j = 0; j < records.size(); j++) {
                try {
                    Date parse = format.parse(records.get(j).getReStartTime());
                    Date parse1 = format.parse(format1);
                    //如果当天时间在请假时间之间
                    if((parse.getTime() + (Integer.valueOf(records.get(j).getReDays())-1)*24*60*60*1000) >= parse1.getTime()&&parse1.getTime()>=parse.getTime()){
                        bookingInfo.setTodayRecord(true);
                        bookingInfo.setTodayStatus(false);
                        break;
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        return bookingInfos;
    }
}
