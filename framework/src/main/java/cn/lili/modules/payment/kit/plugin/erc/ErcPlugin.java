package cn.lili.modules.payment.kit.plugin.erc;

import cn.hutool.json.JSONUtil;
import cn.lili.common.enums.ResultCode;
import cn.lili.common.enums.ResultUtil;
import cn.lili.common.exception.ServiceException;
import cn.lili.common.vo.ResultMessage;
import cn.lili.modules.member.entity.enums.CoinTypeEnum;
import cn.lili.modules.payment.entity.enums.PaymentMethodEnum;
import cn.lili.modules.payment.kit.CashierSupport;
import cn.lili.modules.payment.kit.Payment;
import cn.lili.modules.payment.kit.dto.PayParam;
import cn.lili.modules.payment.kit.dto.PaymentSuccessParams;
import cn.lili.modules.payment.kit.params.dto.CashierParam;
import cn.lili.modules.payment.service.PaymentService;
import cn.lili.modules.payment.service.RefundLogService;
import cn.lili.modules.system.entity.dos.Coin;
import cn.lili.modules.system.entity.dos.Setting;
import cn.lili.modules.system.entity.dto.payment.ErcPaymentSetting;
import cn.lili.modules.system.entity.enums.SettingEnum;
import cn.lili.modules.system.service.SettingService;
import cn.lili.modules.system.serviceimpl.CoinServiceImpl;
import cn.lili.modules.wallet.service.MemberWalletService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
public class ErcPlugin implements Payment {
    /**
     * 支付日志
     */
    @Autowired
    private PaymentService paymentService;
    /**
     * 退款日志
     */
    @Autowired
    private RefundLogService refundLogService;
    /**
     * 会员余额
     */
    @Autowired
    private MemberWalletService memberWalletService;
    /**
     * 收银台
     */
    @Autowired
    private CashierSupport cashierSupport;
    /**
     * 设置
     */
    @Autowired
    private SettingService settingService;

    @Autowired
    private CoinServiceImpl coinService;

    @Override
    public ResultMessage<Object> nativePay(HttpServletRequest request, PayParam payParam) {

        //获取支付收银参数
        CashierParam cashierParam = cashierSupport.cashierParam(payParam);

        // 获取当前时间
        LocalDateTime now = LocalDateTime.now();
        // 查找在最近半小时内价格相同的代币
        List<Coin> coins = coinService.findCoinsWithSamePriceInTimeRange(CoinTypeEnum.ERC.name(), now.minusMinutes(30), now, cashierParam.getPrice());
        if (!coins.isEmpty()) {
            log.error("金额订单已存在");
            throw new ServiceException(ResultCode.PAY_ERROR);
        }
        ErcPaymentSetting ercpayPaymentSetting = alipayPaymentSetting();
        // 如果不存在，则创建新记录
        Coin newCoin = new Coin();
        newCoin.setCoinType(CoinTypeEnum.ERC.name());
        newCoin.setCoinAddress(ercpayPaymentSetting.getAggregateAddress()); // 这里需要设置实际的地址
        newCoin.setOrderType(payParam.getOrderType()); // 这里需要设置实际的订单类型
        newCoin.setClientType(payParam.getClientType());
        newCoin.setOrderSn(payParam.getSn()); // 这里需要设置实际的订单编号
        newCoin.setOrderPrice(cashierParam.getPrice());
        coinService.save(newCoin);
        coins.add(newCoin);
        return ResultUtil.data(ercpayPaymentSetting.getAggregateAddress());
    }

    /**
     * 回调
     *
     * @param payParam PayParam
     */
    public void callBack(PayParam payParam, String receivableNo) {
        //获取支付收银参数
        CashierParam cashierParam = cashierSupport.cashierParam(payParam);
        try {
            PaymentSuccessParams paymentSuccessParams = new PaymentSuccessParams(
                    PaymentMethodEnum.TRC.name(),
                    "",
                    cashierParam.getPrice(),
                    payParam
            );

            paymentService.success(paymentSuccessParams);
            log.info("支付回调通知：余额支付：{}", payParam);
        } catch (ServiceException e) {
            //业务异常，则支付手动回滚
            throw e;
        }
    }

    /**
     * 获取支付宝配置
     *
     * @return
     */
    private ErcPaymentSetting alipayPaymentSetting() {
        Setting setting = settingService.get(SettingEnum.ERC_PAYMENT.name());
        if (setting != null) {
            return JSONUtil.toBean(setting.getSettingValue(), ErcPaymentSetting.class);
        }
        throw new ServiceException(ResultCode.ALIPAY_NOT_SETTING);
    }


}
