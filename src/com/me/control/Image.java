package com.me.control;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.me.ut.StringUT;

@Controller
@RequestMapping("Image")
public class Image {

    @RequestMapping("test01")
    public String test01() {
	return "test01";
    }

    @RequestMapping("test02")
    public ModelAndView test02() {
	ModelAndView mod = new ModelAndView();
	mod.addObject("requestId", StringUT.getUUID());
	mod.addObject("fieldName", "photo");
	mod.setViewName("test02");
	return mod;
    }

    @RequestMapping("test03")
    public String test03() {
	return "test03";
    }

}
