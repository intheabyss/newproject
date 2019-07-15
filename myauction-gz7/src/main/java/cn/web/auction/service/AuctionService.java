package cn.web.auction.service;

import java.util.List;

import cn.web.auction.pojo.Auction;
import cn.web.auction.pojo.AuctionCustom;
import cn.web.auction.pojo.Auctionrecord;

public interface AuctionService {

	public List<Auction> findAuctions(Auction auction);
	
	public void addAuction(Auction auction);
	
	public Auction findAuctionAndRecordListById(int auctionid);

	public void addAuctionRecord(Auctionrecord record) throws Exception ;
	
	public List<AuctionCustom> findAuctionEndtime();
	
	public List<Auction> findAuctionNoendtime();
}
