package cn.web.auction.mapper;

import java.util.List;

import cn.web.auction.pojo.Auction;
import cn.web.auction.pojo.AuctionCustom;

public interface AuctionCustomMapper {

	
	public Auction findAuctionAndRecordListById(int auctionid);
	
	public List<AuctionCustom> findAuctionEndtime();
	
	public List<Auction> findAuctionNoendtime();
}
