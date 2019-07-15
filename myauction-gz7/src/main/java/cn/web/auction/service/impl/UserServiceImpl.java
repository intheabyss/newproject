package cn.web.auction.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.web.auction.mapper.UserMapper;
import cn.web.auction.pojo.User;
import cn.web.auction.pojo.UserExample;
import cn.web.auction.service.UserService;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private UserMapper userMapper;

	@Override
	public User login(String username, String password) {
		UserExample example = new UserExample();
		UserExample.Criteria criteria = example.createCriteria();
		
		criteria.andUsernameEqualTo(username);
		criteria.andUserpasswordEqualTo(password);
		
		List<User> list = userMapper.selectByExample(example);
		if (list != null && list.size()>0) {
			return list.get(0);
		}
		return null;
	}

	@Override
	public void addUser(User user) {
		userMapper.insert(user);
	}

	@Override
	public User findUserByUsername(String username) {
		UserExample example = new UserExample();
		UserExample.Criteria criteria = example.createCriteria();
		
		criteria.andUsernameEqualTo(username);
		
		List<User> list = userMapper.selectByExample(example);
		if (list != null && list.size()>0) {
			return list.get(0);
		}
		return null;
	}

}
