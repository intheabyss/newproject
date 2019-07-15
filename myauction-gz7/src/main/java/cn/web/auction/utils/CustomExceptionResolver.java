package cn.web.auction.utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

/**
 * 集中处理软件运行过各产生的各种异常
 * @author gec
 *
 */
@Component
public class CustomExceptionResolver implements HandlerExceptionResolver {

	@Override
	public ModelAndView resolveException(HttpServletRequest req, HttpServletResponse resp, Object arg2,
			Exception ex) {
		AuctionPriceException priceException = null;
		if (ex instanceof AuctionPriceException) {
			priceException = (AuctionPriceException) ex;
		} else  {
			priceException = new AuctionPriceException("未知异常");
		}
		
		ModelAndView mv = new ModelAndView();
		mv.addObject("errorMsg", ex.getMessage());
		mv.setViewName("error");
		return mv;
	}

}
