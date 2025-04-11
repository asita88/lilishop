package cn.lili.modules.member.entity.vo;

import io.swagger.annotations.ApiModelProperty;

public class MemberCoinVO {
    /**
     * @see cn.lili.modules.member.entity.enums.CoinTypeEnum
     */
    @ApiModelProperty(value = "代币类型")
    private String coinType;

    @ApiModelProperty(value = "代币地址")
    private String coinAddress;
}
