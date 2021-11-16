package edu.lingnan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import edu.lingnan.dto.result.StudentBookingInfo;
import edu.lingnan.dto.result.StudentRecordInfo;
import edu.lingnan.entity.Record;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public interface RecordService extends IService<Record> {
    List<Record> getRecordBySId(String sid);
    StudentBookingInfo getStudentBookingInfo(String sId);
    List<StudentRecordInfo> getStudentRecordList(String sId);
    Long getAllRecordDays(Collection<Integer> bIds);
    Long getUsefulBookingRecordDays(Integer bId);
    Boolean queryStudentCanOrNotRecord(Record record);
}
