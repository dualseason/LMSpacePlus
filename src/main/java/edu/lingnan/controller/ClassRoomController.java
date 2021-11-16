package edu.lingnan.controller;

import edu.lingnan.dto.ClassRoomReq;
import edu.lingnan.entity.ClassRoom;
import edu.lingnan.service.BookingService;
import edu.lingnan.service.ClassRoomService;
import edu.lingnan.vo.Result;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class ClassRoomController {
    @Autowired
    private ClassRoomService classRoomService;
    @ApiOperation(value = "管理教室，查看所有教室信息")
    @GetMapping("/classRooms")
    public Result findAll() {
        List<ClassRoom> list = classRoomService.list();
        return new Result(true, list, "操作成功");
    }

    @GetMapping("/findAllAccessible")
    public Result findAllAccessible() {
        List<ClassRoom> list = classRoomService.list();
        if (!CollectionUtils.isEmpty(list)) {
            List<ClassRoom> collect = list.stream()
                    .filter(room -> "1".equals(room.getRStatus()))
                    .collect(Collectors.toList());
            return new Result(true, collect, "操作成功");
        } else {
            return new Result(false, null, "操作失败");
        }
    }

    /**
     * 开放教室
     *
     * @return
     */
    @PostMapping("/openClassRoom")
    public Result openClassRoom(@RequestBody ClassRoom classRoom) {
        boolean update = classRoomService.updateById(classRoom);
        if (update) {
            return new Result(true, 1, "操作成功");
        }
        return new Result(false, 0, "操作失败");
    }
    @ApiOperation(value = "管理教室页面,用于打开或关闭教室")
    @PostMapping("/openOrCloseRoom")
    public Result openOrCloseRoom(@RequestBody ClassRoom classRoom) {
        if("0".equals(classRoom.getRStatus())){
            boolean b = classRoomService.checkClassRoomCanOrNotClose(classRoom);
            if(!b){
                return new Result(false,0,"该教室座位已被预约，暂时无法关闭");
            }
        }
        if("1".equals(classRoom.getRStatus())){
            boolean b = classRoomService.checkClassRoomCanOrNotClose(classRoom);
            if(!b){
                return new Result(false,0,"该教室座位打开不了");
            }
        }
        int i = classRoomService.openOrCloseClassRoom(classRoom);
        if (i > 0) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("r_id",classRoom.getRId());
            List<ClassRoom> classRooms = classRoomService.listByMap(map);

            return new Result(true, classRooms.get(0), "操作成功");
        } else {
            return new Result(false, 0, "操作失败");
        }
    }
    @ApiOperation(value = "增加一间教室")
    @PostMapping("/addOneClassRooms")
    public Result addOneClassRooms(@RequestBody ClassRoomReq classRoomReq){
        boolean b = classRoomService.checkClassRoomCanOrNotAdd(classRoomReq);
        if(!b){
            return new Result(false,0,"该教室已存在,添加失败");
        }
        ClassRoom classRoom = new ClassRoom();
        classRoom.setRRoom(classRoomReq.getRRoom());
        classRoom.setRNums(classRoomReq.getRNums());
        classRoom.setRBuilding(classRoomReq.getRBuilding());
        int i = classRoomService.addOneClassRooms(classRoom);
        if( i > 0){
            return new Result(true,1,"操作成功");
        }
        return new Result(false,0,"操作失败");
    }
    @ApiOperation(value = "删除一间教室")
    @GetMapping("/deleteOneClassRoom/{rId}")
    public Result deleteOneClassRoom(@PathVariable("rId") Integer rId){
        boolean b = classRoomService.deleteClassRoom(rId);
        if(b){
            return new Result(true,1,"操作成功");
        }
        return new Result(false,0,"删除失败，可能该教室已有预约信息");
    }


}
