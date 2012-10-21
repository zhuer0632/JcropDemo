package com.me.control;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("Image")
public class Image
{
    
    @RequestMapping("test01")
    public String test01()
    {
        return "test01";
    }


    @RequestMapping("test02")
    public String test02()
    {
        return "test02";
    }
}
