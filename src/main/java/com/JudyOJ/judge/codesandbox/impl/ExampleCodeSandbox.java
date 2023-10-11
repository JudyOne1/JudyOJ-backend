package com.JudyOJ.judge.codesandbox.impl;

import com.JudyOJ.judge.codesandbox.CodeSandbox;
import com.JudyOJ.judge.model.ExecuteCodeRequest;
import com.JudyOJ.judge.model.ExecuteCodeResponse;

/**
 * 示例代码沙箱
 * @author Judy
 * @create 2023-10-11-16:38
 */
public class ExampleCodeSandbox implements CodeSandbox {

    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        System.out.println("示例代码沙箱...");
        return null;
    }
}
