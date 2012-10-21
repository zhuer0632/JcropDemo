package com.me.control;


import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;



@Controller
@RequestMapping("l")
public class l
{

    @RequestMapping("logon")
    public ModelAndView logon()
    {
        ModelAndView mod = new ModelAndView();
        mod.setViewName("login");
        return mod;
    }


}
