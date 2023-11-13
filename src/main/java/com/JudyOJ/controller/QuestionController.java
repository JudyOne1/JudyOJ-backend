package com.JudyOJ.controller;

import cn.hutool.json.JSONUtil;
import com.JudyOJ.annotation.AuthCheck;
import com.JudyOJ.common.BaseResponse;
import com.JudyOJ.common.DeleteRequest;
import com.JudyOJ.common.ErrorCode;
import com.JudyOJ.common.ResultUtils;
import com.JudyOJ.constant.UserConstant;
import com.JudyOJ.exception.BusinessException;
import com.JudyOJ.exception.ThrowUtils;
import com.JudyOJ.model.dto.question.*;
import com.JudyOJ.model.dto.questionsubmit.QuestionSubmitAddRequest;
import com.JudyOJ.model.dto.questionsubmit.QuestionSubmitQueryRequest;
import com.JudyOJ.model.entity.Mode;
import com.JudyOJ.model.entity.Question;
import com.JudyOJ.model.entity.QuestionSubmit;
import com.JudyOJ.model.entity.User;
import com.JudyOJ.model.vo.QuestionSubmitVO;
import com.JudyOJ.model.vo.QuestionVO;
import com.JudyOJ.service.QuestionService;
import com.JudyOJ.service.QuestionSubmitService;
import com.JudyOJ.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;


/**
 * 题目接口
 *
 * @author Judy
 */
@RestController
@RequestMapping("/question")
@Slf4j
public class QuestionController {


    @Resource
    private QuestionService questionService;

    @Resource
    private UserService userService;

    @Resource
    private QuestionSubmitService questionSubmitService;

    private final static Gson GSON = new Gson();


    /**
     * 创建
     *
     * @param questionAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Long> addQuestion(@RequestBody QuestionAddRequest questionAddRequest, HttpServletRequest request) {
        if (questionAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Question question = new Question();
        BeanUtils.copyProperties(questionAddRequest, question);
        List<String> tags = questionAddRequest.getTags();
        if (tags != null) {
            question.setTags(GSON.toJson(tags));
        }
        List<JudgeCase> judgeCase = questionAddRequest.getJudgeCase();
        //去除空格
        judgeCase = judgeCase.stream().map(item -> {
            //"text1 \u003d \"abcde\", text2 \u003d \"ace\"
            item.setInput(item.getInput().replace(" ", ""));
            return item;
        }).collect(Collectors.toList());
        if (judgeCase != null) {
            question.setJudgeCase(GSON.toJson(judgeCase));
        }
        JudgeConfig judgeConfig = questionAddRequest.getJudgeConfig();
        if (judgeConfig != null) {
            question.setJudgeConfig(GSON.toJson(judgeConfig));
        }
        //mode
        Integer mode = questionAddRequest.getMode();
        question.setMode(mode);
        //模式判断124,1,2,4[1+2=3][2+4=6][1+4=5][1+2+4=7]
        Mode modedJudge = modeJudgment(mode);
        if (modedJudge.getModeNums() == 0) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "模式未选择");
        }
        if (modedJudge.getIsCCM().intValue() == 1) {
            //核心代码模式 需要有模板代码和辅助模板
            String defaultCode = questionAddRequest.getDefaultCode();
            String helpCode = questionAddRequest.getHelpCode();
            if ((defaultCode != null && !defaultCode.isEmpty()) && (helpCode != null && !helpCode.isEmpty())) {
                question.setDefaultCode(defaultCode);
                question.setHelpCode(helpCode);
            } else {
                return ResultUtils.error(ErrorCode.PARAMS_ERROR, "核心代码模式模板出错");
            }
        } else if (modedJudge.getIsCount().intValue() == 1) {
            //对数器模式 需要有模板代码和对数器代码
            String defaultCode = questionAddRequest.getDefaultCode();
            String countCode = questionAddRequest.getCountCode();
            if ((defaultCode != null && !defaultCode.isEmpty()) && (countCode != null && !countCode.isEmpty())) {
                question.setDefaultCode(defaultCode);
                question.setCountCode(countCode);
            } else {
                return ResultUtils.error(ErrorCode.PARAMS_ERROR, "对数器模式模板出错");
            }
        }
        questionService.validQuestion(question, true);
        User loginUser = userService.getLoginUser(request);
        question.setUserId(loginUser.getId());
        question.setFavourNum(0);
        question.setThumbNum(0);
        boolean result = questionService.save(question);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        long newQuestionId = question.getId();
        return ResultUtils.success(newQuestionId);
    }

    private Mode modeJudgment(int mode) {
        switch (mode) {
            case 1:
                return new Mode(1, 1, 0, 0);
            case 2:
                return new Mode(2, 0, 1, 0);
            case 3:
                return new Mode(3, 1, 1, 0);
            case 4:
                return new Mode(4, 0, 0, 1);
            case 5:
                return new Mode(5, 1, 0, 1);
            case 6:
                return new Mode(6, 0, 1, 1);
            case 7:
                return new Mode(7, 1, 1, 1);
            default:
                return new Mode(0, 0, 0, 0);
        }
    }

    /**
     * 删除
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteQuestion(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        Question oldQuestion = questionService.getById(id);
        ThrowUtils.throwIf(oldQuestion == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可删除
        if (!oldQuestion.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean b = questionService.removeById(id);
        return ResultUtils.success(b);
    }

    /**
     * 更新（仅管理员）
     *
     * @param questionUpdateRequest
     * @return
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateQuestion(@RequestBody QuestionUpdateRequest questionUpdateRequest) {
        if (questionUpdateRequest == null || questionUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Question question = new Question();
        BeanUtils.copyProperties(questionUpdateRequest, question);
        List<String> tags = questionUpdateRequest.getTags();
        if (tags != null) {
            question.setTags(GSON.toJson(tags));
        }
        List<JudgeCase> judgeCase = questionUpdateRequest.getJudgeCase();
        judgeCase = judgeCase.stream().map(item -> {
            //"text1 \u003d \"abcde\", text2 \u003d \"ace\"
            item.setInput(item.getInput().replace(" ", ""));
            return item;
        }).collect(Collectors.toList());
        if (judgeCase != null) {
            question.setJudgeCase(GSON.toJson(judgeCase));
        }
        JudgeConfig judgeConfig = questionUpdateRequest.getJudgeConfig();
        if (judgeConfig != null) {
            question.setJudgeConfig(GSON.toJson(judgeConfig));
        }

        //mode
        Integer mode = questionUpdateRequest.getMode();
        question.setMode(mode);
        //模式判断124,1,2,4[1+2=3][2+4=6][1+4=5][1+2+4=7]
        Mode modedJudge = modeJudgment(mode);
        if (modedJudge.getModeNums() == 0) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "模式未选择");
        }
        if (modedJudge.getIsCCM().intValue() == 1) {
            //核心代码模式 需要有模板代码和辅助模板
            String defaultCode = questionUpdateRequest.getDefaultCode();
            String helpCode = questionUpdateRequest.getHelpCode();
            if ((defaultCode != null && !defaultCode.isEmpty()) && (helpCode != null && !helpCode.isEmpty())) {
                question.setDefaultCode(defaultCode);
                question.setHelpCode(helpCode);
            } else {
                return ResultUtils.error(ErrorCode.PARAMS_ERROR, "核心代码模式模板出错");
            }
        } else if (modedJudge.getIsCount().intValue() == 1) {
            //对数器模式 需要有模板代码和对数器代码
            String defaultCode = questionUpdateRequest.getDefaultCode();
            String countCode = questionUpdateRequest.getCountCode();
            if ((defaultCode != null && !defaultCode.isEmpty()) && (countCode != null && !countCode.isEmpty())) {
                question.setDefaultCode(defaultCode);
                question.setCountCode(countCode);
            } else {
                return ResultUtils.error(ErrorCode.PARAMS_ERROR, "对数器模式模板出错");
            }
        }
        // 参数校验
        questionService.validQuestion(question, false);
        long id = questionUpdateRequest.getId();
        // 判断是否存在
        Question oldQuestion = questionService.getById(id);
        ThrowUtils.throwIf(oldQuestion == null, ErrorCode.NOT_FOUND_ERROR);
        boolean result = questionService.updateById(question);
        return ResultUtils.success(result);
    }

    /**
     * 根据 id 获取
     *
     * @param id
     * @return
     */
    @GetMapping("/get")
    public BaseResponse<Question> getQuestionById(long id, HttpServletRequest request) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Question question = questionService.getById(id);
        if (question == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        // 不是本人或管理员，不能直接获取所有信息
        if (!question.getUserId().equals(loginUser.getId()) && !userService.isAdmin(loginUser)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        return ResultUtils.success(question);
    }

    /**
     * 根据 id 获取（脱敏）
     *
     * @param id
     * @return
     */
    @GetMapping("/get/vo")
    public BaseResponse<QuestionVO> getQuestionVOById(long id, HttpServletRequest request) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Question question = questionService.getById(id);
        if (question == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        return ResultUtils.success(questionService.getQuestionVO(question, request));
    }

    /**
     * 分页获取列表（封装类）
     *
     * @param questionQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page/vo")
    public BaseResponse<Page<QuestionVO>> listQuestionVOByPage(@RequestBody QuestionQueryRequest questionQueryRequest,
                                                               HttpServletRequest request) {
        long current = questionQueryRequest.getCurrent();
        long size = questionQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<Question> questionPage = questionService.page(new Page<>(current, size),
                questionService.getQueryWrapper(questionQueryRequest));
        return ResultUtils.success(questionService.getQuestionVOPage(questionPage, request));
    }

    /**
     * 分页获取当前用户创建的资源列表
     *
     * @param questionQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/my/list/page/vo")
    public BaseResponse<Page<QuestionVO>> listMyQuestionVOByPage(@RequestBody QuestionQueryRequest questionQueryRequest,
                                                                 HttpServletRequest request) {
        if (questionQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        questionQueryRequest.setUserId(loginUser.getId());
        long current = questionQueryRequest.getCurrent();
        long size = questionQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<Question> questionPage = questionService.page(new Page<>(current, size),
                questionService.getQueryWrapper(questionQueryRequest));
        return ResultUtils.success(questionService.getQuestionVOPage(questionPage, request));
    }

    /**
     * 分页获取题目列表（仅管理员）
     *
     * @param questionQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<Question>> listQuestionByPage(@RequestBody QuestionQueryRequest questionQueryRequest,
                                                           HttpServletRequest request) {
        long current = questionQueryRequest.getCurrent();
        long size = questionQueryRequest.getPageSize();
        Page<Question> questionPage = questionService.page(new Page<>(current, size),
                questionService.getQueryWrapper(questionQueryRequest));
        return ResultUtils.success(translateTags(questionPage));
    }

    public Page<Question> translateTags(Page<Question> questionPage) {
        List<Question> questionList = questionPage.getRecords();
        List<Question> collect = questionList.stream().map((question -> {
            List<String> tagList = JSONUtil.toList(question.getTags(), String.class);
            question.setTags(JSONUtil.toJsonStr(tagList));
            return question;
        })).collect(Collectors.toList());
        return questionPage.setRecords(collect);

    }


    /**
     * 编辑（用户）
     *
     * @param questionEditRequest
     * @param request
     * @return
     */
    @PostMapping("/edit")
    public BaseResponse<Boolean> editQuestion(@RequestBody QuestionEditRequest questionEditRequest, HttpServletRequest request) {
        if (questionEditRequest == null || questionEditRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Question question = new Question();
        BeanUtils.copyProperties(questionEditRequest, question);
        List<String> tags = questionEditRequest.getTags();
        if (tags != null) {
            question.setTags(GSON.toJson(tags));
        }
        List<JudgeCase> judgeCase = questionEditRequest.getJudgeCase();
        judgeCase = judgeCase.stream().map(item -> {
            //"text1 \u003d \"abcde\", text2 \u003d \"ace\"
            item.setInput(item.getInput().replace(" ", ""));
            return item;
        }).collect(Collectors.toList());
        if (judgeCase != null) {
            question.setJudgeCase(GSON.toJson(judgeCase));
        }
        JudgeConfig judgeConfig = questionEditRequest.getJudgeConfig();
        if (judgeConfig != null) {
            question.setJudgeConfig(GSON.toJson(judgeConfig));
        }
        // 参数校验
        questionService.validQuestion(question, false);
        User loginUser = userService.getLoginUser(request);
        long id = questionEditRequest.getId();
        // 判断是否存在
        Question oldQuestion = questionService.getById(id);
        ThrowUtils.throwIf(oldQuestion == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可编辑
        if (!oldQuestion.getUserId().equals(loginUser.getId()) && !userService.isAdmin(loginUser)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean result = questionService.updateById(question);
        return ResultUtils.success(result);
    }

    /**
     * 提交题目
     *
     * @param questionSubmitAddRequest
     * @param request
     * @return 提交记录的 id
     */
    @PostMapping("/question_submit/do")
    public BaseResponse<Long> doQuestionSubmit(@RequestBody QuestionSubmitAddRequest questionSubmitAddRequest,
                                               HttpServletRequest request) {
        if (questionSubmitAddRequest == null || questionSubmitAddRequest.getQuestionId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        if (questionSubmitAddRequest.getModeSelect() == null) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "请选择判题模式");
        }
        final User loginUser = userService.getLoginUser(request);
        long questionSubmitId = questionSubmitService.doQuestionSubmit(questionSubmitAddRequest, loginUser);
        return ResultUtils.success(questionSubmitId);
    }

    /**
     * 分页获取题目提交列表（除了管理员外，普通用户只能看到非答案、提交代码等公开信息）
     *
     * @param questionSubmitQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/question_submit/list/page")
    public BaseResponse<Page<QuestionSubmitVO>> listQuestionSubmitByPage(@RequestBody QuestionSubmitQueryRequest questionSubmitQueryRequest,
                                                                         HttpServletRequest request) {
        long current = questionSubmitQueryRequest.getCurrent();
        long size = questionSubmitQueryRequest.getPageSize();
        // 从数据库中查询原始的题目提交分页信息
        Page<QuestionSubmit> questionSubmitPage = questionSubmitService.page(new Page<>(current, size),
                questionSubmitService.getQueryWrapper(questionSubmitQueryRequest));
        final User loginUser = userService.getLoginUser(request);
        // 返回脱敏信息
        return ResultUtils.success(questionSubmitService.getQuestionSubmitVOPage(questionSubmitPage, loginUser));
    }

    /**
     * 获取题目的基础代码
     *
     * @param id 题目ID
     * @return 提交记录的 id
     */
    @GetMapping("/question/getDefaultCode")
    public BaseResponse<String> getDefaultCode(long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Question question = questionService.getById(id);
        if (question == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        String defaultCode = question.getDefaultCode();
        return ResultUtils.success(JSONUtil.toJsonStr(defaultCode));
    }

    @GetMapping("/question/getQuestionSubmitByUser")
    public BaseResponse<QuestionSubmit> getQuestionSubmitByUser(long userId, long questionId) {
        if (userId <= 0 || questionId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        LambdaQueryWrapper<QuestionSubmit> questionSubmitLambdaQueryWrapper = new LambdaQueryWrapper<>();
        questionSubmitLambdaQueryWrapper.eq(QuestionSubmit::getUserId,userId).eq(QuestionSubmit::getQuestionId,questionId).orderByDesc(QuestionSubmit::getCreateTime);

        List<QuestionSubmit> list = questionSubmitService.list(questionSubmitLambdaQueryWrapper);
        QuestionSubmit questionSubmit = list.get(0);
        if (questionSubmit == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        return ResultUtils.success((questionSubmit));
    }


}
