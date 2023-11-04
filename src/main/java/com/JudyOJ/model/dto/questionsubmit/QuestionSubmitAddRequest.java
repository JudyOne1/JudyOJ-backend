package com.JudyOJ.model.dto.questionsubmit;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


/**
 * 题目提交请求
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionSubmitAddRequest implements Serializable {

    /**
     * 编程语言
     */
    private String language;

    /**
     * 用户代码
     */
    private String code;

    /**
     * 题目 id
     */
    private Long questionId;

    /**
     * 1-ACM | 2-CCM | 3-counter
     */
    private Integer modeSelect;


    private static final long serialVersionUID = 1L;
}