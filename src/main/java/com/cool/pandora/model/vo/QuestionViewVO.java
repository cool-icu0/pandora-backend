package com.cool.pandora.model.vo;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.cool.pandora.model.entity.QuestionView;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 用户题目浏览记录视图
 *
 */
@Data
public class QuestionViewVO implements Serializable {

    /**
     * 浏览记录Id
     */
    private Long viewId;

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

    /**
     * 创建用户信息
     */
    private UserVO user;

    /**
     * 封装类转对象
     *
     * @param questionViewVO
     * @return
     */
    public static QuestionView voToObj(QuestionViewVO questionViewVO) {
        if (questionViewVO == null) {
            return null;
        }
        QuestionView questionView = new QuestionView();
        BeanUtils.copyProperties(questionViewVO, questionView);
        return questionView;
    }

    /**
     * 对象转封装类
     *
     * @param questionView
     * @return
     */
    public static QuestionViewVO objToVo(QuestionView questionView) {
        if (questionView == null) {
            return null;
        }
        QuestionViewVO questionViewVO = new QuestionViewVO();
        BeanUtils.copyProperties(questionView, questionViewVO);
        return questionViewVO;
    }
}
