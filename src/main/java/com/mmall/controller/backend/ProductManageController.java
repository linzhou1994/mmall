package com.mmall.controller.backend;

import com.google.common.collect.Maps;
import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Product;
import com.mmall.pojo.User;
import com.mmall.service.IFileService;
import com.mmall.service.IProductService;
import com.mmall.service.IUserService;
import com.mmall.util.PropertiesUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import sun.dc.pr.PRError;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

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
 * 创建时间:17/10/09
 * 描述:
 */
@Controller
@RequestMapping("/manage/product")
public class ProductManageController {

    @Autowired
    private IUserService mIUserService;

    @Autowired
    private IProductService mIProductService;

    @Autowired
    private IFileService mFileServiceImpl;

    /**
     * 新增商品
     *
     * @param session
     * @param product 需要新增的商品
     * @return
     */
    @RequestMapping("sava_product.do")
    @ResponseBody
    public ServerResponse productSave(HttpSession session, Product product) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请登陆");
        }
        if (mIUserService.checkAdminRole(user).isSuccess()) {
            //是管理员，执行业务
            return mIProductService.saveOrUpdateProduct(product);
        }
        return ServerResponse.createByErrorMessage("没有操作权限");
    }

    /**
     * 修改商品的销售状态
     *
     * @param session
     * @param productId 商品id
     * @param status    要修改成的销售状态码
     * @return
     */
    @RequestMapping("set_sale_status.do")
    @ResponseBody
    public ServerResponse setSaleStatus(HttpSession session, Integer productId, Integer status) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请登陆");
        }
        if (mIUserService.checkAdminRole(user).isSuccess()) {
            //是管理员，执行业务
            return mIProductService.setSaleStatus(productId, status);
        }
        return ServerResponse.createByErrorMessage("没有操作权限");
    }


    /**
     * 获取商品详情
     *
     * @param session
     * @param productId 商品id
     * @return
     */
    @RequestMapping("detail.do")
    @ResponseBody
    public ServerResponse getDetail(HttpSession session, Integer productId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请登陆");
        }
        if (mIUserService.checkAdminRole(user).isSuccess()) {
            //是管理员，执行业务
            return mIProductService.manageProductDetail(productId);
        }
        return ServerResponse.createByErrorMessage("没有操作权限");
    }


    /**
     * 商品列表（带分页）
     *
     * @param session
     * @param pageNum 当前页码，默认第一页
     * @param pageSize 每页所含商品数量，默认每页10个
     * @return
     */
    @RequestMapping("list.do")
    @ResponseBody
    public ServerResponse getList(HttpSession session
            , @RequestParam(value = "pageNum",defaultValue = "1") int pageNum
            ,@RequestParam(value = "pageSize",defaultValue = "10") int pageSize) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请登陆");
        }
        if (mIUserService.checkAdminRole(user).isSuccess()) {
            //是管理员，执行业务
            return mIProductService.getProductList(pageNum,pageSize);
        }
        return ServerResponse.createByErrorMessage("没有操作权限");
    }

    /**
     * 商品搜索列表（带分页）
     *
     * @param session
     * @param productName 商品名称（可空）
     * @param productId   商品id（可空）
     * @param pageNum     当前页码，默认第一页
     * @param pageSize    每页所含商品数量，默认每页10个
     * @return
     */
    @RequestMapping("search.do")
    @ResponseBody
    public ServerResponse productSearch(HttpSession session,String productName,Integer productId
            ,@RequestParam(value = "pageNum",defaultValue = "1") int pageNum
            ,@RequestParam(value = "pageSize",defaultValue = "10") int pageSize) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请登陆");
        }
        if (mIUserService.checkAdminRole(user).isSuccess()) {
            //是管理员，执行业务
            return mIProductService.searchProduct(productName,productId,pageNum,pageSize);
        }
        return ServerResponse.createByErrorMessage("没有操作权限");
    }

    /**
     * 上传文件
     * @param file    文件
     * @param request
     * @return
     */
    @RequestMapping("upload.do")
    @ResponseBody
    public ServerResponse upload(HttpSession session,@RequestParam(value = "upload_file",required = false) MultipartFile file, HttpServletRequest request){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请登陆");
        }
        if (mIUserService.checkAdminRole(user).isSuccess()) {
            //是管理员，执行业务
            String path = request.getSession().getServletContext().getRealPath("upload");
            String targetFilename = mFileServiceImpl.upload(file,path);
            //String url = PropertiesUtil.getProperty("ftp.server.http.prefix")+targetFilename;
            String url = "";//PropertiesUtil.getProperty("url")+targetFilename;
            Map fileMap = Maps.newHashMap();
            fileMap.put("uri",targetFilename);
            fileMap.put("url",url);

            return ServerResponse.createBySuccess(fileMap);
        }
        return ServerResponse.createByErrorMessage("没有操作权限");
    }

}
