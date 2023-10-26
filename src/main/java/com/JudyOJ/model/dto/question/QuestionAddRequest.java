package com.JudyOJ.model.dto.question;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 创建请求
 *
 *
 */
@Data
public class QuestionAddRequest implements Serializable {

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 标签列表
     */
    private List<String> tags;

    /**
     * 题目答案
     */
    private String answer;

    /**
     * 判题用例
     */
    private List<JudgeCase> judgeCase;

    /**
     * 判题配置
     */
    private JudgeConfig judgeConfig;

    /**
     * 1-ACM | 2-CCM | 3-counter
     */
    private Integer mode;

    /**
     * CCM核心代码模式的模板代码
     */
    private String defaultCode;

    /**
     * 对数器的代码
     */
    private String countCode;



    private static final long serialVersionUID = 1L;
}