package com.milanuo.springboot2mybatisforum.module.index;

import com.milanuo.springboot2mybatisforum.core.PageResult.BasePageResult;
import com.milanuo.springboot2mybatisforum.core.PageResult.TagsTopicsPageResult;
import com.milanuo.springboot2mybatisforum.core.Query4Object.Query4Topics;
import com.milanuo.springboot2mybatisforum.core.ajax.AjaxResult;
import com.milanuo.springboot2mybatisforum.module.tags.pojo.TagsWithNum;
import com.milanuo.springboot2mybatisforum.module.tags.service.TagsService;
import com.milanuo.springboot2mybatisforum.module.topic.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/tags")
public class TagsController {

    @Autowired
    private TagsService tagsService;

    @Autowired
    private TopicService topicService;


    @GetMapping("/gettags")
    @ResponseBody
    public AjaxResult gettags() {

        AjaxResult ajaxResult = new AjaxResult();

        try {
            String tagStr = tagsService.getTags();
            ajaxResult.setDatas(tagStr.split(","));
            ajaxResult.setSuccessful(true);
            ajaxResult.setDescribe("查询成功");
        } catch (Exception e) {
            e.printStackTrace();
            ajaxResult.setDescribe("没有查询到数据");
            ajaxResult.setSuccessful(false);
        }

        return ajaxResult;
    }

    @GetMapping("/tagspage")
    public String tagspage(Integer pageNum, Model model){

        Query4Topics query4Topics = new Query4Topics();
        if(pageNum!=null){
            query4Topics.setPageNum(pageNum);
        }else {
            query4Topics.setPageNum(1);
        }
        query4Topics.setPageSize(16);
        List<TagsWithNum> tagsList = tagsService.getAllTags(query4Topics);
        BasePageResult basePageResult = new BasePageResult();
        basePageResult.setPageNum(query4Topics.getPageNum());
        basePageResult.setPageSize(query4Topics.getPageSize());
        basePageResult.setTotalCount(tagsService.getAllTagsCount(query4Topics));
        model.addAttribute("tagsList",tagsList);
        model.addAttribute("basePageResult",basePageResult);


        return "front/tag/tagspage";
    }

    @GetMapping("{tag}")
    public String tagDetail(@PathVariable String tag,Integer pageNum,Model model){

        Query4Topics query4Topics = new Query4Topics();
        if(pageNum==null){
            query4Topics.setPageNum(1);
        }else{
            query4Topics.setPageNum(pageNum);
        }
        query4Topics.setPageSize(20);
        query4Topics.setTag(tag);
        List<TagsTopicsPageResult> topicList = topicService.getTopicsByTag(query4Topics);
        TagsWithNum tagsWithNum = tagsService.getTagByTag(tag);
        BasePageResult basePageResult = new BasePageResult();
        basePageResult.setPageNum(query4Topics.getPageNum());
        basePageResult.setPageSize(query4Topics.getPageSize());
        basePageResult.setTotalCount(tagsService.getTagByTagCount(query4Topics));
        model.addAttribute("topicList",topicList);
        model.addAttribute("tagsWithNum",tagsWithNum);
        model.addAttribute("basePageResult",basePageResult);



        return "front/tag/tagsTopics";
    }

}
