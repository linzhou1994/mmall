package com.mmall.controller.portal;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Shipping;
import com.mmall.pojo.User;
import com.mmall.service.IShippingService;
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
 * 包名：   com.mmall.controller.portal
 * 创建者:  linzhou
 * 创建时间:17/10/18
 * 描述:
 */
@Controller
@RequestMapping("/shipping/")
public class ShippingController {

    @Autowired
    private IShippingService mIShippingService;

    /**
     * 添加收货地址
     * @param session
     * @param shipping  收货地址详情
     * @return
     */
    @RequestMapping(value = "add.do")
    @ResponseBody
    public ServerResponse add(HttpSession session,Shipping shipping){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return mIShippingService.add(user.getId(),shipping);
    }

    /**
     * 删除收货地址
     * @param session
     * @param shippingId 要删除的收货地址id
     * @return
     */
    @RequestMapping(value = "del.do")
    @ResponseBody
    public ServerResponse<String> del(HttpSession session,Integer shippingId){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return mIShippingService.del(user.getId(),shippingId);
    }

    /**
     * 修改收货地址
     * @param session
     * @param shipping  要修改成的收货地址id
     * @return
     */
    @RequestMapping(value = "update.do")
    @ResponseBody
    public ServerResponse<String> update(HttpSession session,Shipping shipping){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return mIShippingService.update(user.getId(),shipping);
    }

    /**
     * 查询收货地址
     * @param session
     * @param shippingId  收货地址id
     * @return
     */
    @RequestMapping(value = "select.do")
    @ResponseBody
    public ServerResponse select(HttpSession session,Integer shippingId){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return mIShippingService.select(user.getId(),shippingId);
    }

    /**
     * 获取收货地址列表（分页）
     * @param session
     * @param pageNum   页码（默认第一页）
     * @param pageSize  每页收货地址数量（默认10）
     * @return
     */
    @RequestMapping(value = "list.do")
    @ResponseBody
    public ServerResponse list(HttpSession session,
                               @RequestParam(value = "pageNum" ,defaultValue = "1") int pageNum,
                               @RequestParam(value = "pageSize",defaultValue = "10") int pageSize){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return mIShippingService.list(user.getId(),pageNum,pageSize);
    }

}
