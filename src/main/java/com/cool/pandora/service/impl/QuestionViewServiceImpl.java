package com.cool.pandora.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cool.pandora.common.ErrorCode;
import com.cool.pandora.constant.CommonConstant;
import com.cool.pandora.exception.ThrowUtils;
import com.cool.pandora.mapper.QuestionViewMapper;
import com.cool.pandora.model.dto.questionView.QuestionViewQueryRequest;
import com.cool.pandora.model.entity.QuestionView;
import com.cool.pandora.model.entity.QuestionViewFavour;
import com.cool.pandora.model.entity.QuestionViewThumb;
import com.cool.pandora.model.entity.User;
import com.cool.pandora.model.vo.QuestionViewVO;
import com.cool.pandora.model.vo.UserVO;
import com.cool.pandora.service.QuestionViewService;
import com.cool.pandora.service.UserService;
import com.cool.pandora.utils.SqlUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
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
        // todo 从对象中取值
        String title = questionView.getTitle();
        // 创建数据时，参数不能为空
        if (add) {
            // todo 补充校验规则
            ThrowUtils.throwIf(StringUtils.isBlank(title), ErrorCode.PARAMS_ERROR);
        }
        // 修改数据时，有参数则校验
        // todo 补充校验规则
        if (StringUtils.isNotBlank(title)) {
            ThrowUtils.throwIf(title.length() > 80, ErrorCode.PARAMS_ERROR, "标题过长");
        }
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
        Long id = questionViewQueryRequest.getId();
        Long notId = questionViewQueryRequest.getNotId();
        String title = questionViewQueryRequest.getTitle();
        String content = questionViewQueryRequest.getContent();
        String searchText = questionViewQueryRequest.getSearchText();
        String sortField = questionViewQueryRequest.getSortField();
        String sortOrder = questionViewQueryRequest.getSortOrder();
        List<String> tagList = questionViewQueryRequest.getTags();
        Long userId = questionViewQueryRequest.getUserId();
        // todo 补充需要的查询条件
        // 从多字段中搜索
        if (StringUtils.isNotBlank(searchText)) {
            // 需要拼接查询条件
            queryWrapper.and(qw -> qw.like("title", searchText).or().like("content", searchText));
        }
        // 模糊查询
        queryWrapper.like(StringUtils.isNotBlank(title), "title", title);
        queryWrapper.like(StringUtils.isNotBlank(content), "content", content);
        // JSON 数组查询
        if (CollUtil.isNotEmpty(tagList)) {
            for (String tag : tagList) {
                queryWrapper.like("tags", "\"" + tag + "\"");
            }
        }
        // 精确查询
        queryWrapper.ne(ObjectUtils.isNotEmpty(notId), "id", notId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(id), "id", id);
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
        // 2. 已登录，获取用户点赞、收藏状态
        long questionViewId = questionView.getId();
        User loginUser = userService.getLoginUserPermitNull(request);
        if (loginUser != null) {
            // 获取点赞
            QueryWrapper<QuestionViewThumb> questionViewThumbQueryWrapper = new QueryWrapper<>();
            questionViewThumbQueryWrapper.in("questionViewId", questionViewId);
            questionViewThumbQueryWrapper.eq("userId", loginUser.getId());
            QuestionViewThumb questionViewThumb = questionViewThumbMapper.selectOne(questionViewThumbQueryWrapper);
            questionViewVO.setHasThumb(questionViewThumb != null);
            // 获取收藏
            QueryWrapper<QuestionViewFavour> questionViewFavourQueryWrapper = new QueryWrapper<>();
            questionViewFavourQueryWrapper.in("questionViewId", questionViewId);
            questionViewFavourQueryWrapper.eq("userId", loginUser.getId());
            QuestionViewFavour questionViewFavour = questionViewFavourMapper.selectOne(questionViewFavourQueryWrapper);
            questionViewVO.setHasFavour(questionViewFavour != null);
        }
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
        // 2. 已登录，获取用户点赞、收藏状态
        Map<Long, Boolean> questionViewIdHasThumbMap = new HashMap<>();
        Map<Long, Boolean> questionViewIdHasFavourMap = new HashMap<>();
        User loginUser = userService.getLoginUserPermitNull(request);
        if (loginUser != null) {
            Set<Long> questionViewIdSet = questionViewList.stream().map(QuestionView::getId).collect(Collectors.toSet());
            loginUser = userService.getLoginUser(request);
            // 获取点赞
            QueryWrapper<QuestionViewThumb> questionViewThumbQueryWrapper = new QueryWrapper<>();
            questionViewThumbQueryWrapper.in("questionViewId", questionViewIdSet);
            questionViewThumbQueryWrapper.eq("userId", loginUser.getId());
            List<QuestionViewThumb> questionViewQuestionViewThumbList = questionViewThumbMapper.selectList(questionViewThumbQueryWrapper);
            questionViewQuestionViewThumbList.forEach(questionViewQuestionViewThumb -> questionViewIdHasThumbMap.put(questionViewQuestionViewThumb.getQuestionViewId(), true));
            // 获取收藏
            QueryWrapper<QuestionViewFavour> questionViewFavourQueryWrapper = new QueryWrapper<>();
            questionViewFavourQueryWrapper.in("questionViewId", questionViewIdSet);
            questionViewFavourQueryWrapper.eq("userId", loginUser.getId());
            List<QuestionViewFavour> questionViewFavourList = questionViewFavourMapper.selectList(questionViewFavourQueryWrapper);
            questionViewFavourList.forEach(questionViewFavour -> questionViewIdHasFavourMap.put(questionViewFavour.getQuestionViewId(), true));
        }
        // 填充信息
        questionViewVOList.forEach(questionViewVO -> {
            Long userId = questionViewVO.getUserId();
            User user = null;
            if (userIdUserListMap.containsKey(userId)) {
                user = userIdUserListMap.get(userId).get(0);
            }
            questionViewVO.setUser(userService.getUserVO(user));
            questionViewVO.setHasThumb(questionViewIdHasThumbMap.getOrDefault(questionViewVO.getId(), false));
            questionViewVO.setHasFavour(questionViewIdHasFavourMap.getOrDefault(questionViewVO.getId(), false));
        });
        // endregion

        questionViewVOPage.setRecords(questionViewVOList);
        return questionViewVOPage;
    }

}
