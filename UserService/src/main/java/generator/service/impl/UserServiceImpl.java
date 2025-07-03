package generator.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import generator.domain.User;
import generator.mapper.UserMapper;
import generator.service.UserService;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
* @author Altria
* @description 针对表【user】的数据库操作Service实现
* @createDate 2025-06-17 11:52:57
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

	@Override
	public void register(User user) {
		user.setCreatetime(new Date());
		user.setUpdatetime(new Date());
		save(user);
		System.out.println("注册成功");
	}
}




