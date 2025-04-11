package cn.lili.timetask.handler.impl.coin;

import cn.hutool.json.JSONUtil;
import cn.lili.common.enums.ResultCode;
import cn.lili.common.exception.ServiceException;
import cn.lili.modules.member.entity.enums.CoinTypeEnum;
import cn.lili.modules.system.entity.dos.Coin;
import cn.lili.modules.system.entity.dos.Setting;
import cn.lili.modules.system.entity.dto.payment.ErcPaymentSetting;
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
public class ErcCoinExecute implements EveryMinuteExecute {

    @Autowired
    private CoinServiceImpl coinService;
    /**
     * 设置
     */
    @Autowired
    private SettingService settingService;


    private static final String ERC_API_URL = "https://api.etherscan.io/api?module=account&action=txlist&address=%s&startblock=0&endblock=99999999&sort=desc&apikey=YourApiKeyToken";

    @Override
    public void execute() {
        ErcPaymentSetting ercPaymentSetting = alipayPaymentSetting();
        log.info("Coin Address: {}", ercPaymentSetting.getAggregateAddress());
        // 查询当前地址最近的20笔交易记录
        List<ErcCoinExecute.Transaction> transactions = getRecentTransactions(ercPaymentSetting.getAggregateAddress());
        for (ErcCoinExecute.Transaction transaction : transactions) {
            log.info("Transaction: {}", transaction);

            Coin coin = coinService.findByTxId(CoinTypeEnum.ERC.name(), transaction.getHash());
            if (coin != null) {
                continue;
            }
            // 获取当前时间
            LocalDateTime now = LocalDateTime.now();
            // 查找在最近半小时内价格相同的代币
            List<Coin> coins = coinService.findCoinsWithSamePriceInTimeRange(CoinTypeEnum.ERC.name(), now.minusMinutes(30), now, (double) transaction.getValue());
            if (!coins.isEmpty()) {
                continue;
            }
            coin = coins.get(0);
            coin.setTxId(transaction.getHash());
            coinService.updateById(coin);
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

    private List<Transaction> getRecentTransactions(String address) {
        RestTemplate restTemplate = new RestTemplate();
        String url = String.format(ERC_API_URL, address);
        TransactionResponse response = restTemplate.getForObject(url, TransactionResponse.class);
        return response != null ? response.getResult() : null;
    }

    // 定义 Transaction 和 TransactionResponse 类
    @lombok.Data
    public static class Transaction {
        private String blockNumber;
        private long timeStamp;
        private String hash;
        private String from;
        private String to;
        private long value;
        private String gas;
        private String gasPrice;
        private String isError;
        private String txreceipt_status;
        private String input;
        private String contractAddress;
        private String cumulativeGasUsed;
        private String gasUsed;
        private String confirmations;
    }

    public static class TransactionResponse {
        private String status;
        private String message;
        private List<Transaction> result;

        public List<Transaction> getResult() {
            return result;
        }

        public void setResult(List<Transaction> result) {
            this.result = result;
        }
    }
}