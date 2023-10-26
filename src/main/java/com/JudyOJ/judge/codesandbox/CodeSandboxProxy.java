package com.JudyOJ.judge.codesandbox;

import cn.hutool.json.JSONUtil;
import com.JudyOJ.judge.codesandbox.CodeSandbox;
import com.JudyOJ.judge.codesandbox.model.ExecuteCodeRequest;
import com.JudyOJ.judge.codesandbox.model.ExecuteCodeResponse;
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
        log.info("执行代码请求："+ JSONUtil.toJsonStr(executeCodeRequest));
        ExecuteCodeResponse executeCodeResponse = codeSandbox.executeCode(executeCodeRequest);
        log.info("执行代码结果："+JSONUtil.toJsonStr(executeCodeResponse));
        return executeCodeResponse;
    }
}
