package com.lzq.selfdiscipline.ta.controller;

import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * controller 超类
 */
public class BaseController {
    @Autowired
    private HttpServletRequest request;

    @Autowired
    private HttpServletResponse response;
}
