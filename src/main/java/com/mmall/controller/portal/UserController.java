package com.mmall.controller.portal;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

/**
 *
 *　　　　　　　　┏┓　　　┏┓+ +
 *　　　　　　　┏┛┻━━━┛┻┓ + +
 *　　　　　　　┃　　　　　　　┃ 　
 *　　　　　　　┃　　　━　　　┃ ++ + + +
 *　　　　　　 ████━████ ┃+
 *　　　　　　　┃　　　　　　　┃ +
 *　　　　　　　┃　　　┻　　　┃
 *　　　　　　　┃　　　　　　　┃ + +
 *　　　　　　　┗━┓　　　┏━┛
 *　　　　　　　　　┃　　　┃　　　　　　　　　　　
 *　　　　　　　　　┃　　　┃ + + + +
 *　　　　　　　　　┃　　　┃　　　　Code is far away from bug with the animal protecting　　　　　　　
 *　　　　　　　　　┃　　　┃ + 　　　　神兽保佑,代码无bug　　
 *　　　　　　　　　┃　　　┃
 *　　　　　　　　　┃　　　┃　　+　　　　　　　　　
 *　　　　　　　　　┃　 　　┗━━━┓ + +
 *　　　　　　　　　┃ 　　　　　　　┣┓
 *　　　　　　　　　┃ 　　　　　　　┏┛
 *　　　　　　　　　┗┓┓┏━┳┓┏┛ + + + +
 *　　　　　　　　　　┃┫┫　┃┫┫
 *　　　　　　　　　　┗┻┛　┗┻┛+ + + +
 *          佛曰:
 *                写字楼里写字间，写字间里程序员；
 *                程序人员写程序，又拿程序换酒钱。
 *                酒醒只在网上坐，酒醉还来网下眠；
 *                酒醉酒醒日复日，网上网下年复年。
 *                但愿老死电脑间，不愿鞠躬老板前；
 *                奔驰宝马贵者趣，公交自行程序员。
 *                别人笑我忒疯癫，我笑自己命太贱；
 *                不见满街漂亮妹，哪个归得程序员？
 *
 *          author ：  linzhou
 *          Date :     17/08/31
 */
@Controller
@RequestMapping("/user/")
public class UserController {

    @Autowired
    private IUserService mIUserService;


    /**
     * 用户登录
     *
     * @param username 账号
     * @param password 密码
     * @param session
     * @return
     */
    @RequestMapping(value = "login.do",method = RequestMethod.POST)
    @ResponseBody//让返回自动转化成json格式
    public ServerResponse<User> login(String username,String password, HttpSession session){
        ServerResponse<User> response = mIUserService.login(username,password);
        if (response.isSuccess()){
            session.setAttribute(Const.CURRENT_USER,response.getData());
        }
        return response;
    }


    /**
     * 登出
     *
     * @param session
     * @return
     */
    @RequestMapping(value = "logout.do",method = RequestMethod.GET)
    @ResponseBody//让返回自动转化成json格式
    public ServerResponse<String> logout( HttpSession session){
        session.removeAttribute(Const.CURRENT_USER);
        return ServerResponse.createBySuccess();
    }


    /**
     * 注册
     *
     * @param user 要注册的用户信息
     * @return
     */
    @RequestMapping(value = "register.do",method = RequestMethod.POST)
    @ResponseBody//让返回自动转化成json格式
    public ServerResponse<String> register(User user){
        return mIUserService.register(user);
    }


    /**
     * 判断账号和邮箱是否存着的验证的验证
     *
     * @param str 账号或者邮箱
     * @param type str是账号或者密码的标志
     * @return
     */
    @RequestMapping(value = "check_valid.do",method = RequestMethod.POST)
    @ResponseBody//让返回自动转化成json格式
    public ServerResponse<String> checkValid(String str,String type){
        return mIUserService.checkValid(str,type);
    }


    /**
     *获取用户信息
     *
     * @param session
     * @return
     */
    @RequestMapping(value = "get_user_info.do",method = RequestMethod.GET)
    @ResponseBody//让返回自动转化成json格式
    public ServerResponse<User> getUserInfo(HttpSession session){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user!=null){
            return ServerResponse.createBySuccess(user);
        }
        return ServerResponse.createByErrorMessage("用户未登录，无法获取用户信息");
    }


    /**
     * 获取密码保护问题
     *
     * @param username 用户名
     * @return
     */
    @RequestMapping(value = "forget_get_question.do",method = RequestMethod.POST)
    @ResponseBody//让返回自动转化成json格式
    public ServerResponse<String> forgetGetQuestion(String username){
        return mIUserService.selectQuestion(username);
    }


    /**
     * 验证密码保护问题答案
     *
     * @param username 用户名
     * @param question 密码保护问题
     * @param answer   用户的答案
     * @return
     */
    @RequestMapping(value = "forget_get_answer.do",method = RequestMethod.POST)
    @ResponseBody//让返回自动转化成json格式
    public ServerResponse<String> forgetCheckAnswer(String username,String question,String answer){
        return mIUserService.checkAnswer(username,question,answer);
    }


    /**
     * 忘记密码下的修改用户密码
     *
     * @param username     用户名
     * @param newPassword  新密码
     * @param forgetoTken  系统标识(用户辨别用户是否有更改密码的权限)
     * @return
     */
    @RequestMapping(value = "forget_reset_password.do",method = RequestMethod.POST)
    @ResponseBody//让返回自动转化成json格式
    public ServerResponse<String> forgetRestPassword(String username,String newPassword,String forgetoTken){
        return mIUserService.forgetRestPassword(username,newPassword,forgetoTken);
    }


    /**
     * 登录状态下的修改密码
     *
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @param session
     * @return
     */
    @RequestMapping(value = "reset_password.do",method = RequestMethod.POST)
    @ResponseBody//让返回自动转化成json格式
    public ServerResponse<String> resetPassword(String oldPassword,String newPassword,HttpSession session){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user==null){
            return ServerResponse.createByErrorMessage("用户未登录，请先登录");
        }
        return mIUserService.resetPassword(oldPassword,newPassword,user);
    }


    /**
     * 修改个人信息
     *
     * @param session
     * @param user 修改的用户信息
     * @return
     */
    @RequestMapping(value = "update_information.do",method = RequestMethod.POST)
    @ResponseBody//让返回自动转化成json格式
    public ServerResponse<User> update_information(HttpSession session,User user){
        User sessionUser = (User) session.getAttribute(Const.CURRENT_USER);
        if (sessionUser==null){
            return ServerResponse.createByErrorMessage("用户未登录");
        }
        user.setId(sessionUser.getId());
        //user.setUsername(sessionUser.getUsername());
        ServerResponse<User> response = mIUserService.update_information(user);
        if (response.isSuccess()){
            response.getData().setUsername(sessionUser.getUsername());
            session.setAttribute(Const.CURRENT_USER,response.getData());
        }
        return response;
    }


    /**
     * 获取用户详情
     *
     * @param session
     * @return
     */
    @RequestMapping(value = "get_information.do",method = RequestMethod.POST)
    @ResponseBody//让返回自动转化成json格式
    public ServerResponse<User> get_information(HttpSession session){
        User sessionUser = (User) session.getAttribute(Const.CURRENT_USER);
        if (sessionUser==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"未登录，需要强制登陆");
        }
        return mIUserService.get_information(sessionUser.getId());
    }

}
