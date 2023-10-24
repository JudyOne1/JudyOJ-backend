package com.JudyOJ.judge.codesandbox.strategy;

import com.JudyOJ.model.dto.question.JudgeCase;
import com.JudyOJ.judge.codesandbox.model.JudgeInfo;
import com.JudyOJ.model.entity.Question;
import com.JudyOJ.model.entity.QuestionSubmit;
import lombok.Data;

import java.util.List;

/**
 * 一开始不知道需要什么参数，可以先定义这个类
 * 上下文（用于定义在策略中传递的参数）
 */
@Data
public class JudgeContext {

    private JudgeInfo judgeInfo;

    private List<String> inputList;

    private List<String> outputList;

    private List<JudgeCase> judgeCaseList;

    private Question question;

    private QuestionSubmit questionSubmit;

}
