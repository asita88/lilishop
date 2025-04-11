package cn.lili.modules.system.serviceimpl;

import cn.lili.modules.system.entity.dos.Coin;
import cn.lili.modules.system.entity.dos.Region;
import cn.lili.modules.system.mapper.CoinMapper;
import cn.lili.modules.system.mapper.RegionMapper;
import cn.lili.modules.system.service.CoinService;
import cn.lili.modules.system.service.RegionService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CoinServiceImpl extends ServiceImpl<CoinMapper, Coin> implements CoinService {
    @Override
    public List<Coin> findCoinsWithSamePriceInTimeRange(String coinType, LocalDateTime startTime, LocalDateTime endTime, Double price) {
        return this.lambdaQuery()
                .ge(Coin::getCreateTime, startTime)
                .le(Coin::getCreateTime, endTime)
                .eq(Coin::getOrderPrice, price)
                .eq(Coin::getCoinType, coinType)
                .list();
    }

    @Override
    public Coin findByTxId(String coinType, String txId)
    {
        QueryWrapper<Coin> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("tx_id", txId);
        queryWrapper.eq("coin_type", coinType);
        return this.baseMapper.selectOne(queryWrapper);
    }
}
