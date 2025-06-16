package org.haoai.medixhub.ctkb.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.haoai.medixhub.ctkb.dao.UserDAO;
import org.haoai.medixhub.ctkb.entity.User;
import org.haoai.medixhub.ctkb.service.UserServiceMP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserServiceMPImpl extends ServiceImpl<UserDAO, User> implements UserServiceMP {

    @Autowired
    private UserDAO userDAO;

    @Override
    public List<User> findAll() {
        return userDAO.selectList(null);
    }
}
