package org.haoai.medixhub.ctkb.service;


import com.baomidou.mybatisplus.extension.service.IService;
import org.haoai.medixhub.ctkb.entity.User;

import java.util.List;

public interface UserServiceMP extends IService<User> {

    List<User> findAll();

}
