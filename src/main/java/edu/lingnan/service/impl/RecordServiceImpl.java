package edu.lingnan.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.lingnan.entity.Booking;
import edu.lingnan.entity.Record;
import edu.lingnan.mapper.BookingMapper;
import edu.lingnan.mapper.RecordMapper;
import edu.lingnan.service.RecordService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
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
}
