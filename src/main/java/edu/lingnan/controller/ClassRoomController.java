package edu.lingnan.controller;

import edu.lingnan.entity.ClassRoom;
import edu.lingnan.service.ClassRoomService;
import edu.lingnan.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


/**
 * @author dualseason
 */
@CrossOrigin(origins = "*",maxAge = 3600)
@RestController
public class ClassRoomController {
    @Autowired
    private ClassRoomService classRoomService;

    @GetMapping("/classRooms")
    public Result findAll() {
        List<ClassRoom> list = classRoomService.list();
        return new Result(true, list, "操作成功");
    }
    @GetMapping("/findAllAccessible")
    public Result findAllAccessible() {
        List<ClassRoom> list = classRoomService.list();
        if (!CollectionUtils.isEmpty(list)){
            List<ClassRoom> collect = list.stream()
                    .filter(room -> "1".equals(room.getRStatus()))
                    .collect(Collectors.toList());
            return new Result(true, collect, "操作成功");
        }else {
            return new Result(false,null,"操作失败");
        }
    }

    /**
     * 开放教室
     * @return
     */
    @PostMapping("/openClassRoom")
    public Result openClassRoom(@RequestBody ClassRoom classRoom){
        boolean update = classRoomService.updateById(classRoom);
        if (update){
            return new Result(true,1,"操作成功");
        }
        return new Result(false,0,"操作失败");
    }

}
