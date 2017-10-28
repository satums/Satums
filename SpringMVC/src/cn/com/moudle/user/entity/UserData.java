package cn.com.moudle.user.entity;

import java.util.List;

public interface UserData {
public void save();
public void delete();
public void update();
public List<User> queryAll();
public User queryByCode(String code);

}
