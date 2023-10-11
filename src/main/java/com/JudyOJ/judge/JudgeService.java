package com.JudyOJ.judge;

import com.JudyOJ.model.entity.QuestionSubmit;

/**
 * @author Judy
 * @create 2023-10-11-17:39
 */
public interface JudgeService {

    QuestionSubmit doJudge(long questionSubmitID);
}
