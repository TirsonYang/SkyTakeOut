package com.sky.controller.admin;


import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/setmeal")
@Slf4j
@Api(tags = "套餐相关接口")
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    /**
     * 根据id查询套餐
     * @param id
     * @return
     */
    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
    @ApiOperation("根据id查询套餐")
    public Result<SetmealVO> getById(@PathVariable Long id){
        log.info("查询id为：{}的套餐",id);
        SetmealVO setmealVO=setmealService.getById(id);
        return Result.success(setmealVO);
    }

    /**
     * 套餐分页查询
     * @param setmealPageQueryDTO
     * @return
     */
    @RequestMapping(method = RequestMethod.GET,value = "/page")
    @ApiOperation("套餐分页查询")
    public Result page(SetmealPageQueryDTO setmealPageQueryDTO){
        log.info("套餐分页查询");
        PageResult pageResult=setmealService.pageQuery(setmealPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 新增套餐
     * @param setmealDTO
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    @ApiOperation("新增套餐")
    public Result save(@RequestBody SetmealDTO setmealDTO){
        log.info("新增套餐：{}",setmealDTO);
        setmealService.saveWithDishes(setmealDTO);
        return Result.success();
    }


    @RequestMapping(value = "/status/{status}",method = RequestMethod.POST)
    @ApiOperation("套餐起售与停售")
    public Result startOrStop(@PathVariable Integer status,Long id){
        log.info("id为：{}的套餐需要起售或停售：{}",id,status);
        setmealService.startOrStop(status,id);
        return Result.success();
    }
}
