package com.cool.pandora.model.dto.questionview;

import com.cool.pandora.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 查询用户题目浏览记录请求
 *
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class QuestionViewQueryRequest extends PageRequest implements Serializable {

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