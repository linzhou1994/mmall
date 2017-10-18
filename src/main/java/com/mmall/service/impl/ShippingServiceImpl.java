package com.mmall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.mmall.common.ServerResponse;
import com.mmall.dao.ShippingMapper;
import com.mmall.pojo.Shipping;
import com.mmall.service.IShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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
 * 包名：   com.mmall.service.impl
 * 创建者:  linzhou
 * 创建时间:17/10/18
 * 描述:
 */
@Service("mIShippingService")
public class ShippingServiceImpl implements IShippingService{

    @Autowired
    private ShippingMapper mShippingMapper;

    /**
     * 新增收货地址
     * @param userId     用户id（处理横向越权问题，用户只有新增自己的收货地址的权限，没有新增其他用户收货地址的权限）
     * @param shipping  要新增的收货地址信息
     * @return
     */
    public ServerResponse add(Integer userId,Shipping shipping){
        shipping.setUserId(userId);
        int rowCount = mShippingMapper.insert(shipping);
        if (rowCount>0){
            Map result = Maps.newHashMap();
            result.put("shippingId",shipping.getId());
            return ServerResponse.createBySuccess(result);
        }
        return ServerResponse.createByErrorMessage("新建地址失败");
    }

    /**
     * 删除收货地址
     * @param userId      用户id（处理横向越权问题，用户只有删除自己的收货地址的权限，没有删除其他用户收货地址的权限）
     * @param shippingId  要删除的收货地址id
     * @return
     */
    public ServerResponse<String> del(Integer userId,Integer shippingId){

        int rowCount = mShippingMapper.deleteByShippingIdUserId(userId,shippingId);
        if (rowCount>0){
            return ServerResponse.createByErrorMessage("删除收货地址成功");
        }
        return ServerResponse.createByErrorMessage("删除收货地址失败");
    }

    /**
     * 更新收获地址
     * @param userId     用户id（处理横向越权问题，用户只有更新自己的收货地址的权限，没有更新其他用户收货地址的权限）
     * @param shipping   要更新的收货地址信息
     * @return
     */
    public ServerResponse update(Integer userId,Shipping shipping){
        shipping.setUserId(userId);
        int rowCount = mShippingMapper.updateByShipping(shipping);
        if (rowCount>0){
            return ServerResponse.createBySuccess("更新收货地址成功");
        }
        return ServerResponse.createByErrorMessage("更新收货地址失败");
    }

    /**
     * 查询收货地址
     * @param userId      用户id
     * @param shippingId  收货地址id
     * @return
     */
    public ServerResponse<Shipping> select(Integer userId,Integer shippingId){

        Shipping shipping = mShippingMapper.selectByShippintIdUserId(userId,shippingId);
        if (shipping!=null){
            return ServerResponse.createBySuccess("查询收货地址成功",shipping);
        }
        return ServerResponse.createByErrorMessage("查询收货地址失败");
    }

    /**
     * 获取收货地址列表（分页）
     * @param userId    用户id
     * @param pageNum   页码（默认第一页）
     * @param pageSize  每页收货地址数量（默认10）
     * @return
     */
    public ServerResponse<PageInfo> list(Integer userId, int pageNum,int pageSize){
        PageHelper.startPage(pageNum, pageSize);
        List<Shipping> shippings = mShippingMapper.selectByUserId(userId);
        PageInfo pageInfo = new PageInfo(shippings);
        return ServerResponse.createBySuccess(pageInfo);

    }




}
