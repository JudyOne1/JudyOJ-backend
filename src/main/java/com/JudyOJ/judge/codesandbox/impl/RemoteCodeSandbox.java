package com.JudyOJ.judge.codesandbox.impl;

import com.JudyOJ.judge.codesandbox.CodeSandbox;
import com.JudyOJ.judge.codesandbox.model.ExecuteCodeRequest;
import com.JudyOJ.judge.codesandbox.model.ExecuteCodeResponse;

/**
 * 远程代码沙箱（实际使用的代码沙箱）
 * @author Judy
 * @create 2023-10-11-16:38
 */
public class RemoteCodeSandbox implements CodeSandbox {
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        System.out.println("远程代码沙箱...");
        return null;
    }
}
