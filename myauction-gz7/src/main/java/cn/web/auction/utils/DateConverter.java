package cn.web.auction.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class DateConverter implements Converter<String, Date> {

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	@Override  // String ---> Date 
	public Date convert(String date) {
		try {
			 return sdf.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

}
