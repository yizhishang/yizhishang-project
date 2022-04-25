package com.yizhishang.common.pdf.read;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 铁路大票
 *
 * @author 01387019
 * @date 2021/1/19 16:53
 */
@Data
@ApiModel("铁路大票")
public class SettlementRailwayBill {

    @ApiModelProperty("基本信息")
    @JSONField(name = "基本信息")
    private BaseInfo baseInfo;

    @ApiModelProperty("托运人和收货人信息")
    @JSONField(name = "托运人和收货人信息")
    private List<UserInfo> userInfoList;

    @ApiModelProperty("货品信息")
    @JSONField(name = "货品信息")
    private List<CargoInfo> cargoInfoList;

    @ApiModelProperty("费用信息")
    @JSONField(name = "费用信息")
    private List<CostInfo> costInfoList;

    @Data
    @ApiModel("基础信息")
    @TableName("settlement_railway_bill_base")
    @EqualsAndHashCode()
    public static class BaseInfo {

        private static final long serialVersionUID = -6633745133478629764L;

        @TableId(value = "id", type = IdType.ASSIGN_ID)
        @TableField("id")
        @ApiModelProperty(value = "主键id")
        @JsonSerialize(using = ToStringSerializer.class)
        @JSONField(serialize = false)
        private Long id;

        @TableField("md5_hash_value")
        @ApiModelProperty(value = "文件MD5加密得到的哈希值，作为图片文件的唯一键")
        private String md5HashValue;

        @TableField("doc_code")
        @ApiModelProperty(value = "单证编码（系统生成）")
        @Length(max = 64, message = "单证编码长度不能超过64个字符")
        @JSONField(serialize = false)
        private String docCode;

        @TableField("code")
        @ApiModelProperty(value = "运单号")
        @Length(max = 64, message = "运单号长度不能超过64个字符")
        @JSONField(name = "运单号")
        private String code;

        @TableField("demand_number")
        @ApiModelProperty(value = "需求号")
        @Length(max = 64, message = "需求号长度不能超过64个字符")
        @JSONField(name = "需求号")
        private String demandNumber;

        @TableField("transport_type")
        @ApiModelProperty(value = "运输类型")
        @Length(max = 64, message = "运输类型长度不能超过64个字符")
        @JSONField(name = "运输类型")
        private String transportType;

        @TableField("cargo_area")
        @ApiModelProperty(value = "货区")
        @Length(max = 64, message = "货区长度不能超过64个字符")
        @JSONField(name = "货区")
        private String cargoArea;

        @TableField("cargo_location")
        @ApiModelProperty(value = "货位")
        @Length(max = 64, message = "货位长度不能超过64个字符")
        @JSONField(name = "货位")
        private String cargoLocation;

        @TableField("car_size_and_number")
        @ApiModelProperty(value = "车种车号")
        @Length(max = 64, message = "车种车号长度不能超过64个字符")
        @JSONField(name = "车种车号")
        private String carSizeAndNumber;

        @TableField("transportation_term")
        @ApiModelProperty(value = "运到期限")
        @JSONField(name = "运到期限")
        private Integer transportationTerm;

        @TableField("mark_weight")
        @ApiModelProperty(value = "标重")
        @JSONField(name = "标重")
        private BigDecimal markWeight;

        @TableField("seal_number")
        @ApiModelProperty(value = "施封号")
        @Length(max = 64, message = "施封号长度不能超过64个字符")
        @JSONField(name = "施封号")
        private String sealNumber;

        @TableField("tarpaulin_number")
        @ApiModelProperty(value = "篷布号")
        @Length(max = 64, message = "篷布号长度不能超过64个字符")
        @JSONField(name = "篷布号")
        private String tarpaulinNumber;

        @TableField("payment_way")
        @ApiModelProperty(value = "付费方式")
        @Length(max = 64, message = "付费方式长度不能超过64个字符")
        @JSONField(name = "付费方式")
        private String paymentWay;

        @TableField("receive_cargo_way")
        @ApiModelProperty(value = "领货方式")
        @Length(max = 64, message = "领货方式长度不能超过64个字符")
        @JSONField(name = "领货方式")
        private String receiveCargoWay;

        @TableField("loader")
        @ApiModelProperty(value = "装车方")
        @Length(max = 64, message = "装车方长度不能超过64个字符")
        @JSONField(name = "装车方")
        private String loader;

        @TableField("sealer")
        @ApiModelProperty(value = "施封方")
        @Length(max = 64, message = "施封方长度不能超过64个字符")
        @JSONField(name = "施封方")
        private String sealer;

        @TableField("cargo_quantity")
        @ApiModelProperty(value = "件数合计")
        @JSONField(name = "件数合计")
        private Long cargoQuantity;

        @TableField("cargo_price")
        @ApiModelProperty(value = "货物价格合计（元）")
        @JSONField(name = "货物价格合计")
        private BigDecimal cargoPrice;

        @TableField("cargo_weight")
        @ApiModelProperty(value = "重量合计（kg）")
        @JSONField(name = "重量合计")
        private BigDecimal cargoWeight;

        @TableField("cargo_determined_weight")
        @ApiModelProperty(value = "承运人确定重量合计（kg）")
        @JSONField(name = "承运人确定重量合计")
        private BigDecimal cargoDeterminedWeight;

        @TableField("cargo_volume")
        @ApiModelProperty(value = "体积合计")
        @JSONField(name = "体积合计")
        private BigDecimal cargoVolume;

        @TableField("cargo_transportation_cost_number")
        @ApiModelProperty(value = "运价号合计")
        @Length(max = 64, message = "运价号合计长度不能超过64个字符")
        @JSONField(name = "运价号合计")
        private String cargoTransportationCostNumber;

        @TableField("cargo_charge_weight")
        @ApiModelProperty(value = "计费重量合计（kg）")
        @JSONField(name = "计费重量合计")
        private BigDecimal cargoChargeWeight;

        @TableField("optional_service")
        @ApiModelProperty(value = "选择服务")
        @Length(max = 64, message = "选择服务长度不能超过64个字符")
        @JSONField(name = "选择服务")
        private String optionalService;

        @TableField("other_service")
        @ApiModelProperty(value = "其他服务")
        @Length(max = 64, message = "其他服务长度不能超过64个字符")
        @JSONField(name = "其他服务")
        private String otherService;

        @TableField("vat_invoice_type")
        @ApiModelProperty(value = "增值税发票类型")
        @Length(max = 64, message = "增值税发票类型长度不能超过64个字符")
        @JSONField(name = "增值税发票类型")
        private String vatInvoiceType;

        @TableField("bill_receiver_name")
        @ApiModelProperty(value = "受票方名称")
        @Length(max = 64, message = "受票方名称长度不能超过512个字符")
        @JSONField(name = "受票方名称")
        private String billReceiverName;

        @TableField("bill_receiver_id")
        @ApiModelProperty(value = "受票方识别号")
        @Length(max = 64, message = "受票方识别号长度不能超过512个字符")
        @JSONField(name = "受票方识别号")
        private String billReceiverId;

        @TableField("bill_receiver_address_phone")
        @ApiModelProperty(value = "受票方地址电话")
        @Length(max = 128, message = "受票方地址电话长度不能超过512个字符")
        @JSONField(name = "受票方地址电话")
        private String billReceiverAddressPhone;

        @TableField("bill_receiver_bank_account")
        @ApiModelProperty(value = "受票方开户行及账号")
        @Length(max = 128, message = "受票方开户行及账号长度不能超过512个字符")
        @JSONField(name = "受票方开户行及账号")
        private String billReceiverBankAccount;

        @TableField("total_cost")
        @ApiModelProperty(value = "费用合计")
        @JSONField(name = "费用合计")
        private BigDecimal totalCost;

        @TableField("total_cost_cn")
        @ApiModelProperty(value = "费用合计大写")
        @Length(max = 64, message = "费用合计大写长度不能超过64个字符")
        @JSONField(name = "费用合计大写")
        private String totalCostCn;

        @TableField("shipper_remark")
        @ApiModelProperty(value = "托运人记事")
        @Length(max = 512, message = "托运人记事长度不能超过512个字符")
        @JSONField(name = "托运人记事")
        private String shipperRemark;

        @TableField("carrier_remark")
        @ApiModelProperty(value = "承运人记事")
        @Length(max = 512, message = "承运人记事长度不能超过512个字符")
        @JSONField(name = "承运人记事")
        private String carrierRemark;

        @TableField("bill_created_date")
        @ApiModelProperty(value = "制单日期")
        @Length(max = 64, message = "制单日期长度不能超过64个字符")
        @JSONField(name = "制单日期")
        private Date billCreatedDate;

        @TableField("url")
        @ApiModelProperty(value = "上传文件的url")
        @Length(max = 256, message = "上传文件的url长度不能超过256个字符")
        @JSONField(serialize = false)
        private String url;

        @TableField("json")
        @ApiModelProperty("识别结果json")
        private String json;
    }

    @Data
    @ApiModel("发运人/收货人信息")
    @TableName("settlement_railway_bill_user")
    @EqualsAndHashCode()
    public static class UserInfo {

        private static final long serialVersionUID = -326074380723906880L;

        @TableId(value = "id", type = IdType.ASSIGN_ID)
        @TableField("id")
        @ApiModelProperty(value = "主键id")
        @JsonSerialize(using = ToStringSerializer.class)
        @JSONField(serialize = false)
        private Long id;

        @TableField("md5_hash_value")
        @ApiModelProperty(value = "文件MD5加密得到的哈希值，可以作为图片文件的唯一键")
        private String md5HashValue;

        @TableField("waybill_code")
        @ApiModelProperty(value = "运单号")
        @Length(max = 64, message = "运单号长度不能超过64个字符")
        @JSONField(name = "运单号")
        private String waybillCode;

        @TableField("type")
        @ApiModelProperty(value = "类型")
        @JSONField(serialize = false)
        private Integer type;

        @TableField(exist = false)
        @ApiModelProperty(value = "类型")
        @JSONField(name = "类型")
        private String typeStr;

        @TableField("node")
        @ApiModelProperty(value = "发站（公司）/到站（公司）")
        @Length(max = 64, message = "发站（公司）/到站（公司）长度不能超过64个字符")
        @JSONField(name = "发站（公司）/到站（公司）")
        private String node;

        @TableField("special_line")
        @ApiModelProperty(value = "专用线")
        @Length(max = 64, message = "专用线长度不能超过64个字符")
        @JSONField(name = "专用线")
        private String specialLine;

        @TableField("name")
        @ApiModelProperty(value = "名称")
        @Length(max = 64, message = "名称长度不能超过64个字符")
        @JSONField(name = "名称")
        private String name;

        @TableField("agent")
        @ApiModelProperty(value = "经办人")
        @Length(max = 64, message = "经办人长度不能超过64个字符")
        @JSONField(name = "经办人")
        private String agent;

        @TableField("phone_number")
        @ApiModelProperty(value = "手机号码")
        @Length(max = 64, message = "手机号码长度不能超过64个字符")
        @JSONField(name = "手机号码")
        private String phoneNumber;

        @TableField("door_to_door")
        @ApiModelProperty(value = "上门取货/上门送货")
        @Length(max = 64, message = "上门取货/上门送货长度不能超过64个字符")
        @JSONField(name = "上门取货/上门送货")
        private String doorToDoor;

        @TableField("address")
        @ApiModelProperty(value = "取货地址/送货地址")
        @Length(max = 64, message = "取货地址/送货地址长度不能超过64个字符")
        @JSONField(name = "取货地址/送货地址")
        private String address;

        @TableField("concat_number")
        @ApiModelProperty(value = "联系电话")
        @Length(max = 64, message = "联系电话长度不能超过64个字符")
        @JSONField(name = "联系电话")
        private String concatNumber;

        @TableField("mileage")
        @ApiModelProperty(value = "取货里程（km）/送货里程（km）")
        @JSONField(name = "取货里程（km）/送货里程（km）")
        private BigDecimal mileage;
    }


    @Data
    @ApiModel("货品信息")
    @TableName("settlement_railway_bill_cargo")
    public static class CargoInfo {

        private static final long serialVersionUID = 3083101215306524943L;

        @TableId(value = "id", type = IdType.ASSIGN_ID)
        @TableField("id")
        @ApiModelProperty(value = "主键id")
        @JsonSerialize(using = ToStringSerializer.class)
        @JSONField(serialize = false)
        private Long id;

        @TableField("md5_hash_value")
        @ApiModelProperty(value = "文件MD5加密得到的哈希值，可以作为图片文件的唯一键")
        private String md5HashValue;

        @TableField("waybill_code")
        @ApiModelProperty(value = "运单号")
        @Length(max = 64, message = "运单号长度不能超过64个字符")
        @JSONField(name = "运单号")
        private String waybillCode;

        @TableField("name")
        @ApiModelProperty(value = "货物")
        @Length(max = 64, message = "货物长度不能超过64个字符")
        @JSONField(name = "货物")
        private String name;

        @TableField("quantity")
        @ApiModelProperty(value = "件数")
        @JSONField(name = "件数")
        private Long quantity;

        @TableField("packaging")
        @ApiModelProperty(value = "包装")
        @Length(max = 64, message = "包装长度不能超过64个字符")
        @JSONField(name = "包装")
        private String packaging;

        @TableField("price")
        @ApiModelProperty(value = "货物价格（元）")
        @JSONField(name = "货物价格")
        private BigDecimal price;

        @TableField("weight")
        @ApiModelProperty(value = "重量（kg）")
        @JSONField(name = "重量")
        private BigDecimal weight;

        @TableField("box_size_and_type")
        @ApiModelProperty(value = "箱型箱类")
        @Length(max = 64, message = "箱型箱类长度不能超过64个字符")
        @JSONField(name = "箱型箱类")
        private String boxSizeAndType;

        @TableField("box_number")
        @ApiModelProperty(value = "箱号")
        @Length(max = 64, message = "箱号长度不能超过64个字符")
        @JSONField(name = "箱号")
        private String boxNumber;

        @TableField("box_seal_number")
        @ApiModelProperty(value = "集装箱施封号")
        @Length(max = 64, message = "集装箱施封号长度不能超过64个字符")
        @JSONField(name = "集装箱施封号")
        private String boxSealNumber;

        @TableField("determined_weight")
        @ApiModelProperty(value = "承运人确定重量（kg）")
        @JSONField(name = "承运人确定重量")
        private BigDecimal determinedWeight;

        @TableField("volume")
        @ApiModelProperty(value = "体积")
        @JSONField(name = "体积")
        private BigDecimal volume;

        @TableField("transportation_cost_number")
        @ApiModelProperty(value = "运价号")
        @Length(max = 64, message = "运价号长度不能超过64个字符")
        @JSONField(name = "运价号")
        private String transportationCostNumber;

        @TableField("charge_weight")
        @ApiModelProperty(value = "计费重量（kg）")
        @JSONField(name = "计费重量")
        private BigDecimal chargeWeight;
    }

    @Data
    @ApiModel("费用信息")
    @TableName("settlement_railway_bill_cost")
    @EqualsAndHashCode()
    public static class CostInfo {

        private static final long serialVersionUID = -2101674993790020783L;

        @TableId(value = "id", type = IdType.ASSIGN_ID)
        @TableField("id")
        @ApiModelProperty(value = "主键id")
        @JsonSerialize(using = ToStringSerializer.class)
        @JSONField(serialize = false)
        private Long id;

        @TableField("md5_hash_value")
        @ApiModelProperty(value = "文件MD5加密得到的哈希值，可以作为图片文件的唯一键")
        private String md5HashValue;

        @TableField("waybill_code")
        @ApiModelProperty(value = "运单号")
        @Length(max = 64, message = "运单号长度不能超过64个字符")
        @JSONField(name = "运单号")
        private String waybillCode;

        @TableField("cost_item")
        @ApiModelProperty(value = "费目")
        @Length(max = 64, message = "费目长度不能超过64个字符")
        @JSONField(name = "费目")
        private String costItem;

        @TableField("price")
        @ApiModelProperty(value = "金额（元）")
        @JSONField(name = "金额")
        private BigDecimal price;

        @TableField("tax_amount")
        @ApiModelProperty(value = "税额（元）")
        @JSONField(name = "税额")
        private BigDecimal taxAmount;
    }
}
