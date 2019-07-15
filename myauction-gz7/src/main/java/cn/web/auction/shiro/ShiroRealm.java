package cn.web.auction.shiro;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import cn.web.auction.pojo.User;
import cn.web.auction.service.UserService;

public class ShiroRealm extends AuthorizingRealm {
	
	@Autowired
	private UserService userService;

    //认证
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		//先获取用户的输入的帐号
		String username = (String) token.getPrincipal();
		
		User user = userService.findUserByUsername(username);
		if (user == null) { // 帐号不存在
			return null;  // UnknownAccountException
		}
		
		//查询用户之下的菜单
		//第2个参数 指定数据库中查出的密码   IncroecctCreditionalException
		SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(user, user.getUserpassword(), "ShiroRealm");
		return info;
	}
	
	
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection arg0) {
		
		return null;
	}


}
