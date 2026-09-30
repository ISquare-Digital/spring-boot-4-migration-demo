create table product (
    id          bigint primary key,
    name        varchar(120) not null,
    price_cents bigint not null,
    released_on date not null
);
