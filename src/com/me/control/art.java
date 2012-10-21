package com.me.control;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;


@Controller
@RequestMapping("art")
public class art
{

    @RequestMapping("upload_page")
    public ModelAndView upload_page(@RequestParam("parent_page_id") String parent_page_id)
    {
        ModelAndView mod = new ModelAndView();
        mod.addObject("parent_page_id",
                      parent_page_id);
        mod.setViewName("upload_page");
        return mod;
    }

}
