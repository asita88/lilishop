package cn.lili.modules.system.entity.dto.payment;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 微信支付设置
 *
 * @author Chopper
 * @since 2020-12-02 10:08
 */
@Data
@Accessors(chain = true)
public class ErcPaymentSetting {
    /**
     * 应用id
     */
    private String contractAddress;

    /**
     * 私钥
     */
    private String aggregateAddress;

    /**
     * 私钥
     */
    private String aggregatePasswd;
}
