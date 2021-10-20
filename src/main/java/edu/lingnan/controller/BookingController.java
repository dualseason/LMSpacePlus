package edu.lingnan.controller;

import edu.lingnan.entity.Booking;
import edu.lingnan.service.BookingService;
import edu.lingnan.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.ResultSet;
import java.util.List;

@RestController
public class BookingController {
    @Autowired
    private BookingService bookingService;

    /**
     * 查找所有预定记录
     * @return
     */
    @GetMapping("/bookings")
    public Result findAll() {
        List<Booking> list = bookingService.list();
        return new Result(true, list, "操作成功");
    }

}
