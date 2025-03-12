package com.sky.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;
import com.sky.constant.MessageConstant;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import com.sky.exception.LoginFailedException;
import com.sky.mapper.UserMapper;
import com.sky.properties.WeChatProperties;
import com.sky.service.UserService;
import com.sky.utils.HttpClientUtil;
import io.swagger.util.Json;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service
@Slf4j
public class UserServiceImpl implements UserService {


    public static final String WX_LOGIN_URL = "https://api.weixin.qq.com/sns/jscode2session";

    @Autowired
    private WeChatProperties weChatProperties;
    @Autowired
    private UserMapper userMapper;

    /**
     * 微信登录
     * @param userLoginDTO
     * @return
     */
    @Override
    public User wxLogin(UserLoginDTO userLoginDTO) {

        String openId = getOpenId(userLoginDTO.getCode());

        //判断openid是否为空，如果为空则表示登录失败，抛出业务异常
        if (openId==null){
            throw new LoginFailedException(MessageConstant.LOGIN_FAILED);
        }

        //判断当前用户是否为新用户

        User user = userMapper.getByOpenid(openId);

        //如果是新用户，自动完成注册

        if (user == null) {
            user=User.builder()
                    .openid(openId)
                    .createTime(LocalDateTime.now())
                    .build();
            userMapper.insert(user);
        }

        //返回用户对象

        return user;
    }

    private String getOpenId(String code) {
        //掉哦那个微信接口服务，获得当前微信用户的openid
        Map<String,String> map=new HashedMap<>();
        map.put("appid",weChatProperties.getAppid());
        map.put("secret",weChatProperties.getSecret());
        map.put("js_code", code);
        map.put("grant_type","authorization_code");
        String json=HttpClientUtil.doGet(WX_LOGIN_URL,map);

        JSONObject jsonObject= JSON.parseObject(json);
        String openId = jsonObject.getString("openid");
        return openId;
    }
}
