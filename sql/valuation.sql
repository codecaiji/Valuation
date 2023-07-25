DROP TABLE IF EXISTS `valuation_configs`;
CREATE TABLE `valuation_configs`
(
    `id`                       number(20) NOT NULL AUTO_INCREMENT COMMENT 'key',
    `name`                     varchar(255) NOT NULL COMMENT 'config name',
    `attri_configs`            varchar(255)  COMMENT '',
    `range_configs`            varchar(255) NOT NULL COMMENT '',
    `compu_formulas`            varchar(255) NOT NULL COMMENT '',

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