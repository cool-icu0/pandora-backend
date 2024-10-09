package com.cool.pandora.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户对题目浏览记录
 * @TableName question_view
 */
@TableName(value ="question_view")
@Data
public class QuestionView implements Serializable {
    /**
     * 浏览记录Id
     */
    @TableId(type = IdType.AUTO)
    private Integer viewId;

    /**
     * 访问用户Id
     */
    private Long userId;

    /**
     * 被浏览的面试题的唯一标识
     */
    private Long questionId;

    /**
     * 用户访问该题目的时间戳
     */
    private Date viewTimestamp;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}