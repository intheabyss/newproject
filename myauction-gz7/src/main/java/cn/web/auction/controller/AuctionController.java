package cn.web.auction.controller;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.catalina.security.SecurityUtil;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.web.auction.pojo.Auction;
import cn.web.auction.pojo.AuctionCustom;
import cn.web.auction.pojo.Auctionrecord;
import cn.web.auction.pojo.User;
import cn.web.auction.service.AuctionService;

@Controller
public class AuctionController {

	public static final int PAGE_SIZE = 5;
	
	@Autowired
	private AuctionService auctionService;
	
	@GetMapping("/toAddAuction")
	public String toAddAuction() {
		return "addAuction";
	}
	
	@RequestMapping("/publishAuctions")
	public String publishAuctions(Auction auction,MultipartFile pic) {
		//1. 另存文件到服务器文件夹中
		try {
			if (pic.getSize() > 0) {
				String path = "d:/file"; //路径映射
				File targetFile = new File(path, pic.getOriginalFilename());
				pic.transferTo(targetFile);
				
				auction.setAuctionpic(pic.getOriginalFilename());
				auction.setAuctionpictype(pic.getContentType());
			}
			
			auctionService.addAuction(auction);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// postHandler(ModelAndView)
		return "redirect:/queryAuctions"; //afterCompletion
		
	}
	
	
	
	@RequestMapping("/queryAuctions")
	public ModelAndView queryAuctions(@ModelAttribute("condition") Auction auction,
									  @RequestParam(value="pageNum",required=false,defaultValue="1") int pageNum) {
		ModelAndView mv = new ModelAndView();
		
		//分页拦截： 页码，每页记录数
		PageHelper.startPage(pageNum, PAGE_SIZE);
		List<Auction> list = auctionService.findAuctions(auction);  // mysql    sql + limit n,m
		//创建pageBean 分页bean
		PageInfo pageInfo = new PageInfo<>(list);
		
		//从shiro框架中获取用户的对象
		User loginUser = (User) SecurityUtils.getSubject().getPrincipal();
		System.out.println(loginUser);
		mv.addObject("pageInfo", pageInfo);  
		mv.addObject("auctionList", list);
		mv.addObject("user", loginUser);
		mv.setViewName("index");
		return mv;
		
	}
	
	@RequestMapping("/toDetail/{auctionid}")
	public ModelAndView toDetail(@PathVariable int auctionid) {
		Auction auction = auctionService.findAuctionAndRecordListById(auctionid);
		User loginUser = (User) SecurityUtils.getSubject().getPrincipal();
		
		ModelAndView mv = new ModelAndView();
		mv.addObject("user", loginUser);
		mv.addObject("auctionDetail", auction);
		mv.setViewName("auctionDetail");
		return mv;
		
	}
	
	@RequestMapping("/saveAuctionRecord")
	public String saveAuctionRecord(Auctionrecord record,HttpSession session,Model model) throws Exception {
		//User user = (User) session.getAttribute("user");
		User user = (User) SecurityUtils.getSubject().getPrincipal();
		record.setUserid(user.getUserid());
		//拍卖时间
		record.setAuctiontime(new Date());
		
		auctionService.addAuctionRecord(record);
		return "redirect:/toDetail/"+record.getAuctionid();
		
	}
	
	@RequestMapping("/toAuctionResult")
	public ModelAndView toAuctionResult() {
		User user = (User) SecurityUtils.getSubject().getPrincipal();
		List<AuctionCustom> endtimeList = auctionService.findAuctionEndtime();
		List<Auction> noendtimeList = auctionService.findAuctionNoendtime();
		
		ModelAndView mv = new ModelAndView();
		mv.addObject("user", user);
		mv.addObject("endtimeList", endtimeList);
		mv.addObject("noendtimeList", noendtimeList);
		mv.setViewName("auctionResult");
		return mv;
		
	}
}
