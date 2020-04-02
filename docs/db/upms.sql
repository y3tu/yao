create schema if not exists upms collate utf8_general_ci;

create table if not exists t_department
(
	DEPARTMENT_ID varchar(255) not null comment '部门ID'
		primary key,
	PARENT_ID bigint not null comment '上级部门ID',
	DEPARTMENT_NAME varchar(100) not null comment '部门名称',
	ORDER_NUM double(20,0) null comment '排序',
	CREATE_TIME datetime null comment '创建时间',
	MODIFY_TIME datetime null comment '修改时间'
)
comment '部门表';

create table if not exists t_login_log
(
	ID varchar(255) not null comment 'id'
		primary key,
	USERNAME varchar(50) not null comment '用户名',
	LOGIN_TIME datetime not null comment '登录时间',
	LOCATION varchar(50) null comment '登录地点',
	IP varchar(50) null comment 'IP地址',
	SYSTEM varchar(50) null comment '操作系统',
	BROWSER varchar(50) null comment '浏览器'
)
comment '登录日志表';

create table if not exists t_resource
(
	RESOURCE_ID varchar(255) not null comment '菜单/按钮ID'
		primary key,
	PARENT_ID varchar(255) not null comment '上级菜单ID',
	RESOURCE_NAME varchar(50) not null comment '菜单/按钮名称',
	PATH varchar(255) null comment '对应路由path',
	COMPONENT varchar(255) null comment '对应路由组件component',
	PERMISSION varchar(50) null comment '权限标识',
	ICON varchar(50) null comment '图标',
	TYPE int(1) not null comment '类型 0菜单 1按钮 2目录',
	ORDER_NUM double(20,0) null comment '排序',
	CREATE_TIME datetime not null comment '创建时间',
	MODIFY_TIME datetime null comment '修改时间',
	IFRAME tinyint(1) null comment '是否外链',
	CACHE tinyint(1) null comment '是否缓存',
	HIDDEN tinyint(1) null comment '是否隐藏',
	COMPONENT_NAME varchar(100) null comment '组件名称 '
)
comment '菜单表';

create table if not exists t_role
(
	ROLE_ID varchar(255) not null comment '角色ID'
		primary key,
	ROLE_NAME varchar(10) not null comment '角色名称',
	REMARK varchar(100) null comment '角色描述',
	CREATE_TIME datetime not null comment '创建时间',
	MODIFY_TIME datetime null comment '修改时间',
	ROLE_CODE varchar(50) null comment '角色编码',
	DEFAULT_ROLE tinyint(1) default 0 null comment '是否默认角色'
)
comment '角色表';

create table if not exists t_role_resource
(
	ROLE_ID varchar(255) not null,
	RESOURCE_ID varchar(255) not null,
	ID varchar(255) not null
)
comment '角色菜单关联表';

create table if not exists t_user
(
	USER_ID varchar(255) not null comment '用户ID'
		primary key,
	USERNAME varchar(50) not null comment '用户名',
	PASSWORD varchar(128) not null comment '密码',
	DEPARTMENT_ID varchar(255) null comment '部门ID',
	EMAIL varchar(128) null comment '邮箱',
	MOBILE varchar(20) null comment '联系电话',
	STATUS char not null comment '状态 0锁定 1有效',
	CREATE_TIME datetime not null comment '创建时间',
	MODIFY_TIME datetime null comment '修改时间',
	LAST_LOGIN_TIME datetime null comment '最近访问时间',
	SSEX char null comment '性别 0男 1女 2保密',
	IS_TAB char null comment '是否开启tab，0关闭 1开启',
	THEME varchar(10) null comment '主题',
	AVATAR varchar(100) null comment '头像',
	DESCRIPTION varchar(200) null comment '描述'
)
comment '用户表';

create table if not exists t_user_role
(
	USER_ID varchar(255) not null comment '用户ID',
	ROLE_ID varchar(255) not null comment '角色ID',
	ID varchar(255) not null
)
comment '用户角色关联表';

