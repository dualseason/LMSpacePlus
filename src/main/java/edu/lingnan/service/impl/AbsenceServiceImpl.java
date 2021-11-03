package edu.lingnan.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.lingnan.entity.Absence;
import edu.lingnan.mapper.AbsenceMapper;
import edu.lingnan.service.AbsenceService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

@Service
@Transactional
public class AbsenceServiceImpl extends ServiceImpl<AbsenceMapper, Absence> implements AbsenceService {
    @Resource
    private AbsenceMapper absenceMapper;
    @Override
    public Long getTotalAbsenceDays(Collection<Integer> bIds) {
        QueryWrapper<Absence> wrapper = new QueryWrapper<>();
        wrapper.in("b_id",bIds);
        List<Absence> absences = absenceMapper.selectList(wrapper);
        long totalDays = (long) absences.size();
        return totalDays;
    }
}
