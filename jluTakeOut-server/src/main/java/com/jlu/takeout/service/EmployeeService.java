package com.jlu.takeout.service;

import com.jlu.takeout.dto.EmployeeDTO;
import com.jlu.takeout.dto.EmployeeLoginDTO;
import com.jlu.takeout.dto.EmployeePageQueryDTO;
import com.jlu.takeout.entity.Employee;
import com.jlu.takeout.result.PageResult;

public interface EmployeeService {

    /**
     * 员工登录
     * @param employeeLoginDTO
     * @return
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO);

    /**
     * 新增员工
     * @param employeeDTO
     */
    void save(EmployeeDTO employeeDTO);

    /**
     * 分页查询
     * @param employeePageQueryDTO
     * @return
     */
    PageResult pageResult(EmployeePageQueryDTO employeePageQueryDTO);

    /**
     * 设置状态
     * @param status
     * @param id
     */
    void startOrStop(Integer status, Long id);

    /**
     * 根据id查员工信息
     * @return
     */
    Employee getById(Long id);

    /**
     * 更新员工信息
     * @param employeeDTO
     */
    void update(EmployeeDTO employeeDTO);

}
