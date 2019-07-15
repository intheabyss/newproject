package cn.web.auction.controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.code.kaptcha.impl.DefaultKaptcha;

import cn.web.auction.pojo.User;
import cn.web.auction.service.UserService;

@Controller
public class UserController {
	
	@Autowired
	private DefaultKaptcha captchaProducer;

	@Autowired
	private UserService userService;
	
	@GetMapping("/login")
	public String login() {
		return "login";
	}
	
	@GetMapping("/toRegister")
	public String toRegister(@ModelAttribute("registerUser") User user) {
		return "register";
	}
	
	@RequestMapping("/doLogin")
	public String doLogin(HttpServletRequest request,Model model) {
		//处理错误消息: 异常名称
		String exceptionError = (String) request.getAttribute(FormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME);
		if (exceptionError != null) {
			if (UnknownAccountException.class.getName().equals(exceptionError)) {
				model.addAttribute("errorMsg", "帐号错误！");
			} else if(IncorrectCredentialsException.class.getName().equals(exceptionError)) {
				model.addAttribute("errorMsg", "密码错误！");
			} else if("ErrorValideCode".equals(exceptionError)) {
				model.addAttribute("errorMsg", "验证码错误！");
			}
			else {
				model.addAttribute("errorMsg", "未知错误！");
			}
		}
		return "login";
		
	}
	
//	@RequestMapping("/doLogin")
//	public String doLogin(String username,String password,String valideCode,
//							HttpSession session,Model model) {
//		//1. 验证验证码
//		String randomNum = (String) session.getAttribute("vrifyCode");
//		if (!valideCode.equals(randomNum)) {
//			model.addAttribute("errorMsg", "验证码错误！");
//			return "login";
//		}
//		//2. 查询数据库，验证用户身份
//		User loginUser = userService.login(username, password);
//		if (loginUser != null) {
//			session.setAttribute("user", loginUser);
//			return "redirect:/queryAuctions";
//		} else {
//			model.addAttribute("errorMsg", "帐号或密码错误！");
//			return "login";  // login.html 模板页
//		}
//		
//	}
	
	/**
	 * 获取验证码
	 * 
	 * @param httpServletRequest
	 * @param httpServletResponse
	 * @throws Exception
	 */
	@RequestMapping("/defaultKaptcha")
	public void defaultKaptcha(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
			throws Exception {
		byte[] captchaChallengeAsJpeg = null;
		ByteArrayOutputStream jpegOutputStream = new ByteArrayOutputStream();
		try {
			// 生产验证码字符串并保存到session中
			String createText = captchaProducer.createText();
			httpServletRequest.getSession().setAttribute("vrifyCode", createText);
			// 使用生产的验证码字符串返回一个BufferedImage对象并转为byte写入到byte数组中
			BufferedImage challenge = captchaProducer.createImage(createText);
			ImageIO.write(challenge, "jpg", jpegOutputStream);
		} catch (IllegalArgumentException e) {
			httpServletResponse.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}

		// 定义response输出类型为image/jpeg类型，使用response输出流输出图片的byte数组
		captchaChallengeAsJpeg = jpegOutputStream.toByteArray();
		httpServletResponse.setHeader("Cache-Control", "no-store");
		httpServletResponse.setHeader("Pragma", "no-cache");
		httpServletResponse.setDateHeader("Expires", 0);
		httpServletResponse.setContentType("image/jpeg");
		ServletOutputStream responseOutputStream = httpServletResponse.getOutputStream();
		responseOutputStream.write(captchaChallengeAsJpeg);
		responseOutputStream.flush();
		responseOutputStream.close();
	}
	
//	@RequestMapping("/doLogout")
//	public String doLogout(HttpSession session) {
//		session.invalidate();
//		return "login";
//	}
	
	//BindingResult 相当于错误消息的容器，该对象必须紧跟校验对象之后
	//@ModelAttribute("registerUser") 表单的数据的回显
	@RequestMapping("/register")
	public String register(Model model, @ModelAttribute("registerUser") @Validated User user,BindingResult bindingResult) {
		//1. 先作检验
		if(bindingResult.hasErrors()) {  //验证不通过
			//提取验证错误消息
			List<FieldError> list = bindingResult.getFieldErrors();
			for (FieldError fieldError : list) {  // 分别把错误的消息存放到作用域中。
				//键的名称使用输入框的字段名
				model.addAttribute(fieldError.getField(), fieldError.getDefaultMessage());
			}
			//返回到register.html
			return "register";
		}
		//2. 把用户对象保存到数据库中
		userService.addUser(user);
		return "login";
		
	}
	
}
