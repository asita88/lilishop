package cn.lili.modules.system.service;

import cn.lili.modules.system.entity.dos.AppVersion;
import cn.lili.modules.system.entity.dos.Coin;
import com.baomidou.mybatisplus.extension.service.IService;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public interface CoinService extends IService<Coin> {

    List<Coin> findCoinsWithSamePriceInTimeRange(String coinType, LocalDateTime startTime, LocalDateTime endTime, Double price);

    /**
     * 会员积分历史
     *
     * @param txId       分页
     * @return 积分历史分页
     */
    Coin findByTxId(String coinType, String txId);
}
