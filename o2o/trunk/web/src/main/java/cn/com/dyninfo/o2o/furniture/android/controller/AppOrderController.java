
package cn.com.dyninfo.o2o.furniture.android.controller;

import cn.com.dyninfo.o2o.communication.*;
import cn.com.dyninfo.o2o.entity.Area;
import cn.com.dyninfo.o2o.entity.CartItem;
import cn.com.dyninfo.o2o.entity.Coupon;
import cn.com.dyninfo.o2o.entity.Order;
import cn.com.dyninfo.o2o.furniture.admin.model.CouponMemberRel;
import cn.com.dyninfo.o2o.furniture.admin.service.CouponMemberRelService;
import cn.com.dyninfo.o2o.furniture.admin.service.CouponService;
import cn.com.dyninfo.o2o.furniture.common.BaseAppController;
import cn.com.dyninfo.o2o.furniture.sys.Constants;
import cn.com.dyninfo.o2o.furniture.util.PageInfo;
import cn.com.dyninfo.o2o.furniture.util.ValidationUtil;
import cn.com.dyninfo.o2o.furniture.web.address.model.AreaInfo;
import cn.com.dyninfo.o2o.furniture.web.address.service.AreaService;
import cn.com.dyninfo.o2o.furniture.web.framework.context.Context;
import cn.com.dyninfo.o2o.furniture.web.goods.dao.GoodsDeliveryDAO;
import cn.com.dyninfo.o2o.furniture.web.member.model.AppLoginStatus;
import cn.com.dyninfo.o2o.furniture.web.member.model.HuiyuanInfo;
import cn.com.dyninfo.o2o.furniture.web.member.service.AppLoginStatusService;
import cn.com.dyninfo.o2o.furniture.web.member.service.HuiyuanService;
import cn.com.dyninfo.o2o.furniture.web.order.service.OrderService;
import cn.com.dyninfo.o2o.furniture.web.wuliu.model.Dlytype;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * Created by Administrator on 2016/7/29.
 */

@Controller
@RequestMapping("/order")
public class AppOrderController extends BaseAppController {

    @Resource
    private HuiyuanService huiyuanService;

    @Resource
    private CouponService couponService;

    @Resource
    private OrderService orderService;

    @Resource
    private AppLoginStatusService appLoginStatusService;

    @Resource
    private AreaService areaService;
    @Resource
    private CouponMemberRelService couponMemberRelService;

    @Resource
    private GoodsDeliveryDAO goodsDeliveryDAO;
/**
     * 创建订单请求类，在购物车点击去结算时使用
     * @param createOrderRequest
     * @param request
     * @param response
     * @return
     */

    @ResponseBody
    @RequestMapping("/create")
    public  CreateOrderResult create(@RequestBody  CreateOrderRequest   createOrderRequest, HttpServletRequest request, HttpServletResponse response) {
        log.debug(createOrderRequest);
        CreateOrderResult result = new  CreateOrderResult();
        if (StringUtils.isBlank(createOrderRequest.getDeviceId())) {
            result.setResultCode(NEED_DEVICE_ID);
            result.setMessage("设备识别码不能为空");
            return result;
        }
        if (StringUtils.isBlank(createOrderRequest.getToken())) {
            result.setResultCode(NO_LOGIN);
            result.setMessage("用户未登录");
            return result;
        }

        HashMap map=new HashMap();
        AppLoginStatus appLoginStatus=null;
        HuiyuanInfo info=(HuiyuanInfo)request.getSession().getAttribute(Context.SESSION_MEMBER);
        if (ValidationUtil.isEmpty(info)){
            List<AppLoginStatus> appLoginStatusList =(List<AppLoginStatus>)appLoginStatusService.getListByWhere(new StringBuffer(" and  n.token='"+ createOrderRequest.getToken()+"'"));
            if(!ValidationUtil.isEmpty(appLoginStatusList)){
                appLoginStatus=appLoginStatusList.get(0);
            }
        }
        if (!ValidationUtil.isEmpty(appLoginStatus)) {
            info = appLoginStatus.getHuiyuan();
        }
        if (!ValidationUtil.isEmpty(info)) {
            map.put("member", info);//添加会员信息

            map.put("coupons", createOrderRequest.getCoupons());//优惠卷

            map.put("addressMember", createOrderRequest.getAddressMember());//地址信息

            map.put("dlyType", createOrderRequest.getDlyType());//快递或者自提/1 物流 0上门提货

            map.put("shop", createOrderRequest.getShop());//购物车商品ＩＤ


            map.put("playType", createOrderRequest.getPlayType());//支付类型

            map.put("dlytypeId", createOrderRequest.getDlytypeId());//快递ID  现在默认2顺丰

            map.put("accountStr", createOrderRequest.getAccountStr());//是否优先使用账户支付 0 是 1否

            boolean create = orderService.createApp(map, request);//

            String tradeNo = (String) request.getSession().getAttribute(Context.SESSION_TRADENO);

            List<cn.com.dyninfo.o2o.furniture.web.order.model.Order> data = orderService.getListByPage(new StringBuffer(" and n.tradeNo='" + tradeNo + "' "));
            Double totale = 0d;
            Double orderMoney = 0d;
            Double dlyMoney = 0d;
            for (cn.com.dyninfo.o2o.furniture.web.order.model.Order order : data) {
                totale += order.getOrderPrice();
                orderMoney += order.getOrderPrice();
                dlyMoney += order.getShippingPrice();
            }

            result.setTradeNo(tradeNo);//交易号
            result.setTotalPrice(orderMoney);//商品总金额
            result.setPayPrice(orderMoney);//本次支付金额
            result.setResultCode(SUCCESS);
            result.setMessage("OK");
        }else {
            result.setResultCode(NO_LOGIN);
            result.setMessage("创建订单请求失败");
        }

        log.debug(result);
        return result;
    }

    /**
     * 支付确认成功
     * @param confirmOrderRequest
     * @param request
     * @param response
     * @return
     */

    @ResponseBody
    @RequestMapping("/confirm")
    public  ConfirmOrderResult confirm(@RequestBody  ConfirmOrderRequest   confirmOrderRequest, HttpServletRequest request, HttpServletResponse response) {
        log.debug(confirmOrderRequest);
        ConfirmOrderResult result = new  ConfirmOrderResult();
        if (StringUtils.isBlank(confirmOrderRequest.getDeviceId())) {
            result.setResultCode(NEED_DEVICE_ID);
            result.setMessage("设备识别码不能为空");
            return result;
        }
        if (StringUtils.isBlank(confirmOrderRequest.getToken())) {
            result.setResultCode(NO_LOGIN);
            result.setMessage("用户未登录");
            return result;
        }
        String payStatus =confirmOrderRequest.getPayStatus();//支付时候成功 0成功 1失败
        String tradeNo =confirmOrderRequest.getTradeNo();//交易号
        AppLoginStatus appLoginStatus=null;
        HuiyuanInfo info=(HuiyuanInfo)request.getSession().getAttribute(Context.SESSION_MEMBER);
        if (ValidationUtil.isEmpty(info)){
            List<AppLoginStatus> appLoginStatusList =(List<AppLoginStatus>)appLoginStatusService.getListByWhere(new StringBuffer(" and  n.token='"+ confirmOrderRequest.getToken()+"'"));
            if(!ValidationUtil.isEmpty(appLoginStatusList)){
                appLoginStatus=appLoginStatusList.get(0);
            }
        }
        if (!ValidationUtil.isEmpty(appLoginStatus)) {
            info = appLoginStatus.getHuiyuan();
        }
        if (!ValidationUtil.isEmpty(info)&&payStatus.equals("0")) {
            List orderlist = orderService.getListByWhere(new StringBuffer(" and n.tradeNo='" + tradeNo + "'"));
            String time=new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            int t=(int)(new Date().getTime()/1000);
            if (orderlist != null && orderlist.size() > 0) {
                cn.com.dyninfo.o2o.furniture.web.order.model.Order order = (cn.com.dyninfo.o2o.furniture.web.order.model.Order) orderlist.get(0);

                if (order.getIsPay().equals("0") && order.getStatus().equals("0")) {
                    //支付总额小于20000（系统定义）的金额
                    //状态为7-已付定金。
                    if (order.getDepositAmount() == Constants.DEPOSIT_AMOUNT) {
                        order.setState("7");
                        order.setIsPay("0");
                    } else {
                        order.setPaytime(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
                        order.setState("1");
                        order.setIsPay("1");
                    }
//                    for (OrderProduct op : order.getOrderProductList()) {
//                        Goods good = op.getProduct().getGood();
//                        good = (Goods) goodsDAO.getObjById(good.getGoods_id() + "");
//                        good.setNum(good.getNum() + op.getNum());
//                        good.setInventory(good.getInventory() - op.getNum());
//                        goodsDAO.updateObj(good);
//                    }
                    order.setPaytime(time);
                    order.setIpaytime(t);
                    orderService.updateObj(order);
                }
            }
            result.setResultCode(SUCCESS);
            result.setMessage("OK");
        }else{
            result.setResultCode(NO_LOGIN);
            result.setMessage("支付确认请求失败");
        }

        log.debug(result);
        return result;
    }



    /**
     * 订单确认请求，在购物车点击去结算时跳到确认页面
     * @param submitOrderRequest
     * @param request
     * @param response
     * @return
     */

    @ResponseBody
    @RequestMapping("/submit")
    public  SubmitOrderResult submit(@RequestBody  SubmitOrderRequest   submitOrderRequest, HttpServletRequest request, HttpServletResponse response) {
        log.debug(submitOrderRequest);
        SubmitOrderResult result = new  SubmitOrderResult();
        if (StringUtils.isBlank(submitOrderRequest.getDeviceId())) {
            result.setResultCode(NEED_DEVICE_ID);
            result.setMessage("设备识别码不能为空");
            return result;
        }
        if (StringUtils.isBlank(submitOrderRequest.getToken())) {
            result.setResultCode(NO_LOGIN);
            result.setMessage("用户未登录");
            return result;
        }
//        String status=String.valueOf(createOrderRequest.getOrderStatus());//订单状态
        AppLoginStatus appLoginStatus=null;
        HuiyuanInfo info=(HuiyuanInfo)request.getSession().getAttribute(Context.SESSION_MEMBER);
        if (ValidationUtil.isEmpty(info)){
            List<AppLoginStatus> appLoginStatusList =(List<AppLoginStatus>)appLoginStatusService.getListByWhere(new StringBuffer(" and  n.token='"+ submitOrderRequest.getToken()+"'"));
            if(!ValidationUtil.isEmpty(appLoginStatusList)){
                appLoginStatus=appLoginStatusList.get(0);
            }
        }
        if (!ValidationUtil.isEmpty(appLoginStatus)) {
            info = appLoginStatus.getHuiyuan();
        }
        List<Coupon>  lists=new ArrayList<Coupon>();

        List<cn.com.dyninfo.o2o.entity.Dlytype>  lists2=new ArrayList<cn.com.dyninfo.o2o.entity.Dlytype>();

        if (!ValidationUtil.isEmpty(info)) {
            List<CouponMemberRel> couponMemberRelList = (List<CouponMemberRel>) couponMemberRelService.getListByWhere(new StringBuffer(" and n.coupon.endTime > now() and  n.huiyuan=" + info.getHuiYuan_id() + " order by n.coupon.endTime asc"));
            if (!ValidationUtil.isEmpty(couponMemberRelList)) {
                for (int i = 0; i < couponMemberRelList.size(); i++) {
                    int count = couponMemberRelList.get(i).getCount();
                    for (int j = 0; j < count; j++) {
                        cn.com.dyninfo.o2o.furniture.admin.model.Coupon c = couponMemberRelList.get(i).getCoupon();
                        Coupon coupon = new Coupon();
                        if (String.valueOf(c.getId()) != null) {
                            coupon.setId(String.valueOf(c.getId()));
                        }
                        if (c.getName() != null) {
                            coupon.setName(c.getName());
                        }
                        if (c.getBeginTime() != null) {
                            coupon.setBeginTime(c.getBeginTime());
                        }
                        if (c.getEndTime() != null) {
                            coupon.setEndTime(c.getEndTime());
                        }
                        coupon.setType(c.getType());
                        coupon.setReduceValue(c.getReduceValue());
                        coupon.setDiscountValue(c.getDiscountValue());
                        coupon.setMaxAmount(c.getMaxAmouont());
                        coupon.setConstraintValue(c.getConstraintValue());
                        coupon.setSameUse(c.getSameUse());
                        lists.add(coupon);
                    }
                }
            }
            int aa=info.getShangJiaInfo().getShangjia_id();
            List list=orderService.getDeliveryList((String.valueOf(aa)));
            if (!ValidationUtil.isEmpty(list)) {
                for (int i = 0; i < list.size(); i++) {
                     Dlytype c = (Dlytype)list.get(i);
                      cn.com.dyninfo.o2o.entity.Dlytype  dlytype=new cn.com.dyninfo.o2o.entity.Dlytype();
                        dlytype.setId( String.valueOf(c.getDlytype_id()));
                        dlytype.setDlyName(c.getDlyname());
                        lists2.add(dlytype);
                    }
            }
            result.setCouponList(lists); //可使用的优惠券列表
            result.setDlytypeList(lists2); //可使用的快递列表
            result.setResultCode(SUCCESS);
            result.setMessage("OK");
        }else{
            result.setResultCode(NO_LOGIN);
            result.setMessage("订单确认请求失败");
        }

        log.debug(result);
        return result;
    }


    /**
     * 查询省市
     * @param queryAreaRequest
     * @param request
     * @param response
     * @return
     */

    @ResponseBody
    @RequestMapping("/queryArea")
    public  QueryAreaResult queryArea(@RequestBody QueryAreaRequest   queryAreaRequest, HttpServletRequest request, HttpServletResponse response) {
        log.debug(queryAreaRequest);
        QueryAreaResult result = new  QueryAreaResult();
        if (StringUtils.isBlank(queryAreaRequest.getDeviceId())) {
            result.setResultCode(NEED_DEVICE_ID);
            result.setMessage("设备识别码不能为空");
            return result;
        }
        List list=null;
        String parentId=queryAreaRequest.getParentId();//(父类ID
        if(parentId!=null&&parentId.length()>0){
            list=areaService.getListByWhere(new StringBuffer(" and n.parent.id='"+parentId+"' "));
        }else{
            list=areaService.getListByWhere(new StringBuffer(" and n.parent.id is null "));
        }
        List<Area> areaList=new ArrayList<Area>();
        if (!ValidationUtil.isEmpty(list)) {
            for (int i = 0; i < list.size(); i++) {
                AreaInfo a = (AreaInfo) list.get(i);
                Area area = new Area();
                area.setId(a.getId());
                area.setName(a.getName());
                areaList.add(area);
            }
        }
        result.setAreaList(areaList);
        result.setResultCode(SUCCESS);
        result.setMessage("OK");
//        result.setResultCode(NO_LOGIN);
//        result.setMessage("创建订单请求失败");
        log.debug(result);
        return result;
    }
    /**
     *
     * 查询运费接口
     * 查询运费 传入城市ID，重量，快递ID
     * @param queryYfPriceRequest
     * @param request
     * @param response
     * @return
     */

    @ResponseBody
    @RequestMapping("/queryYfPrice")
    public  QueryYfPricelResult queryYfPrice(@RequestBody QueryYfPriceRequest   queryYfPriceRequest, HttpServletRequest request, HttpServletResponse response) {
        log.debug(queryYfPriceRequest);
        QueryYfPricelResult result = new  QueryYfPricelResult();
        if (StringUtils.isBlank(queryYfPriceRequest.getDeviceId())) {
            result.setResultCode(NEED_DEVICE_ID);
            result.setMessage("设备识别码不能为空");
            return result;
        }
        if (StringUtils.isBlank(queryYfPriceRequest.getToken())) {
            result.setResultCode(NO_LOGIN);
            result.setMessage("用户未登录");
            return result;
        }
        AppLoginStatus appLoginStatus=null;
        HuiyuanInfo info=(HuiyuanInfo)request.getSession().getAttribute(Context.SESSION_MEMBER);
        if (ValidationUtil.isEmpty(info)){
            List<AppLoginStatus> appLoginStatusList =(List<AppLoginStatus>)appLoginStatusService.getListByWhere(new StringBuffer(" and  n.token='"+ queryYfPriceRequest.getToken()+"'"));
            if(!ValidationUtil.isEmpty(appLoginStatusList)){
                appLoginStatus=appLoginStatusList.get(0);
            }
        }
        if (!ValidationUtil.isEmpty(appLoginStatus)) {
            info = appLoginStatus.getHuiyuan();
        }
        if (!ValidationUtil.isEmpty(info)) {
            Double dl = goodsDeliveryDAO.getDeliveryMoney(queryYfPriceRequest.getWeight(), queryYfPriceRequest.getCountyId(), queryYfPriceRequest.getDlytypeId());//area_id区ID    快递dly_id ID   Dlytype表
            result.setYfPrice(dl);
            result.setResultCode(SUCCESS);
            result.setMessage("OK");
        }else {
            result.setResultCode(NO_LOGIN);
            result.setMessage("查询运费请求失败");
        }
        log.debug(result);
        return result;
    }

/**
     * 评价订单请求
     * @param evalOrderRequest
     * @param request
     * @param response
     * @return
     */

    @ResponseBody
    @RequestMapping("/eval")
    public  EvalOrderResult eval(@RequestBody  EvalOrderRequest   evalOrderRequest, HttpServletRequest request, HttpServletResponse response) {
        log.debug(evalOrderRequest);
        EvalOrderResult result = new  EvalOrderResult();

        evalOrderRequest.getOrderId(); //订单ID
        evalOrderRequest.getSameStar();//商品符合度星星数量从1-5
        evalOrderRequest.getSpeedStar(); //物流速度星星数量，从1-5
        evalOrderRequest.getAttitudeStar();//配送员服务态度星星数量，从1-5
        evalOrderRequest.getOrderStatus();//订单状态：-1——全部；0——待付款；1——待发货；2——待收货，根据当前所在tab页填入，用于返回刷新后的订单列表

        List<Order>  lists=new ArrayList<Order>();
        List<cn.com.dyninfo.o2o.furniture.web.order.model.Order> list =(List<cn.com.dyninfo.o2o.furniture.web.order.model.Order>)orderService.getListByWhere(new StringBuffer(""));
        if(list.size()>0) {
            for (int i = 0; i < list.size(); i++) {
                cn.com.dyninfo.o2o.furniture.web.order.model.Order c=list.get(i);
                Order order = new Order();
                if(String.valueOf(c.getOrder_id())!=null){
                    order.setId(String.valueOf(c.getOrder_id()));
                }
                if(String.valueOf(c.getCreatTime())!=null){
                    order.setDate(String.valueOf(c.getCreatTime()));//订单日期
                }
                order.setState(Integer.parseInt(c.getState())); //状态
                if(c.getReceiveName()!=null){
                    order.setReceiveName(c.getReceiveName()); //收货人姓名
                }
                order.setOrderPrice(c.getOrderPrice()); //订单金额
                order.setPayType(Integer.parseInt(c.getPayType()));//支付方式：1-支付宝；2-银联；10-线下；11-支付宝+线下；12-银联+线下
                List<CartItem> cartItemList=new ArrayList<CartItem>();
                //TODO 评价订单请求 订单商品列表
                order.setCartItemList(cartItemList);//订单商品列表
                lists.add(order);
            }
            result.setOrderList(lists);//
            result.setResultCode(SUCCESS);
            result.setMessage("OK");
        }else{
            result.setResultCode(NO_LOGIN);
            result.setMessage("评价订单请求失败");
        }
        log.debug(result);
        return result;
    }


/**
     * 订单查询请求
     * @param queryOrderRequest
     * @param request
     * @param response
     * @return
     * 0.有效 1.未付款 2.已付款3.交易成功4.无效
     */

    @ResponseBody
    @RequestMapping("/list")
    public QueryOrderResult list(@RequestBody  QueryOrderRequest   queryOrderRequest, HttpServletRequest request, HttpServletResponse response) {
        log.debug(queryOrderRequest);
        QueryOrderResult result = new  QueryOrderResult();
        if (StringUtils.isBlank(queryOrderRequest.getDeviceId())) {
            result.setResultCode(NEED_DEVICE_ID);
            result.setMessage("设备识别码不能为空");
            return result;
        }
        if (StringUtils.isBlank(queryOrderRequest.getToken())) {
            result.setResultCode(NO_LOGIN);
            result.setMessage("用户未登录");
            return result;
        }
        String status=String.valueOf(queryOrderRequest.getOrderStatus());//订单状态
        AppLoginStatus appLoginStatus=null;
        HuiyuanInfo info=(HuiyuanInfo)request.getSession().getAttribute(Context.SESSION_MEMBER);
        if (ValidationUtil.isEmpty(info)){
            List<AppLoginStatus> appLoginStatusList =(List<AppLoginStatus>)appLoginStatusService.getListByWhere(new StringBuffer(" and  n.token='"+ queryOrderRequest.getToken()+"'"));
            if(!ValidationUtil.isEmpty(appLoginStatusList)){
                appLoginStatus=appLoginStatusList.get(0);
            }
        }
        if (!ValidationUtil.isEmpty(appLoginStatus)) {
            info = appLoginStatus.getHuiyuan();
        }
        PageInfo pageInfo=new PageInfo();
        pageInfo.setPageNo(queryOrderRequest.getPageNo());
        pageInfo.setPageSize(queryOrderRequest.getPageSize());
        StringBuffer where =new StringBuffer();//0.有效 1.未付款 2.已付款3.交易成功4.无效
        if(!ValidationUtil.isEmpty(info)) {
            where.append(" and n.huiyuan.huiYuan_id = '" + info.getHuiYuan_id() + "' ");
            if (status.equals("0")) {
                where.append(" and n.state != '6' ");
            } else if (status.equals("1")) {
                where.append(" and n.state = '0' and n.isPay='0' ");
            } else if (status.equals("2")) {
                where.append(" and n.state = '1' ");
            } else if (status.equals("3")) {
                where.append(" and n.state = '3' ");
            } else if (status.equals("4")) {
                where.append(" and n.state = '6' ");
            }
            where.append(" and n.status='0'");
            where.append(" order by n.time desc");
            List<Order> lists = new ArrayList<Order>();
            Map map =  (Map) orderService.getListByPageWhere(where, pageInfo);//查询订单
            List<cn.com.dyninfo.o2o.furniture.web.order.model.Order> list=(List<cn.com.dyninfo.o2o.furniture.web.order.model.Order>) map.get("DATA");

            if(!ValidationUtil.isEmpty(list)){
                for (int i = 0; i < list.size(); i++) {
                    Order order=new  Order();
                    order.setId(list.get(i).getOrder_id());//ID
                    order.setDate(String.valueOf(list.get(i).getCreatTime()));//创建时间
                    order.setOrderPrice(list.get(i).getOrderPrice());//总价
                    order.setState(Integer.parseInt(list.get(i).getState()));//状态
                    order.setReceiveName(list.get(i).getReceiveName());//收货人
                    order.setPayType(Integer.parseInt(list.get(i).getIsPay()));//是否支付
                    lists.add(order);
                }
                int totalpage=(pageInfo.getTotalCount()+pageInfo.getPageSize()-1)/pageInfo.getPageSize();
                result.setPageNo(pageInfo.getPageNo());
                result.setTotalPage(totalpage);
                result.setOrderList(lists);
                result.setResultCode(SUCCESS);
                result.setMessage("OK");
            }
        } else{
            result.setResultCode(NO_LOGIN);
            result.setMessage("订单查询请求失败");
        }
        log.debug(result);
        return result;
    }

    /**
     *修改订单请求
     * @param updateOrderRequest
     * @param request
     * @param response
     * @return
     */

    @ResponseBody
    @RequestMapping("/update")
    public UpdateOrderResult update(@RequestBody UpdateOrderRequest updateOrderRequest, HttpServletRequest request, HttpServletResponse response) {
        log.debug(updateOrderRequest);
        UpdateOrderResult result = new UpdateOrderResult();
        if (StringUtils.isBlank(updateOrderRequest.getDeviceId())) {
            result.setResultCode(NEED_DEVICE_ID);
            result.setMessage("设备识别码不能为空");
            return result;
        }
        if (StringUtils.isBlank(updateOrderRequest.getToken())) {
            result.setResultCode(NO_LOGIN);
            result.setMessage("用户未登录");
            return result;
        }
        int orderStatus = updateOrderRequest.getOrderStatus();//订单状态
        String orderId = updateOrderRequest.getOrderId();//订单ID
        //获取用户信息
        AppLoginStatus appLoginStatus=null;
        HuiyuanInfo info=(HuiyuanInfo)request.getSession().getAttribute(Context.SESSION_MEMBER);
        if (ValidationUtil.isEmpty(info)){
            List<AppLoginStatus> appLoginStatusList =(List<AppLoginStatus>)appLoginStatusService.getListByWhere(new StringBuffer(" and  n.token='"+ updateOrderRequest.getToken()+"'"));
            if(!ValidationUtil.isEmpty(appLoginStatusList)){
                appLoginStatus=appLoginStatusList.get(0);
            }
        }
        if (!ValidationUtil.isEmpty(appLoginStatus)) {
            info = appLoginStatus.getHuiyuan();
        }
        if(!ValidationUtil.isEmpty(info)) {
                List orderlist=orderService.getListByWhere(new StringBuffer(" and n.order_id='"+orderId+"'"));
                if(orderlist!=null&&orderlist.size()>0){
                    cn.com.dyninfo.o2o.furniture.web.order.model.Order order=(cn.com.dyninfo.o2o.furniture.web.order.model.Order) orderlist.get(0);
                    //支付总额小于20000（系统定义）的金额
                    //状态为7-已付定金。
                    order.setState(String.valueOf(orderStatus));
                    orderService.updateObj(order);
                }
            result.setResultCode(SUCCESS);
            result.setMessage("OK");
        } else {
            result.setResultCode(NO_LOGIN);
            result.setMessage("修改订单请求失败");
        }
        log.debug(result);
        return result;
    }
}

