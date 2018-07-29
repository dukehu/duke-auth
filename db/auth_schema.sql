drop table if exists auth_login_lock;
drop table if exists auth_operation_code;
drop table if exists auth_resource;
drop table if exists auth_resource_operation_code_r;
drop table if exists auth_role;
drop table if exists auth_role_resource_r;
drop table if exists auth_user;
drop table if exists auth_user_role_r;
drop table if exists oauth_access_token;
drop table if exists oauth_refresh_token;

create table auth_login_lock
(
  id                   varchar(50) not null,
  user_id              varchar(50) not null,
  lock_date            datetime not null,
  lock_num             int(1) not null,
  primary key (id)
);
alter table auth_login_lock comment '登陆锁定表';

create table auth_operation_code
(
  id                   varchar(50) not null,
  service_id           varchar(50) not null,
  name                 varchar(200) not null,
  code                 varchar(50) not null,
  memo                 varchar(200) not null,
  path                 varchar(50) not null,
  controller           varchar(50) not null,
  request_method       varchar(10) not null,
  primary key (id)
);
alter table auth_operation_code comment '操作码表';

create table auth_resource
(
  id                   varchar(50) not null,
  parent_id            varchar(50) not null,
  name                 varchar(50) not null,
  code                 varchar(50) not null,
  type                 int(1) not null comment '栏目
            按钮',
  status               int(1) not null,
  router               varchar(50) not null,
  memo                 varchar(50) not null,
  creater              varchar(50) not null,
  create_time          datetime not null,
  modifier             varchar(50) not null,
  modify_time          datetime not null,
  primary key (id)
);
alter table auth_resource comment '资源表';

create table auth_resource_operation_code_r
(
  id                   varchar(50) not null,
  resource_id          varchar(50) not null,
  operation_code_id    varchar(50) not null,
  primary key (id)
);
alter table auth_resource_operation_code_r comment '资源跟操作码得关系表';

create table auth_role
(
  id                   varchar(50) not null,
  code                 varchar(50) not null,
  name                 varchar(50) not null,
  status               int(1) not null,
  role_type            int(1) not null,
  memo                 varchar(50) not null,
  creater              varchar(50) not null,
  create_time          datetime not null,
  modifier             varchar(50) not null,
  modify_time          datetime not null,
  primary key (id)
);
alter table auth_role comment '角色表';

create table auth_role_resource_r
(
  id                   varchar(50) not null,
  role_id              varchar(50) not null,
  resource_id          varchar(50) not null,
  primary key (id)
);
alter table auth_role_resource_r comment '角色跟资源得关系表';

create table auth_user
(
  id                   varchar(50) not null,
  gender               int(1) not null,
  real_name            varchar(100) not null,
  nick_name            varchar(100) not null,
  login_name           varchar(100) not null,
  login_password       varchar(100) not null,
  login_mobile         varchar(50) not null,
  login_email          varchar(50) not null,
  status               int(1) not null,
  create_time          datetime not null,
  modify_time          datetime not null,
  avatar               varchar(50) not null,
  primary key (id)
);
alter table auth_user comment '用户表';

create table auth_user_role_r
(
  id                   varchar(50) not null,
  user_id              varchar(50) not null,
  role_id              varchar(50) not null,
  primary key (id)
);
alter table auth_user_role_r comment '用户角色关系表';

create table oauth_access_token (
  create_time       timestamp default now(),
  token_id          varchar(255),
  token             blob,
  authentication_id varchar(255),
  user_name         varchar(255),
  client_id         varchar(255),
  authentication    blob,
  refresh_token     varchar(255)
);

create table oauth_refresh_token (
  create_time    timestamp default now(),
  token_id       varchar(255),
  token          blob,
  authentication blob
);