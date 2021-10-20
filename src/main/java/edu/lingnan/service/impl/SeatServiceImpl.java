package edu.lingnan.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.lingnan.entity.Seat;
import edu.lingnan.mapper.SeatMapper;
import edu.lingnan.service.SeatService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

@Service
@Transactional
public class SeatServiceImpl extends ServiceImpl<SeatMapper, Seat> implements SeatService {
    @Resource
    private SeatMapper seatMapper;

    @Override
    public List<Seat> getAccessibleSeat() {
        HashMap<String, Object> map = new HashMap<>(1);
        map.put("seat_status","1");
        return seatMapper.selectByMap(map);
    }
}
