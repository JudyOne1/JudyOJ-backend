package com.yupi.JudyOJ.model.dto.postfavour;

import java.io.Serializable;
import lombok.Data;

/**
 * 帖子收藏 / 取消收藏请求
 *
 * @author Judy 
 *  
 */
@Data
public class PostFavourAddRequest implements Serializable {

    /**
     * 帖子 id
     */
    private Long postId;

    private static final long serialVersionUID = 1L;
}