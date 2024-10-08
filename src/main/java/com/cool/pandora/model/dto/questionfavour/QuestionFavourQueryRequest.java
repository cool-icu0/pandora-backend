package com.cool.pandora.model.dto.questionfavour;

import com.cool.pandora.common.PageRequest;

import com.cool.pandora.model.dto.question.QuestionQueryRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 题目收藏查询请求
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class QuestionFavourQueryRequest extends PageRequest implements Serializable {

    /**
     * 题目查询请求
     */
    private QuestionQueryRequest questionQueryRequest;

    /**
     * 用户 id
     */
    private Long userId;

    private static final long serialVersionUID = 1L;
}