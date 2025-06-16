package org.haoai.medixhub.ctkb.service.impl;

import org.haoai.medixhub.ctkb.dao.UserDAO;
import org.haoai.medixhub.ctkb.entity.User;
import org.haoai.medixhub.ctkb.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDAO userDAO;

    @Override
    public List<User> findAll() {
        return userDAO.selectList(null);
    }

    @Override
    public void save(User user) {
        userDAO.insert(user);
    }
}
