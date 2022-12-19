package com.chuhsi.take.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.chuhsi.take.common.R;
import com.chuhsi.take.entity.User;
import com.chuhsi.take.service.UserService;
import com.chuhsi.take.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    RedisTemplate redisTemplate;

    /**
     * 手机验证码
     *
     * @param user
     * @param session
     * @return
     */
    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session) {
        //获取手机号
        String phone = user.getPhone();

        //生成随机的6位数验证码
        if (StringUtils.isNotEmpty(phone)) {
            String code = ValidateCodeUtils.generateValidateCode(6).toString();
            log.info("code = {}", code);

            //在session中保存起来，用于验证
            //session.setAttribute(phone, code);

            //将生成的验证码缓存于reids中
            redisTemplate.opsForValue().set(phone, code, 60, TimeUnit.SECONDS);

            return R.success("手机验证码发送成功");
        }
        return R.error("短信发送失败");
    }

    /**
     * 移动端登陆
     *
     * @param map
     * @param session
     * @return
     */
    @PostMapping("/login")
    public R<User> login(@RequestBody Map map, HttpSession session) {
        log.info(map.toString());

        //获取手机号码
        String phone = map.get("phone").toString();

        //获取验证码
        String code = map.get("code").toString();

        //获取session中保存的手机号码phone
        //Object codeInSession = session.getAttribute(phone);

        //从redis中获取验证码
        Object codeInSession = redisTemplate.opsForValue().get(phone);

        //进行验证和对比
        if (codeInSession != null && codeInSession.equals(code)) {
            //如果能对比成功，说明登录成功

            //添加条件
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getPhone, phone);

            //如果当前手机号码不存在，则为该手机号码注册
            User user = userService.getOne(queryWrapper);
            if (user == null) {
                user = new User();
                user.setPhone(phone);
                user.setStatus(1);
                userService.save(user);
            }

            //把当前用户的id保存一份在session中
            session.setAttribute("user", user.getId());

            //如果用户登录成功，删除redis中的缓存验证码
            redisTemplate.delete(phone);

            return R.success(user);
        }
        return R.error("登录失败！");
    }

    /**
     * 用户登出
     *
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request) {
        //清理Session中保存的当前用户登录的id
        request.getSession().removeAttribute("user");
        return R.success("退出成功");
    }
}
