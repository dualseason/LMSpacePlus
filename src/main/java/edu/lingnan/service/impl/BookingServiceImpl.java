package edu.lingnan.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.lingnan.entity.Booking;
import edu.lingnan.mapper.BookingMapper;
import edu.lingnan.service.BookingService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class BookingServiceImpl extends ServiceImpl<BookingMapper, Booking> implements BookingService {
}
