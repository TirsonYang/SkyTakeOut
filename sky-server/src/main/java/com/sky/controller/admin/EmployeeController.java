package com.sky.controller.admin;

import com.sky.constant.JwtClaimsConstant;
import com.sky.constant.MessageConstant;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.dto.PasswordEditDTO;
import com.sky.entity.Employee;
import com.sky.exception.PasswordErrorException;
import com.sky.properties.JwtProperties;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.EmployeeService;
import com.sky.utils.JwtUtil;
import com.sky.vo.EmployeeLoginVO;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 员工管理
 */
@RestController
@RequestMapping("/admin/employee")
@Slf4j
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 登录
     *
     * @param employeeLoginDTO
     * @return
     */
    @PostMapping("/login")
    @ApiOperation("员工登录")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {
        log.info("员工登录：{}", employeeLoginDTO);

        Employee employee = employeeService.login(employeeLoginDTO);

        //登录成功后，生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID, employee.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims);

        EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.builder()
                .id(employee.getId())
                .userName(employee.getUsername())
                .name(employee.getName())
                .token(token)
                .build();

        return Result.success(employeeLoginVO);
    }

    /**
     * 退出
     *
     * @return
     */
    @PostMapping("/logout")
    @ApiOperation("退出登录")
    public Result<String> logout() {
        return Result.success();
    }

    @RequestMapping(method = RequestMethod.POST)
    @ApiOperation("新增员工")
    public Result insert(@RequestBody EmployeeDTO employeeDTO){
        employeeService.insert(employeeDTO);
        return Result.success();
    }

    @RequestMapping(value = "/page",method = RequestMethod.GET)
    @ApiOperation("员工分页查询")
    public Result<PageResult> pageQuery(EmployeePageQueryDTO employeePageQueryDTO){
        log.info("员工分页查询，参数为：{}",employeePageQueryDTO);
        PageResult pageResult=employeeService.pageQuery(employeePageQueryDTO);
        return Result.success(pageResult);
    }

    @RequestMapping(value = "/status/{status}",method = RequestMethod.POST)
    @ApiOperation("启用、禁用员工")
    public Result setStatus(@PathVariable Integer status,Long id){
        if (status.equals(1)) {
            log.info("启用");
        } else {
            log.info("禁用");
        }
        employeeService.setStatus(status,id);
        return Result.success();
    }

    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
    @ApiOperation("根据id查找员工")
    public Result<Employee> getById(@PathVariable Long id){
        log.info("查询id为{}的员工",id);
        Employee employee=employeeService.getById(id);
        return Result.success(employee);
    }

    @RequestMapping(method = RequestMethod.PUT)
    @ApiOperation("编辑员工信息")
    public Result updateEmployee(@RequestBody EmployeeDTO employeeDTO){
        log.info("修改员工信息");
        employeeService.updateEmployee(employeeDTO);
        return Result.success();
    }
    @RequestMapping(value = "/editPassword",method = RequestMethod.PUT)
    public Result updatePassword(@RequestBody PasswordEditDTO passwordEditDTO){
        log.info("修改id为{}的员工密码",passwordEditDTO.getEmpId());
        boolean flag=employeeService.updatePassword(passwordEditDTO);
        if (flag){
            return Result.success();
        }else {
            log.info("旧密码错误，"+MessageConstant.PASSWORD_EDIT_FAILED);
            return Result.error("旧密码错误，"+MessageConstant.PASSWORD_EDIT_FAILED);
        }
    }

}
