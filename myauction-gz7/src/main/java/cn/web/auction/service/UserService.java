package cn.web.auction.service;

import cn.web.auction.pojo.User;

public interface UserService {

	
	public User login(String username,String password);
	
	public User findUserByUsername(String username);  //假设帐号是唯一约束
	
	public void addUser(User user);
}
