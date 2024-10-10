package com.cool.pandora.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cool.pandora.common.ErrorCode;
import com.cool.pandora.constant.CommonConstant;
import com.cool.pandora.exception.ThrowUtils;
import com.cool.pandora.mapper.QuestionViewMapper;
import com.cool.pandora.model.dto.questionview.QuestionViewQueryRequest;
import com.cool.pandora.model.entity.QuestionView;
import com.cool.pandora.model.entity.User;
import com.cool.pandora.model.vo.QuestionViewVO;
import com.cool.pandora.model.vo.UserVO;
import com.cool.pandora.service.QuestionViewService;
import com.cool.pandora.service.UserService;
import com.cool.pandora.utils.SqlUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 用户题目浏览记录服务实现
 *
 */
@Service
@Slf4j
public class QuestionViewServiceImpl extends ServiceImpl<QuestionViewMapper, QuestionView> implements QuestionViewService {

    @Resource
    private UserService userService;

    /**
     * 校验数据
     *
     * @param questionView
     * @param add      对创建的数据进行校验
     */
    @Override
    public void validQuestionView(QuestionView questionView, boolean add) {
        ThrowUtils.throwIf(questionView == null, ErrorCode.PARAMS_ERROR);
    }

    /**
     * 获取查询条件
     *
     * @param questionViewQueryRequest
     * @return
     */
    @Override
    public QueryWrapper<QuestionView> getQueryWrapper(QuestionViewQueryRequest questionViewQueryRequest) {
        QueryWrapper<QuestionView> queryWrapper = new QueryWrapper<>();
        if (questionViewQueryRequest == null) {
            return queryWrapper;
        }
        // todo 从对象中取值

        String sortField = questionViewQueryRequest.getSortField();
        String sortOrder = questionViewQueryRequest.getSortOrder();
        Long questionId = questionViewQueryRequest.getQuestionId();
        Integer viewId = questionViewQueryRequest.getViewId();
        Long userId = questionViewQueryRequest.getUserId();
        // todo 补充需要的查询条件
        // 精确查询
        queryWrapper.ne(ObjectUtils.isNotEmpty(viewId), "viewId", viewId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(questionId), "questionId", questionId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);
        // 排序规则
        queryWrapper.orderBy(SqlUtils.validSortField(sortField),
                sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }

    /**
     * 获取用户题目浏览记录封装
     *
     * @param questionView
     * @param request
     * @return
     */
    @Override
    public QuestionViewVO getQuestionViewVO(QuestionView questionView, HttpServletRequest request) {
        // 对象转封装类
        QuestionViewVO questionViewVO = QuestionViewVO.objToVo(questionView);

        // todo 可以根据需要为封装对象补充值，不需要的内容可以删除
        // region 可选
        // 1. 关联查询用户信息
        Long userId = questionView.getUserId();
        User user = null;
        if (userId != null && userId > 0) {
            user = userService.getById(userId);
        }
        UserVO userVO = userService.getUserVO(user);
        questionViewVO.setUser(userVO);
        // endregion

        return questionViewVO;
    }

    /**
     * 分页获取用户题目浏览记录封装
     *
     * @param questionViewPage
     * @param request
     * @return
     */
    @Override
    public Page<QuestionViewVO> getQuestionViewVOPage(Page<QuestionView> questionViewPage, HttpServletRequest request) {
        List<QuestionView> questionViewList = questionViewPage.getRecords();
        Page<QuestionViewVO> questionViewVOPage = new Page<>(questionViewPage.getCurrent(), questionViewPage.getSize(), questionViewPage.getTotal());
        if (CollUtil.isEmpty(questionViewList)) {
            return questionViewVOPage;
        }
        // 对象列表 => 封装对象列表
        List<QuestionViewVO> questionViewVOList = questionViewList.stream().map(questionView -> {
            return QuestionViewVO.objToVo(questionView);
        }).collect(Collectors.toList());

        // todo 可以根据需要为封装对象补充值，不需要的内容可以删除
        // region 可选
        // 1. 关联查询用户信息
        Set<Long> userIdSet = questionViewList.stream().map(QuestionView::getUserId).collect(Collectors.toSet());
        Map<Long, List<User>> userIdUserListMap = userService.listByIds(userIdSet).stream()
                .collect(Collectors.groupingBy(User::getId));
        // 填充信息
        questionViewVOList.forEach(questionViewVO -> {
            Long userId = Long.valueOf(questionViewVO.getUserId());
            User user = null;
            if (userIdUserListMap.containsKey(userId)) {
                user = userIdUserListMap.get(userId).get(0);
            }
            questionViewVO.setUser(userService.getUserVO(user));
        });
        // endregion

        questionViewVOPage.setRecords(questionViewVOList);
        return questionViewVOPage;
    }

}
