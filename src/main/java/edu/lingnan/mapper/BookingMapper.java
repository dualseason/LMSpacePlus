package edu.lingnan.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.yulichang.base.MPJBaseMapper;
import edu.lingnan.dto.result.BookingInfo;
import edu.lingnan.entity.Booking;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BookingMapper extends MPJBaseMapper<Booking> {
    /**
     * 查询现阶段正在被预约的记录
     * @return
     */
//    List<BookingInfo> queryUserfulBookingList();
}
