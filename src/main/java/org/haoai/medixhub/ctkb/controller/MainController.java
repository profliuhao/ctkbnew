package org.haoai.medixhub.ctkb.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {

    @RequestMapping("/")
    public String main(){
        return "forward:/dist/index.html";
    }

    // Serve the Vue.js app for all non-API routes
    @RequestMapping(value = {"/dashboard", "/conditions", "/criteria", "/analytics", "/search", "/test", "/conditions/**"})
    public String spa() {
        return "forward:/dist/index.html";
    }

    // Keep the old template for backward compatibility
    @RequestMapping("/template")
    public String template(){
        return "template_page";
    }

}
