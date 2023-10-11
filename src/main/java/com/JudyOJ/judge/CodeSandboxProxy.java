package com.JudyOJ.judge;

import com.JudyOJ.judge.codesandbox.CodeSandbox;
import com.JudyOJ.judge.model.ExecuteCodeRequest;
import com.JudyOJ.judge.model.ExecuteCodeResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 代理模式
 * @author Judy
 * @create 2023-10-11-17:28
 */
@Slf4j
@AllArgsConstructor//
public class CodeSandboxProxy implements CodeSandbox {
    private CodeSandbox codeSandbox;

    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        log.info(""+executeCodeRequest);
        ExecuteCodeResponse executeCodeResponse = codeSandbox.executeCode(executeCodeRequest);
        log.info(""+executeCodeResponse);
        return executeCodeResponse;
    }
}
