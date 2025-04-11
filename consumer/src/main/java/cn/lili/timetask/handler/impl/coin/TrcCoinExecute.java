package cn.lili.timetask.handler.impl.coin;


import cn.hutool.json.JSONUtil;
import cn.lili.common.enums.ResultCode;
import cn.lili.common.exception.ServiceException;
import cn.lili.modules.member.entity.enums.CoinTypeEnum;
import cn.lili.modules.payment.kit.dto.PayParam;
import cn.lili.modules.payment.kit.plugin.trc.TrcPlugin;
import cn.lili.modules.system.entity.dos.Coin;
import cn.lili.modules.system.entity.dos.Setting;
import cn.lili.modules.system.entity.dto.payment.TrcPaymentSetting;
import cn.lili.modules.system.entity.enums.SettingEnum;
import cn.lili.modules.system.service.SettingService;
import cn.lili.modules.system.serviceimpl.CoinServiceImpl;
import cn.lili.timetask.handler.EveryMinuteExecute;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
public class TrcCoinExecute implements EveryMinuteExecute {

    @Autowired
    private CoinServiceImpl coinService;
    /**
     * 设置
     */
    @Autowired
    private SettingService settingService;

    @Autowired
    private TrcPlugin trcPlugin;

    private int currentPage = 1;
    private static final int PAGE_SIZE = 100;
    private static final String TRC_API_URL = "https://api.trongrid.io/v1/accounts/%s/transactions?limit=20";


    @Override
    public void execute() {
        TrcPaymentSetting trcPaymentSetting = alipayPaymentSetting();
        log.info("Coin Address: {}", trcPaymentSetting.getAggregateAddress());
        // 查询当前地址最近的20笔交易记录
        List<Transaction> transactions = getRecentTransactions(trcPaymentSetting.getAggregateAddress());
        for (Transaction transaction : transactions) {
            log.info("Transaction: {}", transaction);
            Coin coin = coinService.findByTxId(CoinTypeEnum.ERC.name(), transaction.getTxID());
            if (coin != null) {
                continue;
            }
            // 获取当前时间
            LocalDateTime now = LocalDateTime.now();
            // 查找在最近半小时内价格相同的代币
            List<Coin> coins = coinService.findCoinsWithSamePriceInTimeRange(CoinTypeEnum.ERC.name(), now.minusMinutes(30), now, (double) 11.0);
            if (coins.isEmpty()) {
                return;
            }
            Coin temp = coins.get(0);
            temp.setTxId(transaction.getTxID());
            coinService.updateById(temp);

            //
            PayParam payParam = new PayParam();
            payParam.setOrderType(temp.getOrderType());
            payParam.setSn(temp.getOrderSn());
            payParam.setClientType(temp.getClientType());
            //获取支付收银参数
            trcPlugin.callBack(payParam, transaction.getTxID());
        }
    }

    /**
     * 获取支付宝配置
     *
     * @return
     */
    private TrcPaymentSetting alipayPaymentSetting() {
        Setting setting = settingService.get(SettingEnum.TRC_PAYMENT.name());
        if (setting != null) {
            return JSONUtil.toBean(setting.getSettingValue(), TrcPaymentSetting.class);
        }
        throw new ServiceException(ResultCode.ALIPAY_NOT_SETTING);
    }

    private List<Transaction> getRecentTransactions(String address) {
        RestTemplate restTemplate = new RestTemplate();
        String url = String.format(TRC_API_URL, address);
        TransactionResponse response = restTemplate.getForObject(url, TransactionResponse.class);
        return response != null ? response.getData() : null;
    }

    // 定义 Transaction 和 TransactionResponse 类
    @lombok.Data
    public static class Transaction {
        // 根据 TRC 公链 API 的返回结果定义字段
        private String txID;
        private long block_timestamp;
        private String from;
        private String to;
        private long amount;
        private String tokenName;
    }


    public static class TransactionResponse {
        private List<Transaction> data;

        public List<Transaction> getData() {
            return data;
        }

        public void setData(List<Transaction> data) {
            this.data = data;
        }
    }
}
