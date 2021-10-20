package edu.lingnan.controller;

import edu.lingnan.entity.Record;
import edu.lingnan.service.RecordService;
import edu.lingnan.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class RecordController {
    @Autowired
    private RecordService recordService;

    @GetMapping("/records")
    public Result findAll() {
        List<Record> list = recordService.list();
        return new Result(true, list, "操作成功");
    }

    /**
     * 请假
     * @return
     */
    @PostMapping("/vacation")
    public Result vacation(@RequestBody Record record){
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

}
