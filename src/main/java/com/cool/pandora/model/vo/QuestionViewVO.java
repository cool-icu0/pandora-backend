package com.cool.pandora.model.vo;

import cn.hutool.json.JSONUtil;
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
     * id
     */
    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 创建用户 id
     */
    private Long userId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 标签列表
     */
    private List<String> tagList;

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
        List<String> tagList = questionViewVO.getTagList();
        questionView.setTags(JSONUtil.toJsonStr(tagList));
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
        questionViewVO.setTagList(JSONUtil.toList(questionView.getTags(), String.class));
        return questionViewVO;
    }
}
