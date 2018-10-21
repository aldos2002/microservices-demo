drop table T_PRODUCT
if exists;

create table T_PRODUCT (
  ID      bigint identity primary key,
  NUMBER  varchar(9),
  PRODUCT_NAME    varchar(50) not null,
  PRICE decimal(8, 2),
  unique (NUMBER)
);

ALTER TABLE T_PRODUCT
  ALTER COLUMN PRICE SET DEFAULT 0.0;
