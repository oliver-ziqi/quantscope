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
    tenantId     bigint                                null comment 'Tenant ID',
    userName     varchar(256)                           null comment 'User nickname',
    userAccount  varchar(256)                           not null comment 'Account',
    userAvatar   varchar(1024)                          null comment 'User avatar',
    gender       tinyint                                null comment 'Gender',
    userRole     varchar(256) default 'user'            not null comment 'User role: user / admin (legacy)',
    userPassword varchar(512)                           not null comment 'Password',
    `accessKey` varchar(512) default '' not null comment 'accessKey',
    `secretKey` varchar(512) default '' not null comment 'secretKey',
    createTime   datetime     default CURRENT_TIMESTAMP not null comment 'Creation time',
    updateTime   datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment 'Update time',
    isDelete     tinyint      default 0                 not null comment 'Is deleted',
    constraint uni_userAccount
        unique (userAccount)
) comment 'User';

-- Tenant table
create table if not exists tenant
(
    id          bigint auto_increment comment 'ID' primary key,
    name        varchar(128)                          not null comment 'Tenant name',
    status      tinyint      default 1                not null comment '1-enabled, 0-disabled',
    createTime  datetime     default CURRENT_TIMESTAMP not null comment 'Creation time',
    updateTime  datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment 'Update time'
) comment 'Tenant';

-- Role table
create table if not exists role
(
    id          bigint auto_increment comment 'ID' primary key,
    tenantId    bigint                                not null comment 'Tenant ID',
    name        varchar(64)                           not null comment 'Role name',
    code        varchar(64)                           not null comment 'Role code'
) comment 'Role';

-- Permission table
create table if not exists permission
(
    id          bigint auto_increment comment 'ID' primary key,
    code        varchar(128)                          not null comment 'Permission code',
    `desc`      varchar(256)                          null comment 'Description'
) comment 'Permission';

-- User-Role relationship table
create table if not exists user_role
(
    userId      bigint                                not null comment 'User ID',
    roleId      bigint                                not null comment 'Role ID',
    tenantId    bigint                                not null comment 'Tenant ID'
) comment 'User-Role relationship';

-- Role-Permission relationship table
create table if not exists role_permission
(
    roleId      bigint                                not null comment 'Role ID',
    permissionId bigint                               not null comment 'Permission ID'
) comment 'Role-Permission relationship';

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

-- API service catalog
create table if not exists api_service
(
    id          bigint auto_increment comment 'ID' primary key,
    name        varchar(128)                          not null comment 'API name',
    code        varchar(64)                           not null comment 'API code',
    status      tinyint      default 1                not null comment '1-open, 0-closed'
) comment 'API service';

-- Tenant API enablement
create table if not exists tenant_api
(
    tenantId    bigint                                not null comment 'Tenant ID',
    apiId       bigint                                not null comment 'API ID',
    enabled     tinyint      default 1                not null comment '1-enabled, 0-disabled'
) comment 'Tenant API';

-- User API allocation
create table if not exists user_api
(
    userId      bigint                                not null comment 'User ID',
    apiId       bigint                                not null comment 'API ID'
) comment 'User API';

-- Strategy resource table
create table if not exists strategy
(
    id          bigint auto_increment comment 'ID' primary key,
    tenantId    bigint                                not null comment 'Tenant ID',
    name        varchar(128)                          not null comment 'Strategy name',
    status      tinyint      default 1                not null comment '1-online, 0-offline'
) comment 'Strategy';

-- Tenant Strategy enablement
create table if not exists tenant_strategy
(
    tenantId    bigint                                not null comment 'Tenant ID',
    strategyId  bigint                                not null comment 'Strategy ID',
    enabled     tinyint      default 1                not null comment '1-enabled, 0-disabled'
) comment 'Tenant Strategy';

-- User Strategy allocation
create table if not exists user_strategy
(
    userId      bigint                                not null comment 'User ID',
    strategyId  bigint                                not null comment 'Strategy ID'
) comment 'User Strategy';

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

-- Seed data (basic mock data)
insert into tenant (id, name, status) values
    (1, 'QuantScope Main Tenant', 1);

insert into user (id, tenantId, userName, userAccount, userRole, userPassword, accessKey, secretKey)
values
    (1, 1, 'Owner', 'owner@quantscope', 'admin', 'hashed_pw_owner', 'AK_OWNER', 'SK_OWNER'),
    (2, 1, 'Trader', 'trader@quantscope', 'user', 'hashed_pw_trader', 'AK_TRADER', 'SK_TRADER'),
    (3, 1, 'Analyst', 'analyst@quantscope', 'user', 'hashed_pw_analyst', 'AK_ANALYST', 'SK_ANALYST');

insert into role (id, tenantId, name, code) values
    (1, 1, 'Tenant Owner', 'owner'),
    (2, 1, 'Trader', 'trader'),
    (3, 1, 'Analyst', 'analyst');

insert into permission (id, code, `desc`) values
    (1, 'console:strategy:manage', 'Manage strategies'),
    (2, 'console:api:manage', 'Manage APIs'),
    (3, 'api:data:indicator:read', 'Read indicator data'),
    (4, 'api:data:sentiment:read', 'Read daily sentiment logs'),
    (5, 'trade:submit', 'Submit trades');

insert into user_role (userId, roleId, tenantId) values
    (1, 1, 1),
    (2, 2, 1),
    (3, 3, 1);

insert into role_permission (roleId, permissionId) values
    (1, 1),
    (1, 2),
    (2, 3),
    (2, 5),
    (3, 3),
    (3, 4);

insert into api_service (id, name, code, status) values
    (1, 'Indicator Data API', 'indicator_data', 1),
    (2, 'LLM Daily Sentiment Log API', 'sentiment_daily_log', 1);

insert into tenant_api (tenantId, apiId, enabled) values
    (1, 1, 1),
    (1, 2, 1);

insert into user_api (userId, apiId) values
    (1, 1),
    (1, 2),
    (2, 1),
    (3, 1),
    (3, 2);

insert into strategy (id, tenantId, name, status) values
    (1, 1, 'Mean Reversion', 1),
    (2, 1, 'Momentum', 1),
    (3, 1, 'Pairs Trading', 0);

insert into tenant_strategy (tenantId, strategyId, enabled) values
    (1, 1, 1),
    (1, 2, 1),
    (1, 3, 0);

insert into user_strategy (userId, strategyId) values
    (1, 1),
    (1, 2),
    (2, 1),
    (3, 2);

-- RAG job task table
create table if not exists rag_job_task
(
    id              bigint auto_increment comment 'ID' primary key,
    job_id          varchar(64)                           not null comment 'Job ID',
    tenant_id       bigint                               not null comment 'Tenant ID',
    user_id         bigint                               not null comment 'User ID',
    status          varchar(16)                           not null comment 'Status: CREATED/QUEUED/RUNNING/READY/FAILED',
    request_payload text                                 null comment 'Request payload',
    result_json     mediumtext                           null comment 'Result json',
    result_pdf_url  varchar(512)                         null comment 'Result pdf url',
    error_msg       varchar(1024)                        null comment 'Error message',
    created_at      datetime     default CURRENT_TIMESTAMP not null comment 'Creation time',
    updated_at      datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment 'Update time',
    unique key uni_rag_job_id (job_id)
) comment 'RAG job task';

-- RAG job event table
create table if not exists rag_job_event
(
    id         bigint auto_increment comment 'ID' primary key,
    job_id     varchar(64)                           not null comment 'Job ID',
    event_type varchar(32)                           not null comment 'Event type',
    payload    text                                 null comment 'Event payload',
    created_at datetime     default CURRENT_TIMESTAMP not null comment 'Creation time'
) comment 'RAG job event';

-- RAG job result table
create table if not exists rag_job_result
(
    id          bigint auto_increment comment 'ID' primary key,
    job_id      varchar(64)                           not null comment 'Job ID',
    result_type varchar(16)                           not null comment 'Result type',
    result_ref  varchar(512)                          not null comment 'Result reference',
    created_at  datetime     default CURRENT_TIMESTAMP not null comment 'Creation time'
) comment 'RAG job result';
