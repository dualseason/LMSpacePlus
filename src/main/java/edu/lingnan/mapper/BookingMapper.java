package edu.lingnan.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import edu.lingnan.entity.Booking;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BookingMapper extends BaseMapper<Booking> {
}
