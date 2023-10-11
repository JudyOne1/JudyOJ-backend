package com.JudyOJ.judge.codesandbox.strategy;

import com.JudyOJ.model.dto.questionsubmit.JudgeInfo;

/**
 * 策略模式  ——  判题策略
 * @author Judy
 * @create 2023-10-11-17:56
 */
public interface JudgeStrategy {

    /**
     * 执行判题
     * @param judgeContext
     * @return
     */
    JudgeInfo doJudge(JudgeContext judgeContext);
}
