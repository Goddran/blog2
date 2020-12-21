package com.blog.dao;/*
 *@author Goddran
 *@date   2020/9/23{TIME}
 *@version 1.0
 */

import com.blog.po.Blog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlogRepository extends JpaRepository<Blog, Long>, JpaSpecificationExecutor<Blog> {

    @Query("select  b from Blog b where b.recommend = true")
    List<Blog> findTop(Pageable pageable);

    //1表示传值给第一个参数
    @Query("select b from Blog b where b.title like ?1 or b.content like?1")
    Page<Blog> findByQuery(String query, Pageable pageable);

    @Modifying
    @Query("update Blog b set b.views = b.views+1 where b.id = ?1")
    int updateViews(Long id);

    @Query("select function('date_format',b.updateTime,'%Y') as year from Blog b group by function('date_format',b.updateTime,'%Y')order by year DESC ")
    List<String> findGroupYear();

    @Query("select  b from Blog b where function('date_format',b.updateTime,'%Y')= ?1 ")
    List<Blog> findByYear(String year);

    @Query("select b from Blog b where b.title= ?1")
    Blog getByTitle(String title);
}
