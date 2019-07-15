package cn.web.auction.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * 检测用户是否登录(认证用户)
 * @author gec
 *
 */
//@Component
public class CheckUserInterceptor implements HandlerInterceptor {

	// 返回true: 请求放行，进入到handler  ---> postHandle()--》afterCompletion
	// 返回false:  被拦截，请求不会再 进行
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		HttpSession session = request.getSession();
		if (session.getAttribute("user") != null) { // 合法用户
			return true;
		} else { //非法用户
			//跳转到login.html
			response.sendRedirect("/login");
			return false;
		}
	}
	
	//应用场景： 在handler跳转视图前，修改Model和View
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub
		HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
	}
	
	//应用场景：整个handler完成后执行。
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
	}
}
