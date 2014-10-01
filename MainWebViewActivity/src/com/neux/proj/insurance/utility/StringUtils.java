package com.neux.proj.insurance.utility;

import com.neux.proj.insurance.R;

/**
 * Created with IntelliJ IDEA.
 * User: titan
 * Date: 2013/12/30
 * Time: ¤W¤È 10:56
 * To change this template use File | Settings | File Templates.
 */
public class StringUtils {
    public static boolean isEmpty(String s) {
        return (s == null || "".equals(s));
    }

    public static boolean isNotEmpty(String s) {
        return !isEmpty(s);
    }

    public static String leftPad(String s,int count,String append) {
        int current = s.length();
        int c = count - current;

        if(c > 0) {
            for(int i=0;i<c;i++) {
                s = append + s;
            }

            return s;
        }
        else {
            return s;
        }
    }
}
