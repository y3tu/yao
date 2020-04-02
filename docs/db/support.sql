create schema if not exists `support` collate utf8_general_ci;

create table if not exists data_source_config
(
	id varchar(255) not null,
	name varchar(100) null comment '数据源名称',
	driver_class varchar(100) null comment '驱动',
	jdbc_url varchar(255) null comment '数据库连接url',
	username varchar(100) null comment '登录用户名',
	password varchar(100) null comment '密码',
	create_time datetime null comment '创建时间',
	modify_time datetime null comment '修改时间',
	type varchar(30) null comment '数据库类型',
	constraint t_data_source_config_id_uindex
		unique (id)
)
comment '数据源配置';

alter table data_source_config
	add primary key (id);

create table if not exists generator_config
(
	id bigint auto_increment comment 'ID'
		primary key,
	author varchar(255) null comment '作者',
	module_name varchar(255) null comment '模块名称',
	pack varchar(255) null comment '至于哪个包下',
	prefix varchar(255) null comment '表名前缀',
	api_prefix varchar(255) null comment '前端请求路径前缀'
)
comment '代码生成配置';

create table if not exists t_dict
(
	id varchar(255) null,
	create_time timestamp null,
	update_time timestamp null,
	description tinytext null,
	name tinytext null,
	code tinytext null,
	type int null
);

create table if not exists t_dict_data
(
	id tinytext null,
	create_time timestamp null,
	update_time timestamp null,
	description tinytext null,
	dict_id tinytext null,
	sort decimal(10,2) null,
	status int null,
	name tinytext null,
	value tinytext null
);

create table if not exists t_dict_sql
(
	sql_code text null,
	sql_name text null,
	sql_text varchar(4000) null,
	value_col text null,
	name_col text null,
	max_rows int null,
	remarks tinytext null,
	create_time date null,
	update_time date null,
	status int null,
	exec_db text null,
	id varchar(256) null
);

