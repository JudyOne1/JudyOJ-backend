package com.JudyOJ.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 模式
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Mode {

    /**
     * 组合数字
     * 模式判断1,2,4[1+2=3][2+4=6][1+4=5][1+2+4=7]
     */
    Integer modeNums;

    Integer isACM;

    Integer isCCM;

    Integer isCount;


}