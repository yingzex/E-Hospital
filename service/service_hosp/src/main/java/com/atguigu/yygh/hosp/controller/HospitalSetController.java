package com.atguigu.yygh.hosp.controller;

import com.atguigu.yygh.common.result.Result;
import com.atguigu.yygh.hosp.service.HospitalSetService;
import com.atguigu.yygh.model.hosp.HospitalSet;
import com.atguigu.yygh.vo.hosp.HospitalSetQueryVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "医院设置管理")
@RestController
@RequestMapping("/admin/hosp/hospitalSet")
public class HospitalSetController {
    @Autowired
    private HospitalSetService hospitalSetService;

//    1.查询医院设置表的所有信息
    @ApiOperation(value = "获取所有医院设置")
    @GetMapping("findAll")
//    http://localhost:8201/admin/hosp/hospitalSet/findAll
//    http://localhost:8201/swagger-ui.html
    public Result findAllHospitalSet() {
//        controller调用service中的方法
        List<HospitalSet> list = hospitalSetService.list();
        return Result.ok(list); // RestController返回的是json，自动将list转换成json数据返回
    }

//    2.逻辑删除医院设置表的所有信息（表中加上删除标志代表是否删除）
    @DeleteMapping("{id}")
    @ApiOperation(value = "逻辑删除所有医院设置")
//    http://localhost:8201/swagger-ui.html
    public Result removeHospSet(@PathVariable Long id) {
        boolean flag = hospitalSetService.removeById(id);
        if(flag) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

//    3.条件查询（带分页接口）
//        创建vo类，封装条件值，model中有vo.HospitalSetQueryVo
//        编写controller，获取条件对象，包含当前页、每页记录数
    @PostMapping("findPage/{current}/{limit}")
    public Result findPageHospSet(@PathVariable long current,
                                  @PathVariable long limit,
//                                  @RequestBody：通过json数据传递参数，把json数据放入对象中，false可以为空
//                                  此时提交方式不能为get，get提交方式得不到值，应该使用post提交得到RequestBody得到的值（PostMapping）
                                  @RequestBody(required = false) HospitalSetQueryVo hospitalSetQueryVo) {
//        先创建一个page对象，传入当前页和每页记录数
        Page<HospitalSet> page = new Page<>(current,limit);
//        构建条件，用条件构造器QueryWrapper
        QueryWrapper<HospitalSet> wrapper = new QueryWrapper<>();
        String hosname = hospitalSetQueryVo.getHosname();
        String hoscode = hospitalSetQueryVo.getHoscode();
//        条件值可能两个都有、都没有、有其中一个，如果没有则不需要设置
        if(!StringUtils.isEmpty(hosname)) {
            wrapper.like("hosname",hosname); // 医院名称做模糊查询
        }
        if(!StringUtils.isEmpty(hoscode)) {
            wrapper.eq("hoscode", hoscode); // 医院编号做实际的等于查询
        }
//        调用方法实现分页查询
        Page<HospitalSet> pageHospitalSet = hospitalSetService.page(page, wrapper);
//        返回结果
        return Result.ok(pageHospitalSet);
    }

//    4.添加医院设置接口

//    5.根据id获取医院设置

//    6.修改医院设置

//    7.批量删除医院设置
}
