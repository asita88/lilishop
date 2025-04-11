package cn.lili.modules.member.entity.enums;

public enum CoinTypeEnum {
    /**
     * 申请中
     */
    TRC("TRC"),
    /**
     * 审核成功即提现成功
     */
    ERC("ERC");

    private String description;

    CoinTypeEnum(String description) {
        this.description = description;
    }
}
