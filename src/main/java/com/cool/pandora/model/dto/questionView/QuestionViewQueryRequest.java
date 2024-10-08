package com.cool.pandora.model.dto.questionView;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.cool.pandora.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

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
    private Integer userId;

    /**
     * 被浏览的面试题的唯一标识
     */
    private Integer questionId;

    private static final long serialVersionUID = 1L;
}