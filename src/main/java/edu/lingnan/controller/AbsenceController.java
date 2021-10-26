package edu.lingnan.controller;

import edu.lingnan.dto.AbsenceReq;
import edu.lingnan.entity.Absence;
import edu.lingnan.entity.Booking;
import edu.lingnan.service.AbsenceService;
import edu.lingnan.service.BookingService;
import edu.lingnan.util.DateUtil;
import edu.lingnan.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author dualseason
 */
@CrossOrigin(origins = "*",maxAge = 3600)
@RestController
public class AbsenceController {
    @Autowired
    private AbsenceService absenceService;

    @Autowired
    private BookingService bookingService;

    /**
     * 查找所有用户
     * @return
     */
    @GetMapping("/absences")
    public Result findAll() {
        List<Absence> list = absenceService.list();
        return new Result(true, list, "操作成功");
    }

    @PostMapping("/absence")
    public Result absence(@RequestBody AbsenceReq absence){
        // 1. 通过座位Id获取到bId
        List<Integer> collect = bookingService.list()
                .stream()
                .filter(booking -> booking.getSeatId().equals(absence.getSeatId()))
                .filter(booking -> DateUtil.isBetween2Date(absence.getATime(), booking.getBStartTime(), booking.getBEndTime()))
                .map(Booking::getBId)
                .collect(Collectors.toList());
        if (collect.size()>1||collect.size()==0){
            return new Result(false,0,"操作失败");
        }
        Absence temp = new Absence();
        temp.setATime(absence.getATime());
        temp.setBId(collect.get(0));
        boolean save = absenceService.save(temp);
        if (save){
            return new Result(true,1,"操作成功");
        }
        return new Result(false,0,"操作失败");
    }
}
