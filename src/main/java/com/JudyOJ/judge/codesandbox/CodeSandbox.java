package com.JudyOJ.judge.codesandbox;

import com.JudyOJ.judge.model.ExecuteCodeRequest;
import com.JudyOJ.judge.model.ExecuteCodeResponse;

/**
 * @author Judy
 * @create 2023-10-11-16:29
 */
public interface CodeSandbox {

    ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest);

}
