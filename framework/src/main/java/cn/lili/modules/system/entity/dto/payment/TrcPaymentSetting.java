package cn.lili.modules.system.entity.dto.payment;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 支付宝支付设置
 *
 * @author Chopper
 * @since 2020-12-02 10:09
 */
@Data
@Accessors(chain = true)
public class TrcPaymentSetting {

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
