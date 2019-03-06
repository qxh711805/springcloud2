package com.example.service_zuul;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component
public class MyFilter extends ZuulFilter {

    //添加log日志
    private static Logger log= LoggerFactory.getLogger(MyFilter.class);
    //过滤的顺序
    //pre：路由之前
    //routing：路由之时
    //post： 路由之后
    //error：发送错误调用
    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    //添加过滤token
    @Override
    public Object run() throws ZuulException {
        RequestContext requestContext = RequestContext.getCurrentContext();
        //获取httpServlet的url请求参数
        HttpServletRequest request = requestContext.getRequest();
        log.info(String.format("%s>>>%s",request.getMethod(),request.getRequestURI().toString()));
        String token = request.getParameter("token");
        if (token==null){
            //添加警告为空
            log.warn("token is empty");
            requestContext.setSendZuulResponse(false);
            requestContext.setResponseStatusCode(401);
            try {
                requestContext.getResponse().getWriter().write("token is empty");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
        log.info("ok");
        return null;
    }
}
