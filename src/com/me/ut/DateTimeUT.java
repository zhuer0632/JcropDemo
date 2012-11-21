package com.me.ut;

import org.nutz.lang.Times;

public class DateTimeUT {

    
    public static Long getNowNum() {
	return Times.now().getTime();
    }

}
