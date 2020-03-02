drop table if exists file_meta;

create table if not exists file_meta(-- 创建表操作
    name varchar(50) not null,
    path varchar(1000) not null,
<<<<<<< HEAD
    is_directory boolean not null,
=======
>>>>>>> c9db452291d2eb63072f2c5e2f6ac1de5ebf7d73
    size bigint not null,
    last_modified timestamp not null,
    pinyin varchar(50),
    pinyin_first varchar(50)
);