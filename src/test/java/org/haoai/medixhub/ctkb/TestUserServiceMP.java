package org.haoai.medixhub.ctkb;

import org.haoai.medixhub.ctkb.entity.User;
import org.haoai.medixhub.ctkb.service.UserServiceMP;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TestUserServiceMP {

    @Autowired
    private UserServiceMP userServiceMP;

    @Test
    public void testFindAll(){

        userServiceMP.findAll().forEach(user -> System.out.println("user = " + user));
    }

    @Test
    public void testSave(){
        User user = new User();
        user.setName("aaa").setAge(23);
        userServiceMP.save(user);

    }
}
