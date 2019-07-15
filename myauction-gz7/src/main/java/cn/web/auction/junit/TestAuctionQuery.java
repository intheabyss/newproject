package cn.web.auction.junit;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.web.auction.App;
import cn.web.auction.pojo.Auction;
import cn.web.auction.pojo.Auctionrecord;
import cn.web.auction.service.AuctionService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes= {App.class})
public class TestAuctionQuery {

	@Autowired
	private AuctionService auctionService;
	
	@Test
	public void testQuery() {
		Auction auctionDetail = auctionService.findAuctionAndRecordListById(20);
		System.out.println(auctionDetail.getAuctionname() + "," + auctionDetail.getAuctionstarttime());
		List<Auctionrecord> list = auctionDetail.getAuctionrecordList();
		for (Auctionrecord auctionrecord : list) {
			System.out.println(auctionrecord.getAuctiontime()+","+auctionrecord.getAuctionprice()+","+auctionrecord.getUser().getUsername());
		}
	}
}
