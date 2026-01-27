# Database initialization
-- Create database
drop database quantscope;
create database if not exists quantscope;

-- Switch database
use quantscope;

-- User table
create table if not exists user
(
    id           bigint auto_increment comment 'ID' primary key,
    userName     varchar(256)                           null comment 'User nickname',
    userAccount  varchar(256)                           not null comment 'Account',
    userAvatar   varchar(1024)                          null comment 'User avatar',
    gender       tinyint                                null comment 'Gender',
    userRole     varchar(256) default 'user'            not null comment 'User role: user / admin',
    userPassword varchar(512)                           not null comment 'Password',
    `accessKey` varchar(512) default '' not null comment 'accessKey',
    `secretKey` varchar(512) default '' not null comment 'secretKey',
    createTime   datetime     default CURRENT_TIMESTAMP not null comment 'Creation time',
    updateTime   datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment 'Update time',
    isDelete     tinyint      default 0                 not null comment 'Is deleted',
    constraint uni_userAccount
        unique (userAccount)
) comment 'User';

-- Interface information table
create table if not exists quantscope.`interface_info`
(
    `id` bigint not null auto_increment comment 'Primary key' primary key,
    `name` varchar(256) not null comment 'Name',
    `description` varchar(256) null comment 'Description',
    `url` varchar(512) not null comment 'Interface address',
    `requestParams` text not null comment 'Request parameters',
    `requestHeader` text null comment 'Request header',
    `responseHeader` text null comment 'Response header',
    `status` int default 0 not null comment 'Interface status (0-closed, 1-open)',
    `method` varchar(256) not null comment 'Request type',
    `userId` bigint not null comment 'Creator',
    `createTime` datetime default CURRENT_TIMESTAMP not null comment 'Creation time',
    `updateTime` datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment 'Update time',
    `isDelete` tinyint default 0 not null comment 'Is deleted (0-not deleted, 1-deleted)'
) comment 'Interface information';

-- User interface invocation relationship table
create table if not exists quantscope.`user_interface_info`
(
    `id` bigint not null auto_increment comment 'Primary key' primary key,
    `userId` bigint not null comment 'Invoking user ID',
    `interfaceInfoId` bigint not null comment 'Interface ID',
    `totalNum` int default 0 not null comment 'Total invocation count',
    `leftNum` int default 0 not null comment 'Remaining invocation count',
    `status` int default 0 not null comment '0-normal, 1-disabled',
    `createTime` datetime default CURRENT_TIMESTAMP not null comment 'Creation time',
    `updateTime` datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment 'Update time',
    `isDelete` tinyint default 0 not null comment 'Is deleted (0-not deleted, 1-deleted)'
) comment 'User interface invocation relationship';