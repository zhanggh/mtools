/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2014/8/12 星期二 下午 11:37:22                    */
/*==============================================================*/


drop table if exists glm_address;

drop table if exists glm_attrgroup_detail;

drop table if exists glm_attribute;

drop table if exists glm_attribute_group;

drop table if exists glm_attribute_value;

drop table if exists glm_bank_card;

drop table if exists glm_brand;

drop table if exists glm_brand_category;

drop table if exists glm_brand_shop;

drop table if exists glm_captcha;

drop table if exists glm_cate_attrgroup;

drop table if exists glm_category;

drop table if exists glm_category_product;

drop table if exists glm_corporate_member;

drop table if exists glm_delivery;

drop table if exists glm_delivery_rule;

drop table if exists glm_delivery_rule_region;

drop table if exists glm_dict;

drop table if exists glm_dict_value;

drop table if exists glm_file;

drop table if exists glm_file_storage;

drop table if exists glm_freight_template;

drop table if exists glm_history;

drop table if exists glm_log;

drop table if exists glm_logistics;

drop table if exists glm_member;

drop table if exists glm_member_type;

drop table if exists glm_message;

drop table if exists glm_message_reply;

drop table if exists glm_mycollect;

drop table if exists glm_order;

drop table if exists glm_order_item;

drop table if exists glm_order_log;

drop table if exists glm_order_price;

drop table if exists glm_product;

drop table if exists glm_product_attribute;

drop table if exists glm_product_inventory;

drop table if exists glm_product_sku;

drop table if exists glm_recommend;

drop table if exists glm_region;

drop table if exists glm_report;

drop table if exists glm_serial_number;

drop table if exists glm_shop;

drop table if exists glm_shop_category;

drop table if exists glm_sys_param;

drop table if exists glm_trade;

/*==============================================================*/
/* Table: glm_address                                           */
/*==============================================================*/
create table glm_address
(
   addressId            varchar(32) not null comment 'id',
   status               varchar(10) comment '字典status',
   createTime           timestamp comment '创建时间',
   updateTime           timestamp comment '最近修改时间',
   name                 varchar(32) comment '收货人/发货人',
   regionId             int comment 'xbm_region.id',
   address              varchar(200) comment '详细地址',
   postCode             varchar(10) comment '邮政编码',
   mobile               varchar(15) comment '手机号',
   phone                varchar(20) comment '固定电话',
   email                varchar(30) comment '电子邮箱',
   memberId             int comment 'xbm_memberId',
   addressType          varchar(10) comment '买家收货地址或卖家发货地址
            ',
   isDefault            varchar(10) comment '是否默认地址',
   primary key (addressId)
)
charset = UTF8;

alter table glm_address comment '地址';

/*==============================================================*/
/* Table: glm_attrgroup_detail                                  */
/*==============================================================*/
create table glm_attrgroup_detail
(
   id                   varchar(32) not null comment 'id',
   status               varchar(10) comment '字典status',
   createTime           timestamp comment '创建时间',
   updateTime           timestamp comment '最近修改时间',
   groupId              int comment 'xbm_attribute_group.id',
   attributeId          int comment 'xbm_attribute.id',
   primary key (id)
)
charset = UTF8;

alter table glm_attrgroup_detail comment '属性组与属性详情';

/*==============================================================*/
/* Table: glm_attribute                                         */
/*==============================================================*/
create table glm_attribute
(
   attributeId          varchar(32) not null comment 'id',
   status               varchar(10) comment '字典status',
   createTime           timestamp comment '创建时间',
   updateTime           timestamp comment '最近修改时间',
   name                 varchar(200) comment '属性名称',
   type                 varchar(100) comment '属性类型',
   required             varchar(20) comment '是否必填',
   search               varchar(20) comment '是否搜索',
   remark               varchar(500) comment '备注',
   shopId               int comment 'xbm_shop.id',
   alias                varchar(500) comment '别名',
   primary key (attributeId)
)
charset = UTF8;

alter table glm_attribute comment '属性';

/*==============================================================*/
/* Table: glm_attribute_group                                   */
/*==============================================================*/
create table glm_attribute_group
(
   id                   varchar(32) not null comment 'id',
   status               varchar(10) comment '字典状态',
   createTime           timestamp comment '创建时间',
   updateTime           timestamp comment '最近修改时间',
   name                 varchar(200) comment '属性组名称',
   remark               varchar(500) comment '备注',
   attributeGroupStatus varchar(10) comment '属性组状态',
   primary key (id)
)
charset = UTF8;

alter table glm_attribute_group comment '属性组';

/*==============================================================*/
/* Table: glm_attribute_value                                   */
/*==============================================================*/
create table glm_attribute_value
(
   attrValueId          varchar(32) not null comment 'id',
   status               varchar(10) comment '字典status',
   createTime           timestamp comment '创建时间',
   updateTime           timestamp comment '最近修改时间',
   attributeId          int comment 'xbm_attribute.id',
   value                varchar(200) comment '值',
   pos                  int(11) comment '排序',
   primary key (attrValueId)
)
charset = UTF8;

alter table glm_attribute_value comment '属性值';

/*==============================================================*/
/* Table: glm_bank_card                                         */
/*==============================================================*/
create table glm_bank_card
(
   bankCardId           varchar(32) not null comment 'id',
   status               varchar(10) comment '字典status',
   createTime           timestamp comment '创建时间',
   updateTime           timestamp comment '最近修改时间',
   bank                 varchar(32) comment '开户行',
   cardNumber           varchar(32) comment '账号/卡号',
   name                 varchar(50) comment '开户姓名',
   regionId             int comment 'xbm_region.id',
   address              varchar(100) comment '开户行住址',
   remark               text comment '备注',
   memberId             int comment 'xbm_member.id',
   primary key (bankCardId)
)
charset = UTF8;

alter table glm_bank_card comment '银行卡管理';

/*==============================================================*/
/* Table: glm_brand                                             */
/*==============================================================*/
create table glm_brand
(
   brandId              varchar(32) not null comment 'id',
   status               varchar(10) comment '字典status',
   createTime           timestamp comment '创建时间',
   updateTime           timestamp comment '最近修改时间',
   brandName            varchar(60) comment '品牌名称',
   ename                varchar(60) comment '英文名',
   image                varchar(100) comment '品牌图片',
   search               varchar(20) comment '是否搜索',
   remark               varchar(500) comment '备注',
   shopId               int comment 'xbm_shop.id',
   brandStatus          varchar(10) comment '品牌状态',
   primary key (brandId)
)
charset = UTF8;

alter table glm_brand comment '品牌';

/*==============================================================*/
/* Table: glm_brand_category                                    */
/*==============================================================*/
create table glm_brand_category
(
   brandCatId           varchar(32) not null comment 'id',
   status               varchar(10) comment '字典status',
   createTime           timestamp comment '创建时间',
   updateTime           timestamp comment '最近修改时间',
   brandId              int comment 'xbm_brand.id',
   categoryId           int comment 'xbm_category.id',
   primary key (brandCatId)
)
charset = UTF8;

alter table glm_brand_category comment '品牌与商品分类关系';

/*==============================================================*/
/* Table: glm_brand_shop                                        */
/*==============================================================*/
create table glm_brand_shop
(
   brandShopId          varchar(32) not null comment 'id',
   status               varchar(10) comment '字典status',
   createTime           timestamp comment '创建时间',
   updateTime           timestamp comment '最近修改时间',
   brandId              int comment 'xbm_brand.id',
   shopId               int comment 'xbm_shop.id',
   primary key (brandShopId)
)
charset = UTF8;

alter table glm_brand_shop comment '品牌与店铺关系';

/*==============================================================*/
/* Table: glm_captcha                                           */
/*==============================================================*/
create table glm_captcha
(
   captchaId            varchar(32) not null comment 'id',
   status               varchar(10) comment '状态',
   createTime           timestamp comment '创建日期',
   updateTime           timestamp comment '修改日期',
   code                 varchar(10) comment '验证码',
   source               varchar(60) comment '源',
   type                 varchar(10) comment '类型',
   primary key (captchaId)
)
charset = UTF8;

alter table glm_captcha comment '验证码表';

/*==============================================================*/
/* Table: glm_cate_attrgroup                                    */
/*==============================================================*/
create table glm_cate_attrgroup
(
   id                   varchar(32) not null comment 'id',
   status               varchar(10) comment '字典status',
   createTime           timestamp comment '创建时间',
   updateTime           timestamp comment '最近修改时间',
   categoryId           int comment 'xbm_category.id',
   attributeGroupId     int comment 'xbm_attribute_group.id',
   categoryAttributeGroupStatus varchar(10) comment '分类属性组状态',
   primary key (id)
)
charset = UTF8;

alter table glm_cate_attrgroup comment '分类与属性组关系';

/*==============================================================*/
/* Table: glm_category                                          */
/*==============================================================*/
create table glm_category
(
   categoryId           varchar(32) not null comment 'id',
   status               varchar(10) comment '字典status',
   createTime           timestamp comment '创建时间',
   updateAt             timestamp comment '最近修改时间',
   name                 varchar(200) comment '分类名称',
   parentId             int comment 'xbm_category.id',
   sort                 int(100) comment '排序',
   shopId               int comment 'xbm_shop.id',
   level                varchar(10) comment '级别',
   description          text comment '描述',
   keyword              varchar(500) comment '关键字',
   alias                varchar(200) comment '别名',
   hasChild             varchar(10) comment '是否有子节点',
   updateUserId         varchar(32) comment 'xbm_member.id',
   refreshTime          timestamp comment '属性组刷新时间',
   primary key (categoryId)
)
charset = UTF8;

alter table glm_category comment '商品分类';

/*==============================================================*/
/* Table: glm_category_product                                  */
/*==============================================================*/
create table glm_category_product
(
   productCatId         varchar(32) not null comment 'id',
   status               varchar(10) comment '字典status',
   createTime           timestamp comment '创建时间',
   updateTime           timestamp comment '最近修改时间',
   categoryId           int comment 'xbm_category.id',
   productId            int comment 'xbm_product.id',
   shopId               int comment 'xbm_shop.id',
   primary key (productCatId)
)
charset = UTF8;

alter table glm_category_product comment '商品与分类关系';

/*==============================================================*/
/* Table: glm_corporate_member                                  */
/*==============================================================*/
create table glm_corporate_member
(
   corporateId          varchar(32) not null comment 'id',
   status               varchar(10) comment '状态',
   createTime           timestamp comment '创建时间',
   updateTime           timestamp comment '最近修改时间',
   updateUserId         varchar(32) comment 'xbm_member.userId',
   userId               varchar(32) comment '用户中心id',
   userName             varchar(60) comment '登录号(用户名)',
   contactaNme          varchar(30),
   department           varchar(10),
   email                varchar(100) comment '邮箱',
   mobile               varchar(20) comment '手机号',
   phone                varchar(20) comment '联系电话',
   qq                   varchar(20) comment 'qq',
   memberType           varchar(10) comment '1: 普通会员 2：商家会员 3：店小二',
   CompanyDetail        varchar(500),
   regionId             varchar(20) comment '地区',
   address              varchar(200) comment '联系地址',
   WebsiteAddr          varchar(50),
   staff                int,
   industry             varchar(10),
   property             varchar(10),
   companyName          varchar(100) comment '昵称',
   companyIamge         varchar(100) comment '头像',
   registrID            varchar(50),
   legalPerson          varchar(50),
   regTime              timestamp,
   license              varchar(100),
   auditTime            timestamp,
   auditOpinion         varchar(500),
   auditUserId          int,
   freezeTime           timestamp,
   freezeUserId         int,
   freezeOpinion        varchar(500),
   memberStatus         varchar(10) comment '冻结状态',
   certificateState     varchar(20) comment '企业认证状态[WAIT_AUDIT:待认证;AUDIT_PASS:认证通过;AUDIT_NOT_PASS:认证不通过;]',
   certificateTime      timestamp,
   contractNo           varchar(50),
   shopId               int,
   deposit              float,
   depositStatus        varchar(10),
   postCode             varchar(20) comment '邮政编码',
   lastLoginTime        timestamp comment '最后登录时间',
   remark               varchar(500) comment '备注',
   isOpenShop           varchar(10),
   primary key (corporateId)
)
charset = UTF8;

alter table glm_corporate_member comment '企业会员';

/*==============================================================*/
/* Table: glm_delivery                                          */
/*==============================================================*/
create table glm_delivery
(
   deliveryId           varchar(32) not null comment 'id',
   status               varchar(10) comment '字典status',
   createTime           timestamp comment '创建时间',
   updateTime           timestamp comment '最近修改时间',
   freightTemplateId    int comment 'xbm_freight_template.id',
   logisticsId          int comment 'xbm_logistics.id',
   type                 varchar(10) comment '类型',
   priceType            varchar(10) comment '计价方式',
   remark               varchar(500) comment '备注',
   primary key (deliveryId)
)
charset = UTF8;

alter table glm_delivery comment '配送方式';

/*==============================================================*/
/* Table: glm_delivery_rule                                     */
/*==============================================================*/
create table glm_delivery_rule
(
   deliveryRuleId       varchar(32) not null comment 'id',
   status               varchar(10) comment '字典status',
   createTime           timestamp comment '创建时间',
   updateTime           timestamp comment '最近修改时间',
   deliveryId           int comment 'xbm_delivery.id',
   first                int(16) comment '首件',
   firstPrice           int(16) comment '首费',
   second               int(16) comment '续件',
   secondPrice          int(16) comment '续费',
   freightTemplateId    int comment 'xbm_freight_template.id',
   logisticsId          int comment 'xbm_logistics.id',
   type                 varchar(10) comment '类型',
   priceType            varchar(10) comment '计价方式',
   primary key (deliveryRuleId)
)
charset = UTF8;

alter table glm_delivery_rule comment '配送方式-规则';

/*==============================================================*/
/* Table: glm_delivery_rule_region                              */
/*==============================================================*/
create table glm_delivery_rule_region
(
   deliRuleRegionId     varchar(32) not null comment 'id',
   status               varchar(10) comment '字典status',
   createTime           timestamp comment '创建时间',
   updateTime           timestamp comment '最近修改时间',
   deliveryRuleId       int comment 'xbm_delivery_rule',
   regionId             int comment 'xbm_region.id',
   deliveryId           int comment 'xbm_delivery.id',
   freightTemplateId    int comment 'xbm_freight_template.id',
   logisticsId          int comment 'xbm_logistics.id',
   primary key (deliRuleRegionId)
)
charset = UTF8;

alter table glm_delivery_rule_region comment '配送规则-地区';

/*==============================================================*/
/* Table: glm_dict                                              */
/*==============================================================*/
create table glm_dict
(
   dictId               varchar(32) not null comment 'id',
   code                 varchar(50) comment '编码',
   name                 varchar(50) comment '名称',
   sno                  int comment '序号',
   remark               varchar(100) comment '描述',
   status               varchar(10) comment '状态',
   createTime           timestamp comment '创建时间',
   updateTime           timestamp comment '最近修改时间',
   primary key (dictId)
)
charset = UTF8;

alter table glm_dict comment '字典表';

/*==============================================================*/
/* Table: glm_dict_value                                        */
/*==============================================================*/
create table glm_dict_value
(
   dictvalueId          varchar(32) not null comment 'id',
   status               varchar(10) comment '状态',
   createTime           timestamp comment '创建时间',
   updateTime           timestamp comment '最近修改时间',
   dictId               varchar(50) comment 'xbm_dict.id',
   code                 varchar(50) comment '值编码',
   name                 varchar(50) comment '值名称',
   serialNo             int comment '序号',
   remark               varchar(100) comment '描述',
   primary key (dictvalueId)
)
charset = UTF8;

alter table glm_dict_value comment '字典值表';

/*==============================================================*/
/* Table: glm_file                                              */
/*==============================================================*/
create table glm_file
(
   fileId               varchar(32) not null comment 'id',
   status               varchar(10) comment '字典status',
   createTime           timestamp comment '创建时间',
   updateTime           timestamp comment '最近修改时间',
   fileStorageId        int comment 'xbm_file_storage.id',
   url                  varchar(200) comment '图片地址',
   sort                 int(11) comment '排序',
   type                 varchar(20) comment '类型',
   createUserId         int comment 'xbm_memberId',
   name                 varchar(100) comment '名称',
   size                 varchar(50) comment '大小',
   remark               varchar(500) comment '备注',
   primary key (fileId)
)
charset = UTF8;

alter table glm_file comment '文件';

/*==============================================================*/
/* Table: glm_file_storage                                      */
/*==============================================================*/
create table glm_file_storage
(
   fileStorageId        varchar(32) not null comment 'id',
   status               varchar(10) comment '字典status',
   createTime           timestamp comment '创建时间',
   updateAt             timestamp comment '最近修改时间',
   name                 varchar(100) comment '名称',
   parentId             int comment 'xbm_photo_gallery.id',
   memberId             int comment 'xbm_member.id',
   shopId               int comment 'xbm_shop.id',
   cover                varchar(32) comment '封面图',
   type                 varchar(10) comment 'type',
   primary key (fileStorageId)
)
charset = UTF8;

alter table glm_file_storage comment '文件库';

/*==============================================================*/
/* Table: glm_freight_template                                  */
/*==============================================================*/
create table glm_freight_template
(
   freightTemplateId    varchar(32) not null comment 'id',
   status               varchar(10) comment '字典status',
   createTime           timestamp comment '创建时间',
   updateTime           timestamp comment '最近修改时间',
   name                 varchar(100) comment '模板名称',
   address              varchar(200) comment '发货地址',
   priceType            varchar(20) comment '计价方式',
   remark               text comment '备注',
   shopId               varchar(32) comment 'xbm_shop.id',
   logisticsIds         text comment '选中的物流id，多个物流用英文","隔开',
   primary key (freightTemplateId)
)
charset = UTF8;

alter table glm_freight_template comment '运费模板';

/*==============================================================*/
/* Table: glm_history                                           */
/*==============================================================*/
create table glm_history
(
   historyId            varchar(32) not null comment 'id',
   status               varchar(10) comment '字典status',
   createTime           timestamp comment '创建时间',
   updateTime           timestamp comment '最近修改时间',
   targetId             int comment '目标id',
   versionId            int comment '版本id',
   content              text comment '内容',
   createUserId         int comment 'xbm_member.userId',
   type                 varchar(10) comment '类型(会员、店铺...)',
   primary key (historyId)
)
charset = UTF8;

alter table glm_history comment '历史版本';

/*==============================================================*/
/* Table: glm_log                                               */
/*==============================================================*/
create table glm_log
(
   logId                varchar(32) not null comment 'id',
   status               varchar(10) comment '字典status',
   createTime           timestamp comment '创建时间',
   updateTime           timestamp comment '最近修改时间',
   memberId             int comment 'xbm_member.userId',
   name                 varchar(200) comment '账号名称',
   level                varchar(10) comment '日志级别',
   module               varchar(20) comment '模块',
   actionType           varchar(20) comment '操作类型',
   content              text comment '内容',
   primary key (logId)
)
charset = UTF8;

alter table glm_log comment '日志';

/*==============================================================*/
/* Table: glm_logistics                                         */
/*==============================================================*/
create table glm_logistics
(
   logisticsId          varchar(32) not null comment 'id',
   status               varchar(10) comment '字典status',
   createTime           timestamp comment '创建时间',
   updateTime           timestamp comment '最近修改时间',
   name                 varchar(32) comment '名称',
   code                 varchar(32) comment '代码',
   url                  varchar(32) comment '官网',
   inf                  varchar(32) comment '接口',
   remark               varchar(500) comment '备注',
   primary key (logisticsId)
)
charset = UTF8;

alter table glm_logistics comment '物流';

/*==============================================================*/
/* Table: glm_member                                            */
/*==============================================================*/
create table glm_member
(
   memberId             varchar(32) not null comment 'id',
   status               varchar(10) comment '状态',
   createTime           timestamp comment '创建时间',
   updateTime           timestamp comment '最近修改时间',
   updateUserId         varchar(32) comment 'xbm_member.userId',
   userId               varchar(32) comment '用户中心id',
   userName             varchar(60) comment '登录号(用户名)',
   email                varchar(100) comment '邮箱',
   mobile               varchar(20) comment '手机号',
   phone                varchar(20) comment '联系电话',
   qq                   varchar(20) comment 'qq',
   regionId             varchar(20) comment '地区',
   address              varchar(200) comment '联系地址',
   postCode             varchar(20) comment '邮政编码',
   nickName             varchar(100) comment '昵称',
   headIamge            varchar(200) comment '头像',
   memberType           varchar(20) comment '会员类型',
   remark               varchar(500) comment '备注',
   lastLoginTime        timestamp comment '最后登录时间',
   memberStatus         varchar(20) comment '冻结状态',
   personalCertificateStatus varchar(20) comment '个人认证状态[WAIT_AUDIT:待认证;AUDIT_PASS:认证通过;AUDIT_NOT_PASS:认证不通过;]',
   certificateTime      timestamp,
   sex                  varchar(10),
   birthday             timestamp,
   realName             varchar(30),
   idNumberImage        varchar(100),
   idNumber             varchar(30),
   primary key (memberId)
)
charset = UTF8;

alter table glm_member comment '会员';

/*==============================================================*/
/* Table: glm_member_type                                       */
/*==============================================================*/
create table glm_member_type
(
   membertypeId         varchar(32) not null comment 'id',
   status               varchar(10) comment '状态',
   createTime           timestamp comment '创建时间',
   updateTime           timestamp comment '修改时间',
   name                 varchar(32) comment '名称',
   discount             decimal(4,3) comment '折扣',
   code                 varchar(10) comment '编码',
   primary key (membertypeId)
)
charset = UTF8;

alter table glm_member_type comment '会员类型';

/*==============================================================*/
/* Table: glm_message                                           */
/*==============================================================*/
create table glm_message
(
   messageId            varchar(32) not null comment 'id',
   status               varchar(10) comment '字典status',
   createTime           timestamp comment '创建时间',
   updateTime           timestamp comment '最近修改时间',
   sendMemberId         int comment 'xbm_member.userId',
   receiveMemberId      int comment 'xbm_member.userId',
   content              text comment '消息内容',
   shopId               int comment 'xbm_shop.id',
   examine              varchar(10) comment '是否查看',
   sendDelete           varchar(10) comment '发送人是否删除',
   receiveDelete        varchar(10) comment '接收人是否删除',
   primary key (messageId)
)
charset = UTF8;

alter table glm_message comment '消息';

/*==============================================================*/
/* Table: glm_message_reply                                     */
/*==============================================================*/
create table glm_message_reply
(
   messageReplyId       varchar(32) not null comment 'id',
   status               varchar(10) comment '字典status',
   createTime           timestamp comment '创建时间',
   updateTime           timestamp comment '最近修改时间',
   messageId            int comment 'xbm_message.id',
   content              text comment '回复内容',
   replyMemberId        int comment 'xbm_member.userId',
   receiveMemberId      int comment 'xbm_member.userId',
   primary key (messageReplyId)
)
charset = UTF8;

alter table glm_message_reply comment '消息回复';

/*==============================================================*/
/* Table: glm_mycollect                                         */
/*==============================================================*/
create table glm_mycollect
(
   mycollectId          varchar(32) not null comment 'id',
   status               varchar(10) comment '字典status',
   createTime           timestamp comment '创建时间',
   updateTime           timestamp comment '最近修改时间',
   memberId             int comment 'xbm_member.userId',
   type                 varchar(10) comment '收藏类型',
   target               varchar(32) comment '收藏的目标',
   description          text comment '描述',
   primary key (mycollectId)
)
charset = UTF8;

alter table glm_mycollect comment '我的收藏';

/*==============================================================*/
/* Table: glm_order                                             */
/*==============================================================*/
create table glm_order
(
   orderId              varchar(32) not null comment 'id',
   status               varchar(10) comment '字典status',
   createTime           timestamp comment '创建时间',
   updateTime           timestamp comment '最近修改时间',
   buyerMemberId        int comment 'xbm_member.userId',
   shopId               int comment 'xbm_shop.id',
   phone                varchar(20) comment '收货联系电话',
   postalCode           varchar(20) comment '收货邮编',
   email                varchar(100) comment '收货人email',
   address              varchar(200) comment '收货地址',
   regionId             varchar(32) comment 'xbm_region.id',
   userName             varchar(100) comment '收货人',
   mobile               varchar(20) comment '收货人手机',
   orderType            varchar(20) comment '订单类型',
   source               varchar(20) comment '订单来源',
   aliasCode            varchar(32) comment '外部订单号',
   createMemberId       int comment 'xbm_member.userId',
   payType              varchar(32) comment '支付方式',
   payState             varchar(32) comment '支付状态',
   processState         varchar(32) comment '处理状态',
   invoiceType          varchar(32) comment '发票类型',
   invoiceTitle         varchar(32) comment '发票抬头',
   invoiceContent       varchar(200) comment '发票内容',
   primary key (orderId)
)
charset = UTF8;

alter table glm_order comment '订单基本信息';

/*==============================================================*/
/* Table: glm_order_item                                        */
/*==============================================================*/
create table glm_order_item
(
   orderItemId          varchar(32) not null comment 'id',
   status               varchar(10) comment '字典status',
   createTime           timestamp comment '创建时间',
   updateTime           timestamp comment '最近修改时间',
   orderId              int comment 'xbm_order.id',
   productId            int comment 'xbm_product.id',
   productVersion       varchar(32) comment '商品版本',
   unitPrice            int comment '单价',
   amount               int comment '购买数量',
   returnAmount         int comment '退货数量',
   swapAmount           int comment '换货数量',
   productName          varchar(500) comment '商品名称',
   productType          varchar(32) comment '商品类型',
   image                varchar(100) comment '商品图片',
   attrs                varchar(200) comment '商品库存属性',
   skuId                int comment 'sku内部id',
   realSkuId            int comment 'sku外部id',
   shopId               int comment 'xbm_shop.id',
   moneyTypeId          varchar(32) comment '价格类型',
   extra                text comment '扩展',
   primary key (orderItemId)
)
charset = UTF8;

alter table glm_order_item comment '订单商品';

/*==============================================================*/
/* Table: glm_order_log                                         */
/*==============================================================*/
create table glm_order_log
(
   orderlogId           varchar(32) not null comment 'id',
   status               varchar(10) comment '字典status',
   createTime           timestamp comment '状态',
   updateTime           timestamp comment '最近修改时间',
   orderId              int comment 'xbm_order.id',
   createUserId         int comment 'xbm_member.userId',
   remark               text comment '备注',
   beforeState          varchar(10) comment '修改前状态',
   afterState           varchar(10) comment '修改后状态',
   primary key (orderlogId)
)
charset = UTF8;

/*==============================================================*/
/* Table: glm_order_price                                       */
/*==============================================================*/
create table glm_order_price
(
   orderpriceId         varchar(32) not null comment 'id',
   status               varchar(10) comment '字典status',
   createTime           timestamp comment '创建时间',
   updateTime           timestamp comment '最近修改时间',
   totalPrice           bigint comment '订单总价',
   totalSavedPrice      bigint comment '订单总优惠',
   orderId              int comment 'xbm_order.id',
   primary key (orderpriceId)
)
charset = UTF8;

alter table glm_order_price comment '订单价格';

/*==============================================================*/
/* Table: glm_product                                           */
/*==============================================================*/
create table glm_product
(
   productId            varchar(32) not null comment 'id',
   status               varchar(10) comment '字典status',
   createTime           timestamp comment '创建时间',
   updateTime           timestamp comment '最近修改时间',
   productName          varchar(100) comment '商品名称',
   shopId               int comment 'xbm_shop.id',
   productNumber        varchar(10) comment '商品编号',
   detail               text comment '商品详情',
   image                varchar(500) comment '商品图片',
   version              varchar(10) comment '版本',
   auditStatus          varchar(10) comment '审核状态',
   putawayStatus        varchar(10) comment '上架状态',
   putawayTime          timestamp comment '上架时间',
   auditTime            timestamp comment '审核时间',
   putawayUserId        int comment 'xbm_member.userId',
   auditUserId          int comment 'xbm_member.userId',
   brandId              int comment 'xbm_brand.id',
   shopBrandId          int comment 'xbm_brand.id',
   categoryId           int comment 'xbm_category.id',
   shopCategoryId       int comment '店铺分类id(可多个,以英文","分隔)',
   custom               varchar(10) comment '定制商品',
   freightType          varchar(20) comment '运费类型',
   freightTemplateId    int comment '运费模板',
   createUserId         int comment 'xbm_member.userId',
   unit                 varchar(20) comment '计量单位',
   prices               text comment '价格',
   dynaAttrs            text comment '动态属性',
   payWay               text comment '支付方式',
   primary key (productId)
)
charset = UTF8;

alter table glm_product comment '商品';

/*==============================================================*/
/* Table: glm_product_attribute                                 */
/*==============================================================*/
create table glm_product_attribute
(
   productAttrId        varchar(32) not null comment 'id',
   status               varchar(10) comment '字典status',
   createTime           timestamp comment '创建时间',
   updateTime           timestamp comment '最近修改时间',
   productId            int comment 'xbm_product.id',
   attributeId          int comment 'xbm_attribute.id',
   primary key (productAttrId)
)
charset = UTF8;

alter table glm_product_attribute comment '商品与属性关系';

/*==============================================================*/
/* Table: glm_product_inventory                                 */
/*==============================================================*/
create table glm_product_inventory
(
   productInventId      varchar(32) not null comment 'id',
   status               varchar(10) comment '字典status',
   createAt             timestamp comment '创建时间',
   updateAt             timestamp comment '最近修改时间',
   productId            int comment 'xbm.product.id',
   skuId                int comment 'xbm.product_sku.id',
   number               int comment '数量',
   primary key (productInventId)
)
charset = UTF8;

alter table glm_product_inventory comment '商品库存';

/*==============================================================*/
/* Table: glm_product_sku                                       */
/*==============================================================*/
create table glm_product_sku
(
   productSkuId         varchar(32) not null comment 'id',
   status               varchar(10) comment '字典status',
   createTime           timestamp comment '创建时间',
   updateTime           timestamp comment '最近修改时间',
   productId            int comment 'xbm_product.id',
   attr                 varchar(500) comment '商品属性',
   attrName             varchar(100) comment '商品属性名称 ',
   productVersionId     int comment '商品版本id',
   realSkuId            int comment '真实物料ID',
   primary key (productSkuId)
)
charset = UTF8;

alter table glm_product_sku comment '商品sku';

/*==============================================================*/
/* Table: glm_recommend                                         */
/*==============================================================*/
create table glm_recommend
(
   recommendId          varchar(32) not null comment 'id',
   status               varchar(10) not null comment '字典status',
   createAt             timestamp comment '创建时间',
   updateAt             timestamp comment '最近修改时间',
   memberId             int comment 'xbm_member.userId',
   columnId             int comment '推荐类目',
   targetId             int comment '被推荐id',
   type                 varchar(10) comment '推荐类型',
   primary key (recommendId)
)
charset = UTF8;

alter table glm_recommend comment '推荐';

/*==============================================================*/
/* Table: glm_region                                            */
/*==============================================================*/
create table glm_region
(
   regionId             varchar(32) not null comment 'id',
   status               varchar(10) comment '字典status',
   createTime           timestamp comment '创建时间',
   updateTime           timestamp comment '最近修改时间',
   name                 varchar(150) comment '名称',
   sort                 int(11) comment '排序',
   level                varchar(10) comment '级别',
   longCode             varchar(12) comment '长编码',
   code                 varchar(3) comment '编码',
   parentId             int comment 'xbm_region.id',
   keywords             varchar(256) comment '关键字',
   pinyin               varchar(32) comment '拼音',
   hasChild             tinyint(4) comment '是否有下级地区[0:没有,1:有]',
   primary key (regionId)
)
charset = UTF8;

alter table glm_region comment '地区';

/*==============================================================*/
/* Table: glm_report                                            */
/*==============================================================*/
create table glm_report
(
   reportId             varchar(32) not null comment 'id',
   status               varchar(10) comment '字典status',
   createTime           timestamp comment '创建时间',
   updateTime           timestamp comment '最近修改时间',
   type                 varchar(10) comment '类型',
   memberId             int comment 'xbm_member.userId',
   targetMemberId       int comment 'xbm_member.userId',
   orderId              int comment 'xbm_order.id',
   content              text comment '内容',
   remark               text comment '备注',
   primary key (reportId)
)
charset = UTF8;

alter table glm_report comment '举报';

/*==============================================================*/
/* Table: glm_serial_number                                     */
/*==============================================================*/
create table glm_serial_number
(
   serialNumId          varchar(32) not null comment 'id',
   status               varchar(10) comment '字典status',
   createTime           timestamp comment '创建时间',
   updateTime           timestamp comment '最近修改时间',
   type                 varchar(10) comment '类型',
   serialKey            varchar(32) comment 'key名称',
   value                bigint comment '值',
   primary key (serialNumId)
)
charset = UTF8;

alter table glm_serial_number comment '序列';

/*==============================================================*/
/* Table: glm_shop                                              */
/*==============================================================*/
create table glm_shop
(
   shopId               varchar(32) not null comment 'id',
   status               varchar(10) comment '字典status',
   createTime           timestamp comment '创建时间',
   updateTime           timestamp comment '最近修改时间',
   shopNumber           varchar(20) comment '店铺编号',
   name                 varchar(100) comment '店铺名称',
   logo                 varchar(100) comment '店铺logo',
   introduce            text comment '店铺介绍',
   operationMode        varchar(100) comment '经营方式',
   companyName          varchar(100) comment '企业名称',
   contact              varchar(100) comment '联系人',
   phone                varchar(100) comment '联系电话',
   regionId             int comment 'xbm_region.id',
   userId               int comment 'xbm.member.userId',
   address              varchar(200) comment '通讯地址',
   auditTime            timestamp comment '审核时间',
   auditOpinion         varchar(100) comment '审核意见',
   auditUserId          int comment 'xbm_member.userId',
   level                varchar(10) comment '店铺等级',
   templateId           int comment '暂未定',
   domain               varchar(500) comment '店铺域名',
   state                varchar(32) comment '店铺状态',
   qq                   varchar(20) comment 'QQ',
   email                varchar(32) comment '电子邮箱',
   postcode             varchar(20) comment '邮编',
   freezeOpinion        varchar(100) comment '冻结意见',
   freezeTime           timestamp comment '冻结状态最后修改时间',
   freezeUserId         int comment '冻结状态最后修改人',
   primary key (shopId)
)
charset = UTF8;

alter table glm_shop comment '店铺';

/*==============================================================*/
/* Table: glm_shop_category                                     */
/*==============================================================*/
create table glm_shop_category
(
   shopCategoryId       varchar(32) not null comment 'id',
   status               varchar(10) comment '字典status',
   createTime           timestamp comment '状态',
   updateTime           timestamp comment '最近修改时间',
   categoryId           int comment 'xbm_category.id',
   shopId               int comment 'xbm_shop.id',
   parentId             int comment 'xbm_category.id',
   primary key (shopCategoryId)
)
charset = UTF8;

alter table glm_shop_category comment '店铺与分类关系';

/*==============================================================*/
/* Table: glm_sys_param                                         */
/*==============================================================*/
create table glm_sys_param
(
   sysparmId            varchar(32) not null comment 'id',
   code                 varchar(50) comment '编码',
   name                 varchar(50) comment '名称',
   value                varchar(200) comment '参数值',
   type                 varchar(20) comment '参数类型',
   remark               varchar(100) comment '描述',
   status               varchar(10) comment '状态',
   createTime           timestamp comment '创建时间',
   updateTime           timestamp comment '最近修改时间',
   primary key (sysparmId)
)
charset = UTF8;

alter table glm_sys_param comment '系统参数表';

/*==============================================================*/
/* Table: glm_trade                                             */
/*==============================================================*/
create table glm_trade
(
   tradeId              varchar(32) not null comment 'id',
   status               varchar(10) comment '字典status',
   createTime           timestamp comment '创建时间',
   updateTime           timestamp comment '最近修改时间',
   orderId              int comment 'xbm_order.id',
   orderAliasCode       varchar(100) comment 'xbm_order.aliasCode',
   operator             varchar(32) comment '类型',
   remark               varchar(100) comment '备注',
   type                 varchar(32) comment '交易类型',
   money                int comment '交易金额',
   tradeStatus          varchar(32) comment '交易状态',
   serialNumber         varchar(100) comment '流水号',
   primary key (tradeId)
)
charset = UTF8;

alter table glm_trade comment '交易流水';

