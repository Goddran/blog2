package com.blog.dao;

import com.blog.po.Comment;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Goddran
 * @version 1.0
 * @date 2021/1/2713:42
 */
@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    //根据创建时间倒序来排
    List<Comment> findByBlogIdAndParentCommentNull(@Param("blogId") Long blogId, Sort sort);
}
