package com.JudyOJ.judge.codesandbox;

import com.JudyOJ.judge.codesandbox.model.ExecuteCodeRequest;
import com.JudyOJ.judge.codesandbox.model.ExecuteCodeResponse;
import com.JudyOJ.model.enums.QuestionSubmitLanguageEnum;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * @author Judy
 * @create 2023-10-11-16:43
 */
@SpringBootTest
class CodeSandboxTest {

    @Value("${codesandbox.type:example}")
    String type;

    @Test
    void executeCode() {

        CodeSandbox codeSandbox = CodeSandboxFactory.newInstance(type);
//            CodeSandbox codeSandbox = new ExampleCodeSandbox();
        String code = "";
        String language = QuestionSubmitLanguageEnum.JAVA.getValue();
        List<String> list = Arrays.asList("1 2", "3 4");
        ExecuteCodeRequest executeCodeRequest = ExecuteCodeRequest.builder()
                .code(code)
                .language(language)
                .inputList(list)
                .build();
        ExecuteCodeResponse executeCodeResponse = codeSandbox.executeCode(executeCodeRequest);
        Assertions.assertNotNull(executeCodeResponse);


    }

    @Test
    void executeCodeProxy() {

        CodeSandbox codeSandbox = CodeSandboxFactory.newInstance(type);
        CodeSandboxProxy codeSandboxProxy = new CodeSandboxProxy(codeSandbox);
//            CodeSandbox codeSandbox = new ExampleCodeSandbox();
        String code = "";
        String language = QuestionSubmitLanguageEnum.JAVA.getValue();
        List<String> list = Arrays.asList("1 2", "3 4");
        ExecuteCodeRequest executeCodeRequest = ExecuteCodeRequest.builder()
                .code(code)
                .language(language)
                .inputList(list)
                .build();
        ExecuteCodeResponse executeCodeResponse = codeSandboxProxy.executeCode(executeCodeRequest);
        Assertions.assertNotNull(executeCodeResponse);


    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String next = scanner.next();
            CodeSandbox codeSandbox = CodeSandboxFactory.newInstance(next);
//            CodeSandbox codeSandbox = new ExampleCodeSandbox();
            String code = "";
            String language = QuestionSubmitLanguageEnum.JAVA.getValue();
            List<String> list = Arrays.asList("1 2", "3 4");
            ExecuteCodeRequest executeCodeRequest = ExecuteCodeRequest.builder()
                    .code(code)
                    .language(language)
                    .inputList(list)
                    .build();
            ExecuteCodeResponse executeCodeResponse = codeSandbox.executeCode(executeCodeRequest);
            Assertions.assertNotNull(executeCodeResponse);
        }
    }
}