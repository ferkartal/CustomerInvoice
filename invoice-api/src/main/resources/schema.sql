create table customer (
    id varchar(36) not null primary key,
    first_name varchar(30) not null,
    last_name varchar(30) not null,
    email varchar(255) not null,
    password varchar(255) not null,
    birth_date date,
    phone_number varchar(30) not null,
    address varchar(255) not null
);

create table invoice (
    id varchar(36) not null primary key,
    customer_id varchar(36) not null,
    created_date date not null,
    total_price decimal(12, 4) not null
);

create table invoice_item (
    invoice_id varchar(36) not null,
    description varchar(30) not null,
    amount int not null,
    price decimal(12, 4) not null,
    tax decimal(12, 4) not null
);
