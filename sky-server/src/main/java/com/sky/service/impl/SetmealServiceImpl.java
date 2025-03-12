package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.exception.SetmealEnableFailedException;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetmealService;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SetmealServiceImpl implements SetmealService {


    @Autowired
    private SetmealMapper setmealMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;
    @Autowired
    private DishMapper dishMapper;


    /**
     * 根据id查询套餐
     * @param id
     * @return
     */
    @Override
    public SetmealVO getById(Long id) {

        List<SetmealDish> setmealDishes=setmealDishMapper.getByDishId(id);

        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println(setmealDishes);
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        SetmealVO setmealVO = setmealMapper.getById(id);
        setmealVO.setSetmealDishes(setmealDishes);

        return setmealVO;
    }

    /**
     * 套餐分页查询
     * @param setmealPageQueryDTO
     * @return
     */
    @Override
    public PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO) {
        PageHelper.startPage(setmealPageQueryDTO.getPage(), setmealPageQueryDTO.getPageSize());
        Page<SetmealVO> page=setmealMapper.pageQuery(setmealPageQueryDTO);
        return new PageResult(page.getTotal(),page.getResult());
    }

    /**
     * 新增套餐
     * @param setmealDTO
     */
    @Override
    public void saveWithDishes(SetmealDTO setmealDTO) {
        //使用setmeal对象
        Setmeal setmeal=new Setmeal();

        // 属性拷贝和赋值
        BeanUtils.copyProperties(setmealDTO,setmeal);
        setmeal.setUpdateUser(BaseContext.getCurrentId());
        setmeal.setCreateUser(BaseContext.getCurrentId());
        setmeal.setUpdateTime(LocalDateTime.now());
        setmeal.setCreateTime(LocalDateTime.now());

        setmealMapper.insert(setmeal);

        //获取生成的套餐id
        Long id= setmeal.getId();
        //修改套餐中的菜品id
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        for (int i = 0; i < setmealDishes.size(); i++) {
            setmealDishes.get(i).setSetmealId(id);
        }
        //保存套餐和菜品的关联关系
        setmealDishMapper.insertBatch(setmealDishes);
    }

    @Override
    public void startOrStop(Integer status, Long id) {
        if (status == StatusConstant.ENABLE){
            List<Dish> setmealDishes=dishMapper.getBySetmealId(id);
            for (int i = 0; i < setmealDishes.size(); i++) {
                if (setmealDishes.get(i).getStatus()==StatusConstant.ENABLE){
                    throw new SetmealEnableFailedException(MessageConstant.SETMEAL_ENABLE_FAILED);
                }
            }
        }
        setmealMapper.updateStatus(status,id);
    }

    /**
     * 批量删除套餐
     * @param ids
     */
    @Transactional
    @Override
    public void deleteBatch(List<Long> ids) {
        for(Long id:ids){
            Setmeal setmeal=setmealMapper.queryWithDishes(id);
            if (setmeal.getStatus()==StatusConstant.ENABLE){
                throw new SetmealEnableFailedException(MessageConstant.SETMEAL_ON_SALE);
            }
            setmealMapper.deleteBatchById(id);
            setmealDishMapper.deleteBatchById(id);
        }
    }

    @Override
    public void update(SetmealDTO setmealDTO) {
        //按照setmealDTO参数中的内容修改套餐表中内容
        Setmeal setmeal=new Setmeal();
        BeanUtils.copyProperties(setmealDTO,setmeal);
        setmeal.setUpdateTime(LocalDateTime.now());
        setmeal.setUpdateUser(BaseContext.getCurrentId());
        setmealMapper.update(setmeal);

        List<SetmealDish> setmealDishes=setmealDTO.getSetmealDishes();

        Long id=setmealDTO.getId();

        setmealMapper.deleteBatchById(id);

        for (int i = 0; i < setmealDishes.size(); i++) {
            setmealDishes.get(i).setSetmealId(id);
        }

        setmealDishMapper.insertBatch(setmealDishes);

    }

    /**
     * 条件查询
     * @param setmeal
     * @return
     */
    public List<Setmeal> list(Setmeal setmeal) {
        List<Setmeal> list = setmealMapper.list(setmeal);
        return list;
    }

    /**
     * 根据id查询菜品选项
     * @param id
     * @return
     */
    public List<DishItemVO> getDishItemById(Long id) {
        return setmealMapper.getDishItemBySetmealId(id);
    }
}
