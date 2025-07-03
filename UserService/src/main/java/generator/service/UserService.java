package generator.service;

import com.baomidou.mybatisplus.extension.service.IService;
import generator.domain.User;

/**
* @author Altria
* @description 针对表【user】的数据库操作Service
* @createDate 2025-06-17 11:52:57
*/
public interface UserService extends IService<User> {

	void register(User user);
}
