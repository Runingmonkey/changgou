package com.changgou.item.controller;

import com.changgou.item.service.PageService;
import com.changgou.util.Result;
import com.changgou.util.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description:
 * @author: QIXIANG LING
 * @date: 2020/7/23 11:49
 */
@RestController
@RequestMapping(value = "/page")
public class PageController {
    @Autowired
    private PageService pageService;

    @GetMapping("/createHtml/{id}")
    public Result createHtml(@PathVariable(value =  "id") String id){
        pageService.createPageHtml(id);
        return new Result<>(true, StatusCode.OK,"成功");
    }

}
