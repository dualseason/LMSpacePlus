package edu.lingnan.controller;

import edu.lingnan.dto.AbsenceReq;
import edu.lingnan.dto.AbsenceReq2;
import edu.lingnan.dto.result.BookingInfo;
import edu.lingnan.entity.Absence;
import edu.lingnan.entity.Booking;
import edu.lingnan.entity.ClassRoom;
import edu.lingnan.entity.Record;
import edu.lingnan.mapper.RecordMapper;
import edu.lingnan.service.AbsenceService;
import edu.lingnan.service.BookingService;
import edu.lingnan.service.ClassRoomService;
import edu.lingnan.util.DateUtil;
import edu.lingnan.vo.Result;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class AbsenceController {
    @Resource
    private AbsenceService absenceService;

    @Resource
    private BookingService bookingService;

    @Resource
    private ClassRoomService classRoomService;

    @Resource
    private RecordMapper recordMapper;
    /**
     * 查找所有用户
     * @return
     */
    @ApiOperation("查询所有的缺勤信息")
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
    @ApiOperation(value = "考勤操作")
    @PostMapping("/addOrDeleteAbsence")
    public Result addOrDeleteAbsence(@RequestBody AbsenceReq2 absenceReq2){
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("b_id",absenceReq2.getBId());
        map.put("b_useFul",1);
        boolean b1 = bookingService.queryBookingAbleUseful(map);
        if(!b1){
            return new Result(false,0,"该预约记录不存在或已失效");
        }
        if("false".equals(absenceReq2.getTodayStatus())){
            //******************
            HashMap<String, Object> map2 = new HashMap<>();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-M-d");
                map2.put("b_id",absenceReq2.getBId());
                List<Record> records = recordMapper.selectByMap(map2);
                String format1 = format.format(new Date());
                for (int j = 0; j < records.size(); j++) {
                    try {
                        Date parse = format.parse(records.get(j).getReStartTime());
                        Date parse1 = format.parse(format1);
                        //如果当天时间在请假时间之间
                        if((parse.getTime() + Integer.valueOf(records.get(j).getReDays())*24*60*60*1000) >= parse1.getTime()&&parse1.getTime()>=parse.getTime()){
                            return new Result(false,null,"该学生已经请假，操作失败");
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            List<Absence> list = absenceService.list();
            for (Absence absence : list) {
                if(absence.getBId()==absence.getBId() && absence.getATime().equals(format1)){
                    return new Result(false,null,"该学生在当天已经有缺勤记录");
                }
            }
            //******************
            Absence absence = new Absence();
            absence.setATime(format.format(new Date()));
            absence.setBId(absenceReq2.getBId());
            boolean save = absenceService.save(absence);

            return new Result(true,1,"操作成功");


        }else {
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("b_id",absenceReq2.getBId());
            SimpleDateFormat format = new SimpleDateFormat("yyyy-M-d");
            hashMap.put("a_time",format.format(new Date()));
            boolean b = absenceService.removeByMap(hashMap);
            return new Result(true,1,"操作成功");
        }
    }
    @ApiOperation("考勤操作，首先得到已开放的教室列表")
    @GetMapping("/getAllOpenClassroomsList")
    public Result getAllOpenClassroomsList(){
        List<ClassRoom> list2 = classRoomService.findUsefulClassRoomsList2();
        if(list2.size()>0){
            return new Result(true,list2,"操作成功");
        }else {
            return new Result(false,0,"找不到已开放的教室列表");
        }
    }

}
