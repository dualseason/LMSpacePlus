package edu.lingnan.controller;

import edu.lingnan.dto.GenSeatReq;
import edu.lingnan.dto.SeatReturn;
import edu.lingnan.dto.result.StudentBookingInfo;
import edu.lingnan.entity.Booking;
import edu.lingnan.entity.Seat;
import edu.lingnan.service.BookingService;
import edu.lingnan.service.ClassRoomService;
import edu.lingnan.service.RecordService;
import edu.lingnan.service.SeatService;
import edu.lingnan.vo.Result;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@RestController
public class SeatController {
    @Resource
    private SeatService seatService;

    @Resource
    private BookingService bookingService;

    @Resource
    private ClassRoomService room;

    @Resource
    private RecordService recordService;

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
    @ApiOperation(value = "归还座位：先得到座位相关信息")
    @GetMapping("/seat/returnSeatFirst/{sId}")
    public Result returnSeatFirst(@PathVariable("sId") String sId){
        boolean b = bookingService.queryStudentUserfulBookingInfo(sId);
        if(!b){
            return new Result(false,"null","该学生没有预约到座位");
        }
        StudentBookingInfo bookingInfo = recordService.getStudentBookingInfo(sId);
        return new Result(true,bookingInfo,"操作成功");
    }
    @ApiOperation(value = "归还座位：根据预约编号归还座位")
    @PostMapping("/seat/returnSeatSecond")
    public Result returnSeatSecond(@RequestBody SeatReturn seatReturn){
        HashMap<String, Object> map = new HashMap<>();
        map.put("b_id",seatReturn.getBId());
        map.put("b_useFul",1);
        boolean b = bookingService.queryBookingAbleUseful(map);
        if(!b){
            return new Result(false,"null","操作失败，该预约座位不存在或预约信息已失效");
        }
        Booking booking = new Booking();
        booking.setBEndTime(seatReturn.getBEndTime());
        booking.setBId(seatReturn.getBId());
        int i = seatService.returnSeat2(booking);
        if(i > 0)
        {
            return new Result(true,1,"操作成功");
        }
        return new Result(false,0,"操作失败");
    }
}
