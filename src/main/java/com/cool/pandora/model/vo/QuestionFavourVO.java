package com.cool.pandora.model.vo;

import cn.hutool.json.JSONUtil;
import com.cool.pandora.model.entity.QuestionFavour;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 题目收藏视图
 *
 */
@Data
public class QuestionFavourVO implements Serializable {

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
     * @param questionFavourVO
     * @return
     */
    public static QuestionFavour voToObj(QuestionFavourVO questionFavourVO) {
        if (questionFavourVO == null) {
            return null;
        }
        QuestionFavour questionFavour = new QuestionFavour();
        BeanUtils.copyProperties(questionFavourVO, questionFavour);
        List<String> tagList = questionFavourVO.getTagList();
        return questionFavour;
    }

    /**
     * 对象转封装类
     *
     * @param questionFavour
     * @return
     */
    public static QuestionFavourVO objToVo(QuestionFavour questionFavour) {
        if (questionFavour == null) {
            return null;
        }
        QuestionFavourVO questionFavourVO = new QuestionFavourVO();
        BeanUtils.copyProperties(questionFavour, questionFavourVO);
        return questionFavourVO;
    }
}
