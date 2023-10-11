package com.JudyOJ.judge.codesandbox;

import com.JudyOJ.judge.codesandbox.CodeSandbox;
import com.JudyOJ.judge.codesandbox.impl.ExampleCodeSandbox;
import com.JudyOJ.judge.codesandbox.impl.RemoteCodeSandbox;
import com.JudyOJ.judge.codesandbox.impl.ThirdPartyCodeSandbox;
import org.checkerframework.common.reflection.qual.NewInstance;

/**
 * 代码沙箱创建工厂（静态工厂模式）
 * 根据字符串参数
 *
 * @author Judy
 * @create 2023-10-11-16:51
 */
public class CodeSandboxFactory {
    /**
     * 创建代码沙箱示例
     * @param type 沙箱字符串类别
     * @return
     */
    public static CodeSandbox newInstance(String type) {
        switch (type) {
            case "example":
                return new ExampleCodeSandbox();
            case "remote":
                return new RemoteCodeSandbox();
            case "thirdParty":
                return new ThirdPartyCodeSandbox();
            default:
                return new ExampleCodeSandbox();
        }
    }
}
