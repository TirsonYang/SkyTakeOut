package com.sky.mapper;

import com.sky.entity.SetmealDish;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetmealDishMapper {

    void insertBatch(List<SetmealDish> setmealDishes);

    @Delete("delete from sky_take_out.setmeal_dish where setmeal_id = #{id}")
    void deleteBatchById(Long id);

    List<SetmealDish> getByDishId(Long id);
}
