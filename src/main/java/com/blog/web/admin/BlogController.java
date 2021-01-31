package com.blog.web.admin;/*
 *@author Goddran
 *@date   2020/9/20{TIME}
 *@version 1.0
 */


import com.blog.po.Blog;
import com.blog.po.User;
import com.blog.service.BlogService;
import com.blog.service.TagService;
import com.blog.service.TypeService;
import com.blog.vo.BlogQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


@Controller
@RequestMapping("/admin")
public class BlogController {

    private static final String INPUT = "admin/blogs-input";
    private static final String LIST = "admin/blogs";
    private static final String REDIRECT_LIST = "redirect:/admin/blogs";

    @Autowired
    private BlogService blogService;
    @Autowired
    private TypeService typeService;
    @Autowired
    private TagService tagService;

    @GetMapping("/blogs")
    public String blogs(@PageableDefault(size = 2, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable,
                        BlogQuery blog, Model model) {
        model.addAttribute("types", typeService.listType());
        model.addAttribute("page", blogService.listBlog(pageable, blog));
        return LIST;
    }

    @PostMapping("/blogs/search")
    public String search(@PageableDefault(size = 5, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable,
                         BlogQuery blog, Model model) {
        model.addAttribute("page", blogService.listBlog(pageable, blog));
        return "admin/blogs::blogList";
    }

    @GetMapping("/blogs/input")
    public String input(Model model) {
        setTypeAndTag(model);
        model.addAttribute("blog", new Blog());
        return INPUT;
    }

    private void setTypeAndTag(Model model) {
        model.addAttribute("types", typeService.listType());
        model.addAttribute("tags", tagService.listTag());
    }

    @GetMapping("/blogs/{id}/input")
    public String editInput(@PathVariable Long id, Model model) {
        setTypeAndTag(model);
        Blog blog = blogService.getBlog(id);
        blog.init();
        model.addAttribute("blog", blogService.getBlog(id));
        return INPUT;
    }

    @PostMapping("/blogs-input/upload")
    public String upload(MultipartFile firstPicture) throws IOException {
        String fileName = new SimpleDateFormat("yyyyMMddHHmm'.png'").format(new Date());
        firstPicture.transferTo(new File("F:/image/" + fileName));
        return fileName;
    }

    @PostMapping("/blogs")
    public String post(Blog blog, RedirectAttributes attributes, HttpSession session) {
        blog.setUser((User) session.getAttribute("user"));
        blog.setType(typeService.getType(blog.getType().getId()));
        //for(Tag tag:tagService.listTag()){
        //    if (blog.getTagIds().equals(tag)){
        //        tagService.
        //    }
        //}
        blog.setTags(tagService.listTag(blog.getTagIds()));
        Blog b;
        if (blog.getId() == null) {
            b = blogService.saveBlog(blog);
        } else {
            b = blogService.updateBlog(blog.getId(), blog);
        }
        if (b == null) {
            //没保存成功
            attributes.addFlashAttribute("message", "操作失败");
        } else {
            //成功了
            attributes.addFlashAttribute("message", "操作成功");
        }

        return REDIRECT_LIST;
    }

    @GetMapping("/blogs/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes attributes) {
        blogService.deleteBlog(id);
        attributes.addFlashAttribute("message", "删除成功");
        return REDIRECT_LIST;
    }


    /**
     * 上传附件
     *
     * @param file    file
     * @param request request
     * @return Map
     */
    //@PostMapping(value = "/upload", produces = {"application/json;charset=UTF-8"})
    //@ResponseBody
    //@SystemLog(description = "上传文件", type = LogTypeEnum.ATTACHMENT)
    //public Map<String, Object> upload(@RequestParam("file") MultipartFile file,
    //                                  HttpServletRequest request) {
    //    return uploadAttachment(file, request);
    //}
    //
    //
    //public Map<String, Object> uploadAttachment(@RequestParam("file") MultipartFile file,
    //                                            HttpServletRequest request) {
    //
    //    //Long userId = getLoginUserId();
    //    final Map<String, Object> result = new HashMap<>(3);
    //    if (!file.isEmpty()) {
    //        try {
    //            final Map<String, String> resultMap = attachmentService.upload(file, request);
    //            if (resultMap == null || resultMap.isEmpty()) {
    //                log.error("File upload failed");
    //                result.put("success", ResultCodeEnum.FAIL.getCode());
    //                result.put("message", localeMessageUtil.getMessage("code.admin.attachment.upload-failed"));
    //                return result;
    //            }
    //            //保存在数据库
    //            Attachment attachment = new Attachment();
    //            attachment.setUserId(userId);
    //            attachment.setAttachName(resultMap.get("fileName"));
    //            attachment.setAttachPath(resultMap.get("filePath"));
    //            attachment.setAttachSmallPath(resultMap.get("smallPath"));
    //            attachment.setAttachType(file.getContentType());
    //            attachment.setAttachSuffix(resultMap.get("suffix"));
    //            attachment.setCreateTime(DateUtil.date());
    //            attachment.setAttachSize(resultMap.get("size"));
    //            attachment.setAttachWh(resultMap.get("wh"));
    //            attachment.setAttachLocation(resultMap.get("location"));
    //            attachmentService.insert(attachment);
    //            log.info("Upload file {} to {} successfully", resultMap.get("fileName"), resultMap.get("filePath"));
    //            result.put("success", ResultCodeEnum.SUCCESS.getCode());
    //            result.put("message", localeMessageUtil.getMessage("code.admin.attachment.upload-success"));
    //            result.put("url", attachment.getAttachPath());
    //            result.put("filename", resultMap.get("filePath"));
    //        } catch (Exception e) {
    //            log.error("Upload file failed:{}", e.getMessage());
    //            result.put("success", ResultCodeEnum.FAIL.getCode());
    //            result.put("message", localeMessageUtil.getMessage("code.admin.attachment.upload-failed"));
    //        }
    //    } else {
    //        log.error("File cannot be empty!");
    //    }
    //    return result;
    //}
}
