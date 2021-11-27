package edu.lingnan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import edu.lingnan.dto.StudentReq;
import edu.lingnan.dto.result.StudentBookingInfo;
import edu.lingnan.entity.Booking;
import edu.lingnan.entity.Seat;

import java.util.List;

public interface SeatService extends IService<Seat> {
    /**
     * 获取所有可以预约的座位
     * @return
     */
    List<Seat> getAccessibleSeat();
    void provideOneSeatForStudent(StudentReq studentReq);
    int returnSeat(Booking booking);
    int returnSeat2(Booking booking);
}
