create table `user`
(
    id serial not null
        constraint user_pk
            primary key,
    username varchar not null,
    role varchar not null,
    status varchar not null
);

create unique index user_username_uindex
    on user (username);

-- And the sensitive user data table
create table sensitive_user_data
(
    id serial not null
        constraint sensitive_user_data_pk
            primary key,
    `user` int not null
        constraint sensitive_user_data_user_id_fk
            references `user` (id),
    password varchar
);

create unique index sensitive_user_data_user_uindex
    on sensitive_user_data (`user`);