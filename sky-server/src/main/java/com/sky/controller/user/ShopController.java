package com.sky.controller.user;

import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController("userShopController")
@Api(tags = "店铺相关接口")
@RequestMapping("/user/shop")
@Slf4j
public class ShopController {

    @Autowired
    private RedisTemplate redisTemplate;


    @RequestMapping(value="/status",method = RequestMethod.GET)
    @ApiOperation("查询店铺营业状态")
    public Result<Integer> getStatus(){
        log.info("查询店铺的营业状态");
        Integer shopStatus =(Integer)redisTemplate.opsForValue().get("SHOP_STATUS");
        log.info("获取当前店铺的营业状态为:{}",shopStatus==1 ? "营业中" : "打烊中");
        return Result.success(shopStatus);
    }

}
