package generator.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import generator.domain.User;
import generator.mapper.UserMapper;
import generator.service.UserService;
import org.springframework.stereotype.Service;

/**
* @author Altria
* @description 针对表【user】的数据库操作Service实现
* @createDate 2025-07-07 18:06:26
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

	@Override
	public void register(User user) {
		save(user);
	}
}




