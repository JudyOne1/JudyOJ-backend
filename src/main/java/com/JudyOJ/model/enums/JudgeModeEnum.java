package com.JudyOJ.model.enums;

import org.apache.commons.lang3.ObjectUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 判题信息消息枚举
 *
 */
public enum JudgeModeEnum {

    ACM_MODE("acm模式", "1"),
    CCM_MODE("核心代码模式", "2"),
    COUNT_MODE("对数器", "4"),
    ACM_CCM_MODE("acm模式+核心代码模式", "3"),
    ACM_COUNT_MODE("acm模式+对数器", "5"),
    CCM_COUNT_MODE("核心代码模式+对数器", "6"),
    ACM_CCM_COUNT_MODE("acm模式+对数器", "7");

    private final String text;

    private final String value;

    JudgeModeEnum(String text, String value) {
        this.text = text;
        this.value = value;
    }

    /**
     * 获取值列表
     *
     * @return
     */
    public static List<String> getValues() {
        return Arrays.stream(values()).map(item -> item.value).collect(Collectors.toList());
    }

    /**
     * 根据 value 获取枚举
     *
     * @param value
     * @return
     */
    public static JudgeModeEnum getEnumByValue(String value) {
        if (ObjectUtils.isEmpty(value)) {
            return null;
        }
        for (JudgeModeEnum anEnum : JudgeModeEnum.values()) {
            if (anEnum.value.equals(value)) {
                return anEnum;
            }
        }
        return null;
    }

    public String getValue() {
        return value;
    }

    public String getText() {
        return text;
    }
}
