package cn.web.auction.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.web.auction.mapper.AuctionCustomMapper;
import cn.web.auction.mapper.AuctionMapper;
import cn.web.auction.mapper.AuctionrecordMapper;
import cn.web.auction.pojo.Auction;
import cn.web.auction.pojo.AuctionCustom;
import cn.web.auction.pojo.AuctionExample;
import cn.web.auction.pojo.Auctionrecord;
import cn.web.auction.service.AuctionService;
import cn.web.auction.utils.AuctionPriceException;

@Service
@Transactional
public class AuctionServiceImpl implements AuctionService {

	@Autowired
	private AuctionMapper auctionMapper;
	@Autowired
	private AuctionCustomMapper  auctionCustomMapper;
	@Autowired
	private AuctionrecordMapper recordMapper;
	
	@Override
	public List<Auction> findAuctions(Auction auction) {
		AuctionExample example = new AuctionExample();
		
		AuctionExample.Criteria critera = example.createCriteria();
		if (auction != null) {
			//1. 商品名称模糊查询
			if (auction.getAuctionname()!=null && !"".equals(auction.getAuctionname())) {
				critera.andAuctionnameLike("%" + auction.getAuctionname() + "%");
			}
			//2. 商品描述 模糊查询
			if (auction.getAuctiondesc() != null && !"".equals(auction.getAuctiondesc())) {
				critera.andAuctiondescLike("%" + auction.getAuctiondesc() + "%");
			}
		    //3. 起拍时间  大于
			if(auction.getAuctionstarttime() != null) {
				critera.andAuctionstarttimeGreaterThan(auction.getAuctionstarttime());
			}
			//4. 结束时间  小于
			if (auction.getAuctionendtime() != null) {
				critera.andAuctionendtimeLessThan(auction.getAuctionendtime());
			}
			//5. 起拍价  大于
			if (auction.getAuctionstartprice() != null) {
				critera.andAuctionstartpriceGreaterThan(auction.getAuctionstartprice());
			}
		}
		//按照起拍时间降序排列
		example.setOrderByClause("auctionstarttime desc");
		List<Auction> list = auctionMapper.selectByExample(example);

		return list;
	}

	@Override
	public void addAuction(Auction auction) {
		auctionMapper.insert(auction);
	}

	@Override
	public Auction findAuctionAndRecordListById(int auctionid) {
		return auctionCustomMapper.findAuctionAndRecordListById(auctionid);
	}

	/**
	 -- 竞拍时间不能过期
	 -- 如果是首次竞价，价格不能低于起拍价
	 -- 如果后续的竞价，价格必须高于所有价格的最高价
	 * @throws Exception 
	 */
	@Override
	public void addAuctionRecord(Auctionrecord record) throws Exception {
		//1. 查询出商品的详情数据
		Auction auction = auctionCustomMapper.findAuctionAndRecordListById(record.getAuctionid());
		//auctionMapper.selectByPrimaryKey(record.getAuctionid());
		// 判断拍卖时间
		if (auction.getAuctionendtime().after(new Date()) == false) { //过期
			//抛异常
			throw new AuctionPriceException("拍卖时间已经过期！");
		} else {
			//判断价格
			if (auction.getAuctionrecordList()!=null && auction.getAuctionrecordList().size()>0) {//后续竞拍
				//竞拍记录的最高价
				Auctionrecord maxRecord = auction.getAuctionrecordList().get(0);
				if (record.getAuctionprice() < maxRecord.getAuctionprice()) {
					throw new AuctionPriceException("价格必须高于所有价格的最高价");
				}
			} else {//首次竞拍
				if (record.getAuctionprice() < auction.getAuctionstartprice()) {
					throw new AuctionPriceException("价格不能低于起拍价");
				}
			}
		}
		recordMapper.insert(record);
	}
	
	@Override
	public List<AuctionCustom> findAuctionEndtime() {
		// TODO Auto-generated method stub
		return auctionCustomMapper.findAuctionEndtime();
	}

	@Override
	public List<Auction> findAuctionNoendtime() {
		// TODO Auto-generated method stub
		return auctionCustomMapper.findAuctionNoendtime();
	}

}
