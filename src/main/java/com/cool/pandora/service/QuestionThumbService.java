package com.cool.pandora.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cool.pandora.model.entity.QuestionThumb;
import com.cool.pandora.model.vo.QuestionThumbVO;

import javax.servlet.http.HttpServletRequest;

/**
 * 题目点赞服务
 *
 */
public interface QuestionThumbService extends IService<QuestionThumb> {

    /**
     * 校验数据
     *
     * @param questionThumb
     * @param add 对创建的数据进行校验
     */
    void validQuestionThumb(QuestionThumb questionThumb, boolean add);

    /**
     * 获取查询条件
     *
     * @param questionThumbQueryRequest
     * @return
     */
//    QueryWrapper<QuestionThumb> getQueryWrapper(QuestionThumbQueryRequest questionThumbQueryRequest);
    
    /**
     * 获取题目点赞封装
     *
     * @param questionThumb
     * @param request
     * @return
     */
    QuestionThumbVO getQuestionThumbVO(QuestionThumb questionThumb, HttpServletRequest request);

    /**
     * 分页获取题目点赞封装
     *
     * @param questionThumbPage
     * @param request
     * @return
     */
    Page<QuestionThumbVO> getQuestionThumbVOPage(Page<QuestionThumb> questionThumbPage, HttpServletRequest request);
}
