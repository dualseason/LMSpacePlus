package edu.lingnan.controller;

import edu.lingnan.dto.result.StudentBookingInfo;
import edu.lingnan.dto.result.StudentRecordInfo;
import edu.lingnan.entity.Record;
import edu.lingnan.service.BookingService;
import edu.lingnan.service.RecordService;
import edu.lingnan.vo.Result;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

@RestController
public class RecordController {
    @Resource
    private RecordService recordService;
    @Resource
    private BookingService bookingService;
    @GetMapping("/records")
    public Result findAll() {
        List<Record> list = recordService.list();
        return new Result(true, list, "操作成功");
    }

    /**
     * 请假
     * @return
     */
    @ApiOperation("学生请假，生成一条请假记录")
    @PostMapping("/vacation")
    public Result vacation(@RequestBody Record record){
        HashMap<String, Object> map = new HashMap<>();
        map.put("b_id",record.getBId());
        map.put("b_useFul",1);
        boolean b = bookingService.queryBookingAbleUseful(map);
        if(!b){
            return new Result(false,"null","操作失败，没有该预约记录或预约记录已到时间");
        }
        Boolean canOrNotRecord = recordService.queryStudentCanOrNotRecord(record);
        if(!canOrNotRecord){
            return new Result(false,0,"操作失败，该学生已有在该时间段的请假记录");
        }
        boolean save = recordService.save(record);
        if (save){
            return new Result(true,1,"操作成功");
        }
        return new Result(false,0,"操作失败");
    }

    /**
     * 获得请假记录列表
     * @param sid
     * @return
     */
    @GetMapping("/getVocationList/{id}")
    public Result getVocationList(@PathVariable("id") String sid){
        List<Record> recordBySId = recordService.getRecordBySId(sid);
        if (!CollectionUtils.isEmpty(recordBySId)){
            return new Result(true,recordBySId,"操作成功");
        }
        return new Result(false,null,"操作失败");
    }
    @ApiOperation(value = "学生请假，先得到学生的预约信息")
    @GetMapping("/record/getStudentBookingInfo/{sId}")
    public Result getStudentBookingInfo(@PathVariable("sId") String sId){
        boolean b = bookingService.queryStudentUserfulBookingInfo(sId);
        if(!b){
            return new Result(false,"null","该学生没有预约到座位");
        }
        StudentBookingInfo studentBookingInfo = recordService.getStudentBookingInfo(sId);
        return new Result(true,studentBookingInfo,"操作成功");
    }
    @ApiOperation(value = "学生请假，获取学生请假列表")
    @GetMapping("/record/getStudentRecordList/{sId}")
    public Result getStudentRecordList(@PathVariable("sId") String sId){
        List<StudentRecordInfo> studentRecordList = recordService.getStudentRecordList(sId);
            return new Result(true,studentRecordList,"操作成功");

    }

}
