package edu.lingnan.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.lingnan.entity.Absence;
import edu.lingnan.mapper.AbsenceMapper;
import edu.lingnan.service.AbsenceService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AbsenceServiceImpl extends ServiceImpl<AbsenceMapper, Absence> implements AbsenceService {

}
