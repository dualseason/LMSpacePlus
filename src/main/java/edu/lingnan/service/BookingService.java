package edu.lingnan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import edu.lingnan.dto.result.BookingInfo;
import edu.lingnan.entity.Booking;

import java.util.HashMap;
import java.util.List;

public interface BookingService extends IService<Booking> {
    List<BookingInfo> queryUserfulBookingList();
    boolean queryStudentUserfulBookingInfo(String sId);
    long[] getTotalBookingDays(String sId);
    long[] getTotalBookingDays2(String sId);
    int renewal(Integer bId);
    boolean queryBookingAbleUseful(HashMap<String,Object> map);
    List<Booking> updateBookingInfoTiming();
}
