package com.cool.pandora.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cool.pandora.model.dto.questionview.QuestionViewQueryRequest;
import com.cool.pandora.model.entity.QuestionView;
import com.cool.pandora.model.vo.QuestionViewVO;

import javax.servlet.http.HttpServletRequest;

/**
 * 用户题目浏览记录服务
 *
 */
public interface QuestionViewService extends IService<QuestionView> {

    /**
     * 校验数据
     *
     * @param questionView
     * @param add 对创建的数据进行校验
     */
    void validQuestionView(QuestionView questionView, boolean add);

    /**
     * 获取查询条件
     *
     * @param questionViewQueryRequest
     * @return
     */
    QueryWrapper<QuestionView> getQueryWrapper(QuestionViewQueryRequest questionViewQueryRequest);
    
    /**
     * 获取用户题目浏览记录封装
     *
     * @param questionView
     * @param request
     * @return
     */
    QuestionViewVO getQuestionViewVO(QuestionView questionView, HttpServletRequest request);

    /**
     * 分页获取用户题目浏览记录封装
     *
     * @param questionViewPage
     * @param request
     * @return
     */
    Page<QuestionViewVO> getQuestionViewVOPage(Page<QuestionView> questionViewPage, HttpServletRequest request);
}
