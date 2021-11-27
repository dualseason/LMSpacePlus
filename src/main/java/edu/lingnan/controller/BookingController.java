package edu.lingnan.controller;

import edu.lingnan.dto.result.BookingInfo;
import edu.lingnan.dto.result.StudentBookingInfo;
import edu.lingnan.entity.Booking;
import edu.lingnan.service.AbsenceService;
import edu.lingnan.service.BookingService;
import edu.lingnan.service.RecordService;
import edu.lingnan.service.StudentService;
import edu.lingnan.vo.Result;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

@RestController
public class BookingController {
    @Resource
    private BookingService bookingService;
    @Resource
    private StudentService studentService;
    @Resource
    private AbsenceService absenceService;
    @Resource
    private RecordService recordService;

    /**
     * 查找所有预定记录
     * @return
     */
    @ApiOperation("查询所有的预约记录")
    @GetMapping("/bookings")
    public Result findAll() {
        List<Booking> list = bookingService.list();
        return new Result(true, list, "操作成功");
    }
    @ApiOperation(value = "查询所有有效的预约记录，用于考勤")
    @GetMapping("/queryUserfulBookingList/{rId}")
    public Result queryUserfulBookingList(@PathVariable("rId") Integer rId){
        List<BookingInfo> bookingInfos = bookingService.queryUserfulBookingList2();
        for (int i = 0; i < bookingInfos.size(); i++) {
            BookingInfo info = bookingInfos.get(i);
            if(info.getRId() != rId){
                bookingInfos.remove(i);
                i--;
            }

        }
        if(!CollectionUtils.isEmpty(bookingInfos)){
            return new Result(true,bookingInfos,"操作成功");
        }
        else {
            return new Result(false,"null","操作失败");
        }
    }
    @ApiOperation(value = "续约界面：提供续约的相关信息")
    @GetMapping("/booking/provideRenewalStudentInfo/{sId}")
    public Result provideRenewalStudentInfo(@PathVariable("sId") String sId){
        boolean b = bookingService.queryStudentUserfulBookingInfo(sId);
        if(!b){
            return new Result(false,"null","该学生没有预约到座位");
        }
        StudentBookingInfo studentBookingInfo = studentService.queryUseFulStudentBookingInfo(sId);
        long[] longs = bookingService.getTotalBookingDays2(sId);
        studentBookingInfo.setTotalBookingDays(longs[0]);

        studentBookingInfo.setActualBookingDays(longs[0] - longs[1]);

        return new Result(true,studentBookingInfo,"操作成功");
    }
    @ApiOperation(value = "续约界面：续约")
    @GetMapping("/booking/renewal/{bId}")
    public Result renewal(@PathVariable("bId") Integer bId){
        HashMap<String, Object> map = new HashMap<>();
        map.put("b_id",bId);
        map.put("b_useFul",1);
        boolean b = bookingService.queryBookingAbleUseful(map);
        if(!b){
            return new Result(false,"null","操作失败，不存在该预约编号或该预约编号已失效");
        }
        int i = bookingService.renewal(bId);
        if(i > 0)
        {
            return new Result(true,1,"操作成功");
        }

        return new Result(false,0,"操作失败");
    }
}
