package org.haoai.medixhub.ctkb.service;


import org.haoai.medixhub.ctkb.entity.User;

import java.util.List;

public interface UserService {

    List<User> findAll();

    void save(User user);
}
