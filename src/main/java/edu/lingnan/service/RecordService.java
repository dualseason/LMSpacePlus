package edu.lingnan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import edu.lingnan.entity.Record;

import java.util.List;

public interface RecordService extends IService<Record> {
    List<Record> getRecordBySId(String sid);
}
