package com.mmall.controller.backend;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.ICategoryService;
import com.mmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


import javax.servlet.http.HttpSession;

/**
 * 　　　　　　　　┏┓　　　┏┓+ +
 * 　　　　　　　┏┛┻━━━┛┻┓ + +
 * 　　　　　　　┃　　　　　　　┃
 * 　　　　　　　┃　　　━　　　┃ ++ + + +
 * 　　　　　　 ████━████ ┃+
 * 　　　　　　　┃　　　　　　　┃ +
 * 　　　　　　　┃　　　┻　　　┃
 * 　　　　　　　┃　　　　　　　┃ + +
 * 　　　　　　　┗━┓　　　┏━┛
 * 　　　　　　　　　┃　　　┃
 * 　　　　　　　　　┃　　　┃ + + + +
 * 　　　　　　　　　┃　　　┃　　　　Code is far away from bug with the animal protecting
 * 　　　　　　　　　┃　　　┃ + 　　　　神兽保佑,代码无bug
 * 　　　　　　　　　┃　　　┃
 * 　　　　　　　　　┃　　　┃　　+
 * 　　　　　　　　　┃　 　　┗━━━┓ + +
 * 　　　　　　　　　┃ 　　　　　　　┣┓
 * 　　　　　　　　　┃ 　　　　　　　┏┛
 * 　　　　　　　　　┗┓┓┏━┳┓┏┛ + + + +
 * 　　　　　　　　　　┃┫┫　┃┫┫
 * 　　　　　　　　　　┗┻┛　┗┻┛+ + + +
 * 佛曰:
 * 写字楼里写字间，写字间里程序员；
 * 程序人员写程序，又拿程序换酒钱。
 * 酒醒只在网上坐，酒醉还来网下眠；
 * 酒醉酒醒日复日，网上网下年复年。
 * 但愿老死电脑间，不愿鞠躬老板前；
 * 奔驰宝马贵者趣，公交自行程序员。
 * 别人笑我忒疯癫，我笑自己命太贱；
 * 不见满街漂亮妹，哪个归得程序员？
 * ---------------------------
 * 项目名： mmall
 * 包名：   com.mmall.controller.backend
 * 创建者:  linzhou
 * 创建时间:17/09/29
 * 描述:
 */
@Controller
@RequestMapping("/manage/category")
public class CategorManageController {

    @Autowired
    private IUserService mIUserService;

    @Autowired
    private ICategoryService mICategoryService;


    /**
     * 添加分类
     * @param session
     * @param categoryName 分类名称
     * @param parentId 父节点id
     * @return
     */
    @RequestMapping("add_category.do")
    @ResponseBody
    public ServerResponse addCategory(HttpSession session, String categoryName
            , @RequestParam(value = "parentId", defaultValue = "0") int parentId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode()
                    ,"用户未登录，请登陆");
        }
        if (mIUserService.checkAdminRole(user).isSuccess()){//是管理员
            //增加分类
            return mICategoryService.addCategory(categoryName,parentId);
        }else {
            return  ServerResponse.createByErrorMessage("没有操作权限");
        }
    }

    /**
     * 更新品类名称
     * @param session
     * @param categoryName 品类名称
     * @param categoryId   品类id
     * @return
     */
    @RequestMapping("set_categoryname.do")
    @ResponseBody
    public ServerResponse setCategoryName(HttpSession session, String categoryName,Integer categoryId){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode()
                    ,"用户未登录，请登陆");
        }
        if (mIUserService.checkAdminRole(user).isSuccess()){//是管理员
            //修改分类名称
            return mICategoryService.updateCategoryName(categoryName,categoryId);
        }else {
            return  ServerResponse.createByErrorMessage("没有操作权限");
        }
    }

    /**
     * 获取下一个子节点的所有平级节点信息（不递归）
     * @param session
     * @param categoryId 品类id
     * @return
     */
    @RequestMapping("get_category.do")
    @ResponseBody
    public ServerResponse getChildrenParallelCategory(HttpSession session ,@RequestParam(value = "categoryId" ,defaultValue ="0") Integer categoryId ){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode()
                    ,"用户未登录，请登陆");
        }
        if (mIUserService.checkAdminRole(user).isSuccess()){//是管理员
            //查询子节点的集合，不递归
            return mICategoryService.getChildrenParallelCategory(categoryId);
        }else {
            return  ServerResponse.createByErrorMessage("没有操作权限");
        }
    }

    /**
     * 递归查询本节点的id和子节点的id
     * @param categoryId 当前节点的id
     * @return
     */
    @RequestMapping("get_deep_category.do")
    @ResponseBody
    public ServerResponse getCategoryAndDeepChildrenCategory(HttpSession session ,@RequestParam(value = "categoryId" ,defaultValue ="0") Integer categoryId ){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode()
                    ,"用户未登录，请登陆");
        }
        if (mIUserService.checkAdminRole(user).isSuccess()){//是管理员
            //查询当前子节点的id和递归子节点的id
            return  mICategoryService.selectCategoryAndChildrenById(categoryId);

        }else {
            return  ServerResponse.createByErrorMessage("没有操作权限");
        }
    }

}
