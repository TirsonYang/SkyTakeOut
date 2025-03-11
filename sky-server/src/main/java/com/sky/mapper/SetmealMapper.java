package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.vo.SetmealVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface SetmealMapper {

    @Select("select count(id) from sky_take_out.setmeal where category_id = #{categoryId}")
    Integer countByCategoryId(Long id);

    /**
     * 根据id查询套餐
     * @param id
     * @return
     */

    SetmealVO getById(Long id);

    /**
     * 分页查询
     * @param setmealPageQueryDTO
     * @return
     */
    Page<SetmealVO> pageQuery(SetmealPageQueryDTO setmealPageQueryDTO);

    /**
     * 新增套餐
     *
     * @param setmeal
     * @return
     */

    Long insert(Setmeal setmeal);

    @Select("select * from sky_take_out.setmeal where id = #{id}")
    Setmeal queryWithDishes(Long id);

    @Select("update sky_take_out.setmeal set status = #{status} where id = #{id}")
    void updateStatus(Integer status, Long id);

    @Delete("delete from sky_take_out.setmeal where id = #{id}")
    void deleteBatchById(Long id);

    void update(Setmeal setmeal);
}
