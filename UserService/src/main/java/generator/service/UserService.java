package generator.service;

import com.baomidou.mybatisplus.extension.service.IService;
import generator.domain.User;

/**
* @author Altria
* @description 针对表【user】的数据库操作Service
* @createDate 2025-07-07 18:06:26
*/
public interface UserService extends IService<User> {

	void register(User user);
}
