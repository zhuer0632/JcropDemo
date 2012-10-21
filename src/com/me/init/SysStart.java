package com.me.init;


import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.web.context.ContextLoaderListener;

import com.me.ut.WebPath;



public class SysStart extends ContextLoaderListener implements ServletContextListener
{

    @Override
    public void contextDestroyed(ServletContextEvent event)
    {
    }


    @Override
    public void contextInitialized(ServletContextEvent event)
    {
        String syspath = event.getServletContext().getContextPath();
        WebPath.SYS_PATH=syspath;
        System.out.println(syspath);
    }

}
