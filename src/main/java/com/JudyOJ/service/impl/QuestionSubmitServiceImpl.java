package com.JudyOJ.service.impl;

import com.JudyOJ.common.ErrorCode;
import com.JudyOJ.common.ResultUtils;
import com.JudyOJ.constant.CommonConstant;
import com.JudyOJ.exception.BusinessException;
import com.JudyOJ.exception.ThrowUtils;
import com.JudyOJ.judge.JudgeService;
import com.JudyOJ.model.dto.questionsubmit.QuestionSubmitAddRequest;
import com.JudyOJ.model.dto.questionsubmit.QuestionSubmitQueryRequest;
import com.JudyOJ.model.entity.Question;
import com.JudyOJ.model.entity.QuestionSubmit;
import com.JudyOJ.model.entity.User;
import com.JudyOJ.model.enums.QuestionSubmitLanguageEnum;
import com.JudyOJ.model.enums.QuestionSubmitStatusEnum;
import com.JudyOJ.model.vo.QuestionSubmitVO;
import com.JudyOJ.model.vo.QuestionVO;
import com.JudyOJ.service.QuestionService;
import com.JudyOJ.service.UserService;
import com.JudyOJ.utils.SqlUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.JudyOJ.service.QuestionSubmitService;
import com.JudyOJ.mapper.QuestionSubmitMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;


/**
 * @author Judy
 * @description 针对表【question_submit(题目提交)】的数据库操作Service实现
 * @createDate 2023-10-04 22:23:09
 */
@Service
public class QuestionSubmitServiceImpl extends ServiceImpl<QuestionSubmitMapper, QuestionSubmit>
        implements QuestionSubmitService {

    @Resource
    private QuestionService questionService;

    @Resource
    private UserService userService;

    //@Resource 循环依赖
    private JudgeService judgeService;

    @Autowired
    @Lazy
    public QuestionSubmitServiceImpl(JudgeService judgeService) {
        this.judgeService = judgeService;
    }


    /**
     * 提交题目
     *
     * @param questionSubmitAddRequest
     * @param loginUser
     * @return
     */
    @Override
    public long doQuestionSubmit(QuestionSubmitAddRequest questionSubmitAddRequest, User loginUser) {
        String userCode = questionSubmitAddRequest.getCode();
        Integer modeSelect = questionSubmitAddRequest.getModeSelect();
        //剔除import
//        StringBuilder stringBuilder = new StringBuilder(userCode);
        String[] split = userCode.split("public");
        if (split.length > 1) {
            //有public
            String ACMCode = "public" + split[1];
            if (modeSelect == 1) {
                if (!ACMCode.startsWith("public class Main{") || !ACMCode.endsWith("}")) {
                    //ACM模式不以 public class Main{    } 为结构，抛异常
                    ResultUtils.error(ErrorCode.POST_CODE_ERROR, "请勿修改题目初始模板");
                }
            }
        } else {
            //没有public => 核心代码模式
            split = userCode.split("class");
            String CMCode = "class" + split[1];
            if (modeSelect == 2 || modeSelect == 4) {
                if (!CMCode.startsWith("class Solution {") || !CMCode.endsWith("}")) {
                    //核心代码模式不以 class Solution {    } 为结构，抛异常
                    ResultUtils.error(ErrorCode.POST_CODE_ERROR, "请勿修改题目初始模板");
                }
            }
        }

        // 校验编程语言是否合法
        String language = questionSubmitAddRequest.getLanguage();
        QuestionSubmitLanguageEnum languageEnum = QuestionSubmitLanguageEnum.getEnumByValue(language);
        if (languageEnum == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "编程语言错误");
        }
        long questionId = questionSubmitAddRequest.getQuestionId();
        // 判断实体是否存在，根据类别获取实体
        Question question = questionService.getById(questionId);
        if (question == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 是否已提交题目
        long userId = loginUser.getId();
        // 每个用户串行提交题目
        QuestionSubmit questionSubmit = new QuestionSubmit();
        //userCode要在判题时修改
        questionSubmit.setCode(userCode);
        questionSubmit.setUserId(userId);
        questionSubmit.setQuestionId(questionId);
        questionSubmit.setLanguage(language);
        // 设置初始状态
        questionSubmit.setStatus(QuestionSubmitStatusEnum.WAITING.getValue());
        questionSubmit.setJudgeInfo("{}");
        questionSubmit.setModeSelect(modeSelect);
        boolean save = this.save(questionSubmit);
        if (!save) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "数据插入失败");
        }
        Long questionSubmitId = questionSubmit.getId();
        // 执行判题服务
        CompletableFuture.runAsync(() -> {
            judgeService.doJudge(questionSubmitId);
        });
        return questionSubmitId;
    }


    /**
     * 获取查询包装类（用户根据哪些字段查询，根据前端传来的请求对象，得到 mybatis 框架支持的查询 QueryWrapper 类）
     *
     * @param questionSubmitQueryRequest
     * @return
     */
    @Override
    public QueryWrapper<QuestionSubmit> getQueryWrapper(QuestionSubmitQueryRequest questionSubmitQueryRequest) {
        QueryWrapper<QuestionSubmit> queryWrapper = new QueryWrapper<>();
        if (questionSubmitQueryRequest == null) {
            return queryWrapper;
        }
        String language = questionSubmitQueryRequest.getLanguage();
        Integer status = questionSubmitQueryRequest.getStatus();
        Long questionId = questionSubmitQueryRequest.getQuestionId();
        Long userId = questionSubmitQueryRequest.getUserId();
        String sortField = questionSubmitQueryRequest.getSortField();
        String sortOrder = questionSubmitQueryRequest.getSortOrder();

        // 拼接查询条件
        queryWrapper.eq(StringUtils.isNotBlank(language), "language", language);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(questionId), "questionId", questionId);
        queryWrapper.eq(QuestionSubmitStatusEnum.getEnumByValue(status) != null, "status", status);
        queryWrapper.eq("isDelete", false);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }

    @Override
    public QuestionSubmitVO getQuestionSubmitVO(QuestionSubmit questionSubmit, User loginUser) {
        QuestionSubmitVO questionSubmitVO = QuestionSubmitVO.objToVo(questionSubmit);
        LambdaQueryWrapper<Question> questionVOLambdaQueryWrapper = new LambdaQueryWrapper<>();
        questionVOLambdaQueryWrapper.eq(Question::getId, questionSubmit.getQuestionId());
        Question question = questionService.getOne(questionVOLambdaQueryWrapper);
        QuestionVO questionVO = new QuestionVO();
        BeanUtils.copyProperties(question, questionVO);
        questionSubmitVO.setQuestionVO(questionVO);
        // 脱敏：仅本人和管理员能看见自己（提交 userId 和登录用户 id 不同）提交的代码
        long userId = loginUser.getId();
        // 处理脱敏
        if (userId != questionSubmit.getUserId() && !userService.isAdmin(loginUser)) {
            questionSubmitVO.setCode(null);
        }
        return questionSubmitVO;
    }

    @Override
    public Page<QuestionSubmitVO> getQuestionSubmitVOPage(Page<QuestionSubmit> questionSubmitPage, User loginUser) {
        List<QuestionSubmit> questionSubmitList = questionSubmitPage.getRecords();
        Page<QuestionSubmitVO> questionSubmitVOPage = new Page<>(questionSubmitPage.getCurrent(), questionSubmitPage.getSize(), questionSubmitPage.getTotal());
        if (CollectionUtils.isEmpty(questionSubmitList)) {
            return questionSubmitVOPage;
        }
        List<QuestionSubmitVO> questionSubmitVOList = questionSubmitList.stream()
                .map(questionSubmit -> getQuestionSubmitVO(questionSubmit, loginUser))
                .collect(Collectors.toList());
        questionSubmitVOPage.setRecords(questionSubmitVOList);
        return questionSubmitVOPage;
    }


}




