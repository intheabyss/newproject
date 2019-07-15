package cn.web.auction.utils;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import cn.web.auction.interceptor.CheckUserInterceptor;

@Configuration
public class WebMVCConfig extends WebMvcConfigurerAdapter {

	@Autowired
	private FileProperties fileProperties;
	
	//@Autowired
	//private CheckUserInterceptor userInterceptor;
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		//前面表示映射的访问路径 ，后面表示文件存放的物理路径 
		registry.addResourceHandler(fileProperties.getStaticAccessPath()).addResourceLocations("file:"+ fileProperties.getUploadFileFolder() +"/");
	}
	
//	@Override  //注册拦截器对象
//	public void addInterceptors(InterceptorRegistry registry) {
//		//registry.addInterceptor(userInterceptor);
//		//定义排除的路径 
//		List<String> paths = new ArrayList<>();
//		paths.add("/login");
//		paths.add("/toRegister");
//		paths.add("/doLogin");
//		paths.add("/register");
//		paths.add("/defaultKaptcha");
//		paths.add("/css/**");
//		paths.add("/images/**");
//		paths.add("/js/**");
//		registry.addInterceptor(new CheckUserInterceptor()).addPathPatterns("/**").excludePathPatterns(paths);
//	}
}
