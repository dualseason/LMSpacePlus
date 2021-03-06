package edu.lingnan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import edu.lingnan.dto.result.BookingInfo;
import edu.lingnan.entity.Absence;

import java.util.Collection;
import java.util.List;

public interface AbsenceService extends IService<Absence> {
    Long getTotalAbsenceDays(Collection<Integer> bIds);
    Long getUsefulBookingAbsenceDays(Integer bid);
    List<BookingInfo> getCurrentNoRecordBookingList(List<BookingInfo> bookingInfos);
    List<BookingInfo> getCurrentNoRecordBookingList2(List<BookingInfo> bookingInfos);
}
