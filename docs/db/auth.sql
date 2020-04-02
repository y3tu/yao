create schema if not exists auth collate utf8_general_ci;

create table if not exists oauth_client_details
(
	client_id varchar(255) not null
		primary key,
	resource_ids varchar(255) null,
	client_secret varchar(255) not null,
	scope varchar(255) not null,
	authorized_grant_types varchar(255) not null,
	web_server_redirect_uri varchar(255) null,
	authorities varchar(255) null,
	access_token_validity int not null,
	refresh_token_validity int null,
	additional_information varchar(4096) null,
	autoapprove tinyint null,
	origin_secret varchar(255) null
)
charset=utf8mb4;

INSERT INTO auth.oauth_client_details (client_id, resource_ids, client_secret, scope, authorized_grant_types, web_server_redirect_uri, authorities, access_token_validity, refresh_token_validity, additional_information, autoapprove, origin_secret) VALUES ('app', '', '$2a$10$88IWBHS3PUn9.NSeA1cMbeotMCbc2tOposnW7efm4ed1T4ZCay2ei', 'all', 'password,refresh_token', '', null, 86400, 8640000, null, 1, '123456');
INSERT INTO auth.oauth_client_details (client_id, resource_ids, client_secret, scope, authorized_grant_types, web_server_redirect_uri, authorities, access_token_validity, refresh_token_validity, additional_information, autoapprove, origin_secret) VALUES ('febs', null, '$2a$10$aSZTvMOtUAYUQ.75z2n3ceJd6dCIk9Vy3J/SKZUE4hBLd6sz7.6ge', 'all', 'password,refresh_token', null, null, 86400, 8640000, null, 1, '123456');
INSERT INTO auth.oauth_client_details (client_id, resource_ids, client_secret, scope, authorized_grant_types, web_server_redirect_uri, authorities, access_token_validity, refresh_token_validity, additional_information, autoapprove, origin_secret) VALUES ('swagger', '', '$2a$10$HSXh7gb19pngr8gcvzkODOvhQ048m6mrlXtaHJmBUE2jca7RvjEBa', 'test', 'password,refresh_token', '', null, 86400, 86400, '', 0, '123456');

create table if not exists t_user_connection
(
	USER_NAME varchar(50) not null comment 'Yao系统用户名',
	PROVIDER_NAME varchar(20) not null comment '第三方平台名称',
	PROVIDER_USER_ID varchar(50) not null comment '第三方平台账户ID',
	PROVIDER_USER_NAME varchar(50) null comment '第三方平台用户名',
	NICK_NAME varchar(50) null comment '第三方平台昵称',
	IMAGE_URL varchar(512) null comment '第三方平台头像',
	LOCATION varchar(255) null comment '地址',
	REMARK varchar(255) null comment '备注',
	ID varchar(255) not null
		primary key
);

