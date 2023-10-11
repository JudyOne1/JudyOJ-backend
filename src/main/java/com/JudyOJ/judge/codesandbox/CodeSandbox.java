package com.JudyOJ.judge.codesandbox;

import com.JudyOJ.judge.codesandbox.model.ExecuteCodeRequest;
import com.JudyOJ.judge.codesandbox.model.ExecuteCodeResponse;

/**
 * @author Judy
 * @create 2023-10-11-16:29
 */
public interface CodeSandbox {

    ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest);

}
