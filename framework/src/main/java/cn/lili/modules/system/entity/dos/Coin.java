package cn.lili.modules.system.entity.dos;

import cn.lili.mybatis.BaseEntity;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@TableName("li_coin")
@ApiModel(value = "会员预存款")
public class Coin extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @CreatedBy
    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建者", hidden = true)
    private String createBy;

    @CreatedDate
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建时间", hidden = true)
    private Date createTime;

    /**
     * @see cn.lili.modules.payment.entity.enums.PaymentMethodEnum
     */
    @ApiModelProperty(value = "代币类型")
    private String coinType;

    @ApiModelProperty(value = "代币地址")
    private String coinAddress;

    @ApiModelProperty(value = "代币地址")
    private String orderType;

    @ApiModelProperty(value = "代币地址")
    private String clientType;

    @ApiModelProperty(value = "代币地址")
    private String orderSn;

    @ApiModelProperty(value = "代币地址")
    private Double orderPrice;

    @ApiModelProperty(value = "代币地址")
    private String txId;
}