package com.cool.pandora.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cool.pandora.annotation.AuthCheck;
import com.cool.pandora.common.BaseResponse;
import com.cool.pandora.common.DeleteRequest;
import com.cool.pandora.common.ErrorCode;
import com.cool.pandora.common.ResultUtils;
import com.cool.pandora.constant.UserConstant;
import com.cool.pandora.exception.BusinessException;
import com.cool.pandora.exception.ThrowUtils;
import com.cool.pandora.model.dto.questionview.QuestionViewAddRequest;
import com.cool.pandora.model.dto.questionview.QuestionViewQueryRequest;
import com.cool.pandora.model.dto.questionview.QuestionViewUpdateRequest;
import com.cool.pandora.model.entity.QuestionView;
import com.cool.pandora.model.entity.User;
import com.cool.pandora.model.vo.QuestionViewVO;
import com.cool.pandora.service.QuestionViewService;
import com.cool.pandora.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 用户题目浏览记录接口
 *
 */
@RestController
@RequestMapping("/questionView")
@Slf4j
public class QuestionViewController {

    @Resource
    private QuestionViewService questionViewService;

    @Resource
    private UserService userService;

    // region 增删改查

    /**
     * 创建用户题目浏览记录
     *
     * @param questionViewAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Long> addQuestionView(@RequestBody QuestionViewAddRequest questionViewAddRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(questionViewAddRequest == null, ErrorCode.PARAMS_ERROR);
        // todo 在此处将实体类和 DTO 进行转换
        QuestionView questionView = new QuestionView();
        BeanUtils.copyProperties(questionViewAddRequest, questionView);
        // 数据校验
        questionViewService.validQuestionView(questionView, true);
        // todo 填充默认值
        User loginUser = userService.getLoginUser(request);
        questionView.setUserId(loginUser.getId());
        // 写入数据库
        boolean result = questionViewService.save(questionView);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        // 返回新写入的数据 id
        long newQuestionViewId = questionView.getViewId();
        return ResultUtils.success(newQuestionViewId);
    }

    /**
     * 删除用户题目浏览记录
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteQuestionView(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        QuestionView oldQuestionView = questionViewService.getById(id);
        ThrowUtils.throwIf(oldQuestionView == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可删除
        if (!oldQuestionView.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        // 操作数据库
        boolean result = questionViewService.removeById(id);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 更新用户题目浏览记录（仅管理员可用）
     *
     * @param questionViewUpdateRequest
     * @return
     */
    @PostMapping("/update")
    @SaCheckRole(UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateQuestionView(@RequestBody QuestionViewUpdateRequest questionViewUpdateRequest) {
        if (questionViewUpdateRequest == null || questionViewUpdateRequest.getViewId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // todo 在此处将实体类和 DTO 进行转换
        QuestionView questionView = new QuestionView();
        BeanUtils.copyProperties(questionViewUpdateRequest, questionView);
        // 数据校验
        questionViewService.validQuestionView(questionView, false);
        // 判断是否存在
        long id = questionViewUpdateRequest.getViewId();
        QuestionView oldQuestionView = questionViewService.getById(id);
        ThrowUtils.throwIf(oldQuestionView == null, ErrorCode.NOT_FOUND_ERROR);
        // 操作数据库
        boolean result = questionViewService.updateById(questionView);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 根据 id 获取用户题目浏览记录（封装类）
     *
     * @param id
     * @return
     */
    @GetMapping("/get/vo")
    public BaseResponse<QuestionViewVO> getQuestionViewVOById(long id, HttpServletRequest request) {
        ThrowUtils.throwIf(id <= 0, ErrorCode.PARAMS_ERROR);
        // 查询数据库
        QuestionView questionView = questionViewService.getById(id);
        ThrowUtils.throwIf(questionView == null, ErrorCode.NOT_FOUND_ERROR);
        // 获取封装类
        return ResultUtils.success(questionViewService.getQuestionViewVO(questionView, request));
    }

    /**
     * 分页获取用户题目浏览记录列表（仅管理员可用）
     *
     * @param questionViewQueryRequest
     * @return
     */
    @PostMapping("/list/page")
    @SaCheckRole(UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<QuestionView>> listQuestionViewByPage(@RequestBody QuestionViewQueryRequest questionViewQueryRequest) {
        long current = questionViewQueryRequest.getCurrent();
        long size = questionViewQueryRequest.getPageSize();
        // 查询数据库
        Page<QuestionView> questionViewPage = questionViewService.page(new Page<>(current, size),
                questionViewService.getQueryWrapper(questionViewQueryRequest));
        return ResultUtils.success(questionViewPage);
    }

    /**
     * 分页获取用户题目浏览记录列表（封装类）
     *
     * @param questionViewQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page/vo")
    public BaseResponse<Page<QuestionViewVO>> listQuestionViewVOByPage(@RequestBody QuestionViewQueryRequest questionViewQueryRequest,
                                                               HttpServletRequest request) {
        long current = questionViewQueryRequest.getCurrent();
        long size = questionViewQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        // 查询数据库
        Page<QuestionView> questionViewPage = questionViewService.page(new Page<>(current, size),
                questionViewService.getQueryWrapper(questionViewQueryRequest));
        // 获取封装类
        return ResultUtils.success(questionViewService.getQuestionViewVOPage(questionViewPage, request));
    }

    /**
     * 分页获取当前登录用户创建的用户题目浏览记录列表
     *
     * @param questionViewQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/my/list/page/vo")
    public BaseResponse<Page<QuestionViewVO>> listMyQuestionViewVOByPage(@RequestBody QuestionViewQueryRequest questionViewQueryRequest,
                                                                 HttpServletRequest request) {
        ThrowUtils.throwIf(questionViewQueryRequest == null, ErrorCode.PARAMS_ERROR);
        // 补充查询条件，只查询当前登录用户的数据
        User loginUser = userService.getLoginUser(request);
        questionViewQueryRequest.setUserId(loginUser.getId());
        long current = questionViewQueryRequest.getCurrent();
        long size = questionViewQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        // 查询数据库
        Page<QuestionView> questionViewPage = questionViewService.page(new Page<>(current, size),
                questionViewService.getQueryWrapper(questionViewQueryRequest));
        // 获取封装类
        return ResultUtils.success(questionViewService.getQuestionViewVOPage(questionViewPage, request));
    }

    // endregion
}
