package cn.web.auction.shiro;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;

public class CustomFormAuthenticationFilter extends FormAuthenticationFilter {

	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
		HttpServletRequest req = (HttpServletRequest) request;
		//先判断验证码
		//先获取用户输入的验证码
		String validateCode = req.getParameter("valideCode");
		//系统的验证码
		String randomNum = (String) req.getSession().getAttribute("vrifyCode"); 
		if (validateCode!=null && randomNum!=null && !validateCode.equals(randomNum)) {
			request.setAttribute(DEFAULT_ERROR_KEY_ATTRIBUTE_NAME, "ErrorValideCode");
			return true;  //返回doLogin
		}
		return super.onAccessDenied(request, response);
	}
	
	@Override
	protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request,
			ServletResponse response) throws Exception {
		WebUtils.getAndClearSavedRequest(request);
		WebUtils.redirectToSavedRequest(request, response, "/queryAuctions");
		return false;
	}
}
