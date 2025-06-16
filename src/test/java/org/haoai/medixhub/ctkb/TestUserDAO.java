package org.haoai.medixhub.ctkb;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.haoai.medixhub.ctkb.dao.UserDAO;
import org.haoai.medixhub.ctkb.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class TestUserDAO {

    @Autowired
    private UserDAO userDAO;

    @Test
    public void testFindAll(){
        List<User> users = userDAO.selectList(null);
        users.forEach(user -> System.out.println("user = " + user));
    }

    @Test
    public void testFindById(){
        User user1 = userDAO.selectById(2);
        System.out.println("user = " + user1);
        user1 = userDAO.selectById(2);
        System.out.println("user = " + user1);
    }

    @Test
    public void testFindLike(){
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("name", "diabetes");
        List<User> users = userDAO.selectList(queryWrapper);
        users.forEach(user -> System.out.println("user = " + user));
    }

    @Test
    public void testSave(){
        User user = new User();
        user.setName("notshow").setAge(45);
        userDAO.insert(user);
    }

    @Test
    public void testUpdateById(){
        User user = userDAO.selectById(3);
        user.setName("new name");
        userDAO.updateById(user);
    }

    @Test
    public void testFindPage(){
        // para1: current page  para2: number of records per page
        IPage<User> page = new Page<>(1, 2);
        IPage<User> userIPage = userDAO.selectPage(page, null);
        long total = userIPage.getTotal();
        System.out.println("total = " + total);
        userIPage.getRecords().forEach(user -> System.out.println("user = " + user));
    }

    @Test
    public void testFindPageWithCondition(){
        // para1: current page  para2: number of records per page
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("age", 45);
        IPage<User> page = new Page<>(1, 2);
        IPage<User> userIPage = userDAO.selectPage(page, queryWrapper);
        long total = userIPage.getTotal();
        System.out.println("total = " + total);
        long pages = userIPage.getPages();
        System.out.println("pages = " + pages);
        userIPage.getRecords().forEach(user -> System.out.println("user = " + user));
    }

}
