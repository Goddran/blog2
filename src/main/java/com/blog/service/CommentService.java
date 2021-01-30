package com.blog.service;

import com.blog.po.Comment;

import java.util.List;

/**
 * @author Goddran
 * @version 1.0
 * @date 2021/1/2713:40
 */

public interface CommentService {

    List<Comment> listCommentByBlogId(Long blogId);

    Comment saveComment(Comment comment);
}

