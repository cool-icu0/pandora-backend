package com.cool.pandora.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cool.pandora.model.dto.questionfavour.QuestionFavourQueryRequest;
import com.cool.pandora.model.entity.QuestionFavour;
import com.cool.pandora.model.vo.QuestionFavourVO;

import javax.servlet.http.HttpServletRequest;

/**
 * 题目收藏服务
 *
 */
public interface QuestionFavourService extends IService<QuestionFavour> {

    /**
     * 校验数据
     *
     * @param questionFavour
     * @param add 对创建的数据进行校验
     */
    void validQuestionFavour(QuestionFavour questionFavour, boolean add);

    /**
     * 获取查询条件
     *
     * @param questionFavourQueryRequest
     * @return
     */
    QueryWrapper<QuestionFavour> getQueryWrapper(QuestionFavourQueryRequest questionFavourQueryRequest);
    
    /**
     * 获取题目收藏封装
     *
     * @param questionFavour
     * @param request
     * @return
     */
    QuestionFavourVO getQuestionFavourVO(QuestionFavour questionFavour, HttpServletRequest request);

    /**
     * 分页获取题目收藏封装
     *
     * @param questionFavourPage
     * @param request
     * @return
     */
    Page<QuestionFavourVO> getQuestionFavourVOPage(Page<QuestionFavour> questionFavourPage, HttpServletRequest request);
}
