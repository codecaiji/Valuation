DROP TABLE IF EXISTS `VALUATION_CONFIGS`;
CREATE TABLE `VALUATION_CONFIGS`
(
    `id`                       number(20) NOT NULL AUTO_INCREMENT COMMENT 'key',
    `name`                     varchar(255) NOT NULL COMMENT 'config name',
    `status`                 varchar(255)  COMMENT '',
    `description`            varchar(1023) NOT NULL COMMENT '',

    `create_time`              datetime     NOT NULL COMMENT 'create time',
    `create_user`              varchar(255) NOT NULL COMMENT 'create user',
    `modify_time`              datetime     NOT NULL COMMENT 'modify time',
    `modify_user`              varchar(255) NOT NULL COMMENT 'modify user',
    PRIMARY KEY (`id`)
) AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

----创建序列
create sequence ID_INCREMENT
    increment by 1
    start with 1;

----创建触发器
create or replace trigger VALUATION_ID_INCREMENT
before insert on VALUATION_CONFIGS
for each row
begin
select  ID_INCREMENT.nextval into :new.ID from DUAL;
end;


CREATE TABLE "SCOTT"."COMPU_FORMULAS" (
    "CONFIG_ID" NUMBER NOT NULL,
    "TARGET_NAME" VARCHAR2(255) NOT NULL,
    "FUNC" VARCHAR2(255) NOT NULL,
    CONSTRAINT "VALUATION_CONFIG_ID" FOREIGN KEY ("CONFIG_ID") REFERENCES "VALUATION_CONFIGS" ("ID") ON DELETE CASCADE
)
CREATE TABLE "SCOTT"."RANGE_CONFIG" (
      "CONFIG_ID" NUMBER NOT NULL,
      "TARGET_NAME" VARCHAR2(255) NOT NULL,
      "FUNC" VARCHAR2(255) NOT NULL,
      CONSTRAINT "VALUATION_CONFIG_ID" FOREIGN KEY ("CONFIG_ID") REFERENCES "VALUATION_CONFIGS" ("ID") ON DELETE CASCADE
)
CREATE TABLE "SCOTT"."ATTRI_CONFIG" (
    "CONFIG_ID" NUMBER NOT NULL,
    "NAME" VARCHAR2(255) NOT NULL,
    "FIELD_SCORES" VARCHAR2(4000) NOT NULL,
    CONSTRAINT "VALUATION_CONFIG_ID" FOREIGN KEY ("CONFIG_ID") REFERENCES "VALUATION_CONFIGS" ("ID") ON DELETE CASCADE
)