package edu.lingnan.controller;

import edu.lingnan.dto.GenSeatReq;
import edu.lingnan.entity.Booking;
import edu.lingnan.entity.Seat;
import edu.lingnan.service.BookingService;
import edu.lingnan.service.ClassRoomService;
import edu.lingnan.service.SeatService;
import edu.lingnan.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author dualseason
 */
@CrossOrigin(origins = "*",maxAge = 3600)
@RestController
public class SeatController {
    @Autowired
    private SeatService seatService;

    @Autowired
    private BookingService bookingService;

    @Autowired
    private ClassRoomService room;

    @GetMapping("/seats")
    public Result findAll() {
        List<Seat> list = seatService.list();
        return new Result(true, list, "操作成功");
    }

    @PostMapping("/seat/genSeat")
    @Transactional
    public Result genSeat(@RequestBody GenSeatReq req){
        List<Seat> accessibleSeat = seatService.getAccessibleSeat();
        if (!CollectionUtils.isEmpty(accessibleSeat)){
            List<Seat> collect = accessibleSeat.stream()
                    .filter(seat -> seat.getRId().equals(req.getRId()))
                    .collect(Collectors.toList());
            Collections.shuffle(collect);
            Seat seat = null;
            if (!CollectionUtils.isEmpty(collect)){
                seat = collect.get(0);
            }
            if (seat!=null){
                //获得申请座位信息
                Booking booking = new Booking();
                booking.setSId(req.getSId());
                long now = System.currentTimeMillis();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                booking.setBStartTime(sdf.format(now));
                GregorianCalendar calendar = new GregorianCalendar();
                calendar.add(Calendar.DATE,req.getDays());
                Date time = calendar.getTime();
                booking.setBEndTime(sdf.format(time));
                booking.setSeatId(seat.getSeatId());
                booking.setBUseful(true);
                boolean save = bookingService.save(booking);
                if (save){
                    room.subtract1(req.getRId());
                    //修改座位状态
                    Seat seatServiceById = seatService.getById(seat.getSeatId());
                    seatServiceById.setSeatStatus("0");
                    seatService.updateById(seatServiceById);
                    return new Result(true,seatServiceById,"操作成功");
                }else {
                    //强行手动事务回滚
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    return new Result(false,seat,"该座位预约失败");
                }
            }else {
                return new Result(false,null,"该教室无空座");
            }
        }else {
            return new Result(false,null,"目前无空座位");
        }
    }
    @PostMapping("/renewalSeat")
    public Result renewalSeat(@RequestBody Booking booking){
        boolean save = bookingService.save(booking);
        if (save){
            return new Result(true,1,"操作成功");
        }else {
            return new Result(false,0,"操作失败");
        }
    }
    @PostMapping("/seat/cancel")
    public Result cancel(@RequestBody Booking booking){
        booking.setBUseful(false);
        boolean update = bookingService.updateById(booking);
        if (update){
            return new Result(true,1,"操作成功");
        }
        return new Result(false,0,"操作失败");
    }
}
