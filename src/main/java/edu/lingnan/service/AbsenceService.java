package edu.lingnan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import edu.lingnan.entity.Absence;

import java.util.Collection;

public interface AbsenceService extends IService<Absence> {
    Long getTotalAbsenceDays(Collection<Integer> bIds);
}
