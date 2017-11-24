package com.mmall.service.impl;

import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.common.TokenCache;
import com.mmall.dao.UserMapper;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import com.mmall.util.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;


/**
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

@Service("mIUserService")
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserMapper mUserMapper;

    /**
     * 登录
     *
     * @param username 账号
     * @param password 密码
     * @return
     */
    @Override
    public ServerResponse<User> login( String username, String password) {
        int resultCount = mUserMapper.chekUsername(username);
        if (resultCount == 0){
            return ServerResponse.createByErrorMessage("用户名不存在");
        }
        String md5Password = MD5Util.MD5EncodeUtf8(password);
        User user = mUserMapper.selectLogin(username, md5Password);
        if (user == null)
            return ServerResponse.createByErrorMessage("密码错误");
        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySuccess("登录成功", user);
    }

    /**
     * 注册
     *
     * @param user 要注册的用户信息
     * @return
     */
    @Override
    public ServerResponse<String> register(User user) {

        ServerResponse response = this.checkValid(user.getUsername(),Const.USERNAME);
        if (!response.isSuccess()){
            return response;
        }

        response=this.checkValid(user.getEmail(),Const.EMAIL);
        if (!response.isSuccess()){
            return response;
        }

        user.setRole(Const.Role.ROLE_CUSTOMER);
        //MD5加密
        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));

        int resultCount = mUserMapper.insert(user);
        if (resultCount == 0){
            return ServerResponse.createByErrorMessage("注册失败");
        }

        return ServerResponse.createBySuccessMessage("注册成功");
    }


    /**
     * 判断账号和邮箱是否存着的验证的验证
     *
     * @param str 账号或者邮箱
     * @param type str是账号或者密码的标志
     * @return
     */
    @Override
    public ServerResponse<String> checkValid(String str, String type) {

        if (StringUtils.isNotBlank(type)) {
            if (Const.USERNAME.equals(type)) {
                int resultCount = mUserMapper.chekUsername(str);
                if (resultCount > 0){
                    return ServerResponse.createByErrorMessage("用户名已存在");
                }
            }
            if (Const.EMAIL.equals(type)) {
                int resultCount = mUserMapper.checkEmail(str);
                if (resultCount > 0){
                    return ServerResponse.createByErrorMessage("邮箱已注册");
                }
            }
        } else return ServerResponse.createByErrorMessage("参数错误");

        return ServerResponse.createBySuccessMessage("校验成功");
    }


    /**
     * 获取密码保护问题
     * @param username 用户名
     * @return
     */
    public ServerResponse<String> selectQuestion(String username){
        ServerResponse validResponse = checkValid(username,Const.USERNAME);
        if (validResponse.isSuccess()){//如果校验功说明用户不存在
            return ServerResponse.createByErrorMessage("用户不存在");
        }

        String question = mUserMapper.selectQuestionByUsername(username);
        if (StringUtils.isNotBlank(question)){
            return ServerResponse.createBySuccess(question);
        }
        return ServerResponse.createByErrorMessage("找回密码的问题为空");
    }


    /**
     * 验证密码保护问题答案
     *
     * @param username 用户名
     * @param question 密码保护问题
     * @param answer   用户的答案
     * @return
     */
    @Override
    public ServerResponse<String> checkAnswer(String username, String question, String answer){
        int resultCount = mUserMapper.checkAnswer(username,question,answer);
        if (resultCount>0){//问题答案回答正确
            String forgetToken = UUID.randomUUID().toString();
            TokenCache.setKey(TokenCache.TOKEN_PREFIX+username,forgetToken);
            return ServerResponse.createBySuccess(forgetToken);
        }
        return ServerResponse.createByErrorMessage("答案错误");

    }


    /**
     * 忘记密码下的修改用户密码
     *
     * @param username     用户名
     * @param newPassword  新密码
     * @param forgetoTken  系统标识
     * @return
     */
    @Override
    public ServerResponse<String> forgetRestPassword(String username, String newPassword, String forgetoTken){
        if (StringUtils.isBlank(forgetoTken)){
            return ServerResponse.createByErrorMessage("参数错误，需要传递token");
        }
        ServerResponse validResponse = checkValid(username,Const.USERNAME);
        if (validResponse.isSuccess()){//如果校验功说明用户不存在
            return ServerResponse.createByErrorMessage("用户不存在");
        }
        String token = TokenCache.getKey(TokenCache.TOKEN_PREFIX+username);
        if (StringUtils.isBlank(token)){
            return ServerResponse.createByErrorMessage("token无效或者过期");
        }
        if (StringUtils.equals(token,forgetoTken)){
            String md5Password = MD5Util.MD5EncodeUtf8(newPassword);
            int rowCount = mUserMapper.updatePasswordByUsername(username,md5Password);
            if (rowCount>0){
                return ServerResponse.createBySuccessMessage("修改密码成功");
            }
        }else{
            return ServerResponse.createByErrorMessage("token错误请重新获取");
        }
        return ServerResponse.createByErrorMessage("密码修改失败");
    }

    /**
     * 登录状态下的修改密码
     *
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @return
     */
    public ServerResponse<String> resetPassword(String oldPassword,String newPassword,User user){
        int resultCount = mUserMapper.checkPassword(MD5Util.MD5EncodeUtf8(oldPassword),user.getId());
        if (resultCount==0){
            return ServerResponse.createByErrorMessage("旧密码错误");
        }
        user.setPassword(MD5Util.MD5EncodeUtf8(newPassword));
        int updateCount = mUserMapper.updateByPrimaryKeySelective(user);
        if (updateCount>0){
            return ServerResponse.createBySuccessMessage("修改密码成功");
        }
        return ServerResponse.createByErrorMessage("修改密码失败");
    }


    /**
     * 修改个人信息
     *
     * @param user 修改的用户信息
     * @return
     */
    public ServerResponse<User> update_information(User user){
        //username不能被更新
        //email要校验是否已经存在
        int resultCount = mUserMapper.checkEmailByUserId(user.getEmail(),user.getId());

        if (resultCount>0){
            return ServerResponse.createByErrorMessage("邮箱已经注册，修改失败");
        }
        User updateUser = new User();
        updateUser.setId(user.getId());
        updateUser.setEmail(user.getEmail());
        updateUser.setPhone(user.getPhone());
        updateUser.setQuestion(user.getQuestion());
        updateUser.setAnswer(user.getAnswer());

        int updateCount = mUserMapper.updateByPrimaryKeySelective(updateUser);
        if (updateCount>0){
            return ServerResponse.createBySuccess("修改个人信息成功",updateUser);
        }

        return ServerResponse.createByErrorMessage("修改个人信息失败");

    }


    /**
     * 获取用户信息
     * @param userId 用户id
     * @return
     */
    public ServerResponse<User> get_information(int userId) {
        User user = mUserMapper.selectByPrimaryKey(userId);
        if (user==null){
            return ServerResponse.createByErrorMessage("找不到当前用户");
        }
        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySuccess(user);
    }


    /**
     * 校验是否是管理员
     * @param user
     * @return
     */
    public ServerResponse checkAdminRole(User user){
        if (user!=null&&user.getRole().intValue()==Const.Role.ROLE_ADMIN){
            return ServerResponse.createBySuccess();
        }
        return ServerResponse.createByError();
    }




}