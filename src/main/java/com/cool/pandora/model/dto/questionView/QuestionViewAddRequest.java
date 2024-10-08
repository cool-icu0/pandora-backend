package com.cool.pandora.model.dto.questionView;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 创建用户题目浏览记录请求
 *
 */
@Data
public class QuestionViewAddRequest implements Serializable {

    /**
     * 访问用户Id
     */
    private Integer userId;

    /**
     * 被浏览的面试题的唯一标识
     */
    private Integer questionId;

    private static final long serialVersionUID = 1L;
}