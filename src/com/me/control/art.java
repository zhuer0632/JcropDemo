package com.me.control;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("art")
public class art {

    @RequestMapping("upload_page")
    public ModelAndView upload_page(
	    @RequestParam("requestid") String requestid,
	    @RequestParam("fieldName") String fieldName) {
	ModelAndView mod = new ModelAndView();
	mod.addObject("requestid", requestid);
	mod.addObject("fieldName", fieldName);
	mod.setViewName("upload_page");
	return mod;
    }

}
