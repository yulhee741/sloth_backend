drop table if exists category CASCADE;
drop table if exists lesson CASCADE;
drop table if exists member CASCADE;
drop table if exists member_token CASCADE;
drop table if exists site CASCADE;

create table category (
    category_id bigint not null,
    category_lvl integer not null,
    name varchar(255) not null,
    root_category_id bigint,
    root_category_name varchar(255),
    primary key (category_id)
);

create table lesson (
   lesson_id bigint not null,
    reg_time timestamp,
    update_time timestamp,
    created_by varchar(255),
    modified_by varchar(255),
    alert_days varchar(255) not null,
    end_date date not null,
    is_finished boolean not null,
    message varchar(200),
    name varchar(150) not null,
    present_number integer not null,
    price integer not null,
    start_date date not null,
    total_number integer not null,
    category_id bigint,
    member_id bigint,
    site_id bigint,
    primary key (lesson_id)
);

create table member (
   member_id bigint not null,
    reg_time timestamp,
    update_time timestamp,
    created_by varchar(255),
    modified_by varchar(255),
    email varchar(255) not null,
    is_delete boolean not null,
    name varchar(255) not null,
    password varchar(255) not null,
    picture varchar(255),
    role varchar(255) not null,
    social_type varchar(255) not null,
    primary key (member_id)
);

create table member_token (
   member_token_id bigint not null,
    reg_time timestamp,
    update_time timestamp,
    created_by varchar(255),
    modified_by varchar(255),
    refresh_token varchar(255),
    token_expiration_time timestamp,
    member_id bigint,
    primary key (member_token_id)
);

create table site (
   site_id bigint not null,
    name varchar(100) not null,
    primary key (site_id)
);

alter table member
add constraint UK_mbmcqelty0fbrvxp1q58dn57t unique (email)
;

alter table lesson
add constraint FK5fh9e98erf9mfa9udc11vlxtr
foreign key (category_id)
references category
;

alter table lesson
add constraint FKkx2caj5sqlomiff494dug8jio
foreign key (member_id)
references member
;

alter table lesson
add constraint FK62tlmbxrjw0o0jw79s3bbheke
foreign key (site_id)
references site
;

alter table member_token
add constraint FKt02uutgl1v2am5mshqqdk1cvd
foreign key (member_id)
references member
;



insert into category
values(1, 0, '개발', 1, '개발')
;

insert into category
values(2, 0, '디자인', 2, '디자인')
;