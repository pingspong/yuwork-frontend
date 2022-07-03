package com.yupi.project.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yupi.project.common.BaseResponse;
import com.yupi.project.common.ErrorCode;
import com.yupi.project.common.ResultUtils;
import com.yupi.project.exception.BusinessException;
import com.yupi.project.model.entity.User;
import com.yupi.project.model.entity.Work;
import com.yupi.project.model.request.UserLoginRequest;
import com.yupi.project.model.request.UserRegisterRequest;
import com.yupi.project.service.UserService;
import com.yupi.project.service.WorkService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 任务接口
 *
 * @author yupi
 */
@RestController
@RequestMapping("/work")
@CrossOrigin
public class WorkController {

    @Resource
    private WorkService workService;

    @PostMapping("/create")
    public BaseResponse<Long> createWork(@RequestBody Work work) {
        if (work == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        if (StringUtils.isAnyBlank(work.getName(), work.getDescription())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = workService.save(work);
        if (!result) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }
        return ResultUtils.success(work.getId());
    }


    @GetMapping("/list")
    public BaseResponse<List<Work>> listWork(Work work) {
        QueryWrapper<Work> queryWrapper = new QueryWrapper<>(work);
        List<Work> workList = workService.list(queryWrapper);
        return ResultUtils.success(workList);
    }
}
