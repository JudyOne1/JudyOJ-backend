package com.JudyOJ.model.dto.questionsubmit;

import com.JudyOJ.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 查询请求
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class QuestionSubmitQueryByIdRequest extends PageRequest implements Serializable {


    /**
     * 题目 id
     */
    private Long questionId;

    /**
     * 用户 id
     */
    private Long userId;

    /**
     * 提交id
     */
    private Long submitId;


    private static final long serialVersionUID = 1L;
}