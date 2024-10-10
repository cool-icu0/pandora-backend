package com.cool.pandora.model.dto.questionview;

import lombok.Data;

import java.io.Serializable;

/**
 * 编辑用户题目浏览记录请求
 *
 */
@Data
public class QuestionViewEditRequest implements Serializable {

    /**
     * 浏览记录Id
     */
    private Integer viewId;

    /**
     * 访问用户Id
     */
    private Long userId;

    /**
     * 被浏览的面试题的唯一标识
     */
    private Long questionId;

    private static final long serialVersionUID = 1L;
}