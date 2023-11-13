package com.JudyOJ.judge;

import cn.hutool.json.JSONUtil;
import com.JudyOJ.common.ErrorCode;
import com.JudyOJ.exception.BusinessException;
import com.JudyOJ.judge.codesandbox.CodeSandbox;
import com.JudyOJ.judge.codesandbox.CodeSandboxFactory;
import com.JudyOJ.judge.codesandbox.CodeSandboxProxy;
import com.JudyOJ.judge.codesandbox.model.ExecuteCodeRequest;
import com.JudyOJ.judge.codesandbox.model.ExecuteCodeResponse;
import com.JudyOJ.judge.codesandbox.strategy.JudgeContext;
import com.JudyOJ.model.dto.question.JudgeCase;
import com.JudyOJ.judge.codesandbox.model.JudgeInfo;
import com.JudyOJ.model.entity.Question;
import com.JudyOJ.model.entity.QuestionSubmit;
import com.JudyOJ.model.enums.QuestionSubmitStatusEnum;
import com.JudyOJ.service.QuestionService;
import com.JudyOJ.service.QuestionSubmitService;
import com.JudyOJ.utils.CodeUtils;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Judy
 * @create 2023-10-11-17:42
 */
@Service
public class JudgeServiceImpl implements JudgeService {
    @Resource
    private QuestionService questionService;

    //@Resource 循环依赖
    private QuestionSubmitService questionSubmitService;

    @Autowired
    @Lazy
    public JudgeServiceImpl(QuestionSubmitService questionSubmitService) {
        this.questionSubmitService = questionSubmitService;
    }

    @Resource
    private JudgeManager judgeManager;

    @Value("${codesandbox.type:example}")
    String type;

    @Override
    public QuestionSubmit doJudge(long questionSubmitID) {
        // 1）传入题目的提交 id，获取到对应的题目、提交信息（包含代码、编程语言等）
        QuestionSubmit questionSubmit = questionSubmitService.getById(questionSubmitID);
        if (questionSubmit == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "提交信息不存在");
        }
        Long questionId = questionSubmit.getQuestionId();
        Question question = questionService.getById(questionId);
        if (question == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "题目不存在");
        }
        // 2）如果题目提交状态不为等待中，就不用重复执行了
        if (!questionSubmit.getStatus().equals(QuestionSubmitStatusEnum.WAITING.getValue())) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "题目正在判题中");
        }
        // 3）更改判题（题目提交）的状态为 “判题中”，防止重复执行
        QuestionSubmit questionSubmitUpdate = new QuestionSubmit();
        questionSubmitUpdate.setId(questionSubmitID);
        questionSubmitUpdate.setStatus(QuestionSubmitStatusEnum.RUNNING.getValue());
        boolean update = questionSubmitService.updateById(questionSubmitUpdate);
        if (!update) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "题目状态更新错误");
        }
        // 4）调用沙箱，获取到执行结果
        CodeSandboxFactory codeSandboxFactory = new CodeSandboxFactory();
        CodeSandbox codeSandbox = codeSandboxFactory.newInstance(type);
        codeSandbox = new CodeSandboxProxy(codeSandbox);
        String language = questionSubmit.getLanguage();
        String code = questionSubmit.getCode();
        Integer modeSelect = questionSubmit.getModeSelect();
        String helpCode;
        // 获取输入用例
        //code在此处修改
        switch (modeSelect) {
            case 1:
                //好像AMC模式不需要处理
                break;
            case 2:
                helpCode = question.getHelpCode();
                code = CodeUtils.dealWithCCMCode(helpCode, code);
                break;
            case 4:
                helpCode = question.getHelpCode();
                code = CodeUtils.dealWithCMCode(helpCode, code);
                //finish！
                break;
        }
        String judgeCaseStr = question.getJudgeCase();
        List<JudgeCase> judgeCaseList = JSONUtil.toList(judgeCaseStr, JudgeCase.class);
        List<String> inputList = judgeCaseList.stream().map(item->{
            String input = item.getInput();
            //   "换成\"
            String textWithBackslash = input.replaceAll("\"", "\\\\\"");
            return textWithBackslash;
        }).collect(Collectors.toList());
        ExecuteCodeRequest executeCodeRequest = ExecuteCodeRequest.builder()
                .code(code)
                .language(language)
                .inputList(inputList)
                .modeSelect(modeSelect)
                .build();
        // 执行代码沙箱 运行用户java程序
        ExecuteCodeResponse executeCodeResponse = codeSandbox.executeCode(executeCodeRequest);
        // 获取输出数据
        List<String> outputList = executeCodeResponse.getOutputList();
        // 5）根据沙箱的执行结果，设置题目的判题状态和信息
        JudgeContext judgeContext = new JudgeContext();
        judgeContext.setJudgeInfo(executeCodeResponse.getJudgeInfo());
        judgeContext.setInputList(inputList);
        judgeContext.setOutputList(outputList);
        judgeContext.setJudgeCaseList(judgeCaseList);
        judgeContext.setQuestion(question);
        judgeContext.setQuestionSubmit(questionSubmit);
        // 进行判题
        JudgeInfo judgeInfo = judgeManager.doJudge(judgeContext);
        // 6）修改数据库中的判题结果
        questionSubmitUpdate = new QuestionSubmit();
        questionSubmitUpdate.setId(questionSubmitID);
        questionSubmitUpdate.setStatus(QuestionSubmitStatusEnum.SUCCEED.getValue());
        questionSubmitUpdate.setJudgeInfo(JSONUtil.toJsonStr(judgeInfo));
        update = questionSubmitService.updateById(questionSubmitUpdate);
        if (!update) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "题目状态更新错误");
        }
        QuestionSubmit questionSubmitResult = questionSubmitService.getById(questionId);
        return questionSubmitResult;
    }
}
