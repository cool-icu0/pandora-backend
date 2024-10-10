package com.cool.pandora.model.vo;

import cn.hutool.json.JSONUtil;
import com.cool.pandora.model.entity.QuestionThumb;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 题目点赞视图
 *
 */
@Data
public class QuestionThumbVO implements Serializable {

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
     * @param questionThumbVO
     * @return
     */
    public static QuestionThumb voToObj(QuestionThumbVO questionThumbVO) {
        if (questionThumbVO == null) {
            return null;
        }
        QuestionThumb questionThumb = new QuestionThumb();
        BeanUtils.copyProperties(questionThumbVO, questionThumb);
        List<String> tagList = questionThumbVO.getTagList();
        return questionThumb;
    }

    /**
     * 对象转封装类
     *
     * @param questionThumb
     * @return
     */
    public static QuestionThumbVO objToVo(QuestionThumb questionThumb) {
        if (questionThumb == null) {
            return null;
        }
        QuestionThumbVO questionThumbVO = new QuestionThumbVO();
        BeanUtils.copyProperties(questionThumb, questionThumbVO);
        return questionThumbVO;
    }
}
