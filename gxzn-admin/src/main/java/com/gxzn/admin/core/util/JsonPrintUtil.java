package com.gxzn.admin.core.util;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

public class JsonPrintUtil {

    public static void writeJson(HttpServletResponse response,String content) {
        response.reset();
        response.setContentType("application/json");
        response.setHeader("Cache-Control", "no-store");
        response.setCharacterEncoding("UTF-8");
        try {
            PrintWriter pw=response.getWriter();
            pw.write(content);
            pw.flush();
        }catch(Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
