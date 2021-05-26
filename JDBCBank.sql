create table users (
	USER_ID serial primary key,
	is_employee boolean,
	username varchar(20) unique,
	u_password varchar(20)
);
create table accounts (
	BANK_ACCOUNT_ID serial primary key,
	amount integer,
	pending boolean
);
create table users_accounts (
	USER_ID integer references users(USER_ID),
	BANK_ACCOUNT_ID integer references accounts(BANK_ACCOUNT_ID)
);
create table trans_types(
	type_id serial primary key,
	type_name varchar(20)
);
insert into trans_types(type_name)
values ('Withdrawal'),
		('Deposit'),
		('Transfer');
create table transactions (
	acc_from integer default(0),
	acc_to integer default (0),
	amount integer,
	type_id integer references trans_types(type_id),
	ts timestamp default(current_timestamp)
);
create or replace function withdraw(account integer, t_amount integer)
returns void
language plpgsql
as
$$
begin
	update accounts set amount = amount - t_amount where BANK_ACCOUNT_ID = account;
	insert into transactions (acc_from, amount, type_id)
	values (account, t_amount, 1);
end
$$;
create or replace function deposit(account integer, t_amount integer)
returns void
language plpgsql
as
$$
begin
	update accounts set amount = amount + t_amount where BANK_ACCOUNT_ID = account;
	insert into transactions (acc_to, amount, type_id)
	values (account, t_amount, 2);
end
$$;
create or replace function transfer(acc_f integer, acc_t integer, t_amount integer)
returns void
language plpgsql
as
$$
begin
	update accounts set amount = amount - t_amount where BANK_ACCOUNT_ID = acc_f;
	update accounts set amount = amount + t_amount where BANK_ACCOUNT_ID = acc_t;
	insert into transactions (acc_from, acc_to, amount, type_id)
	values (acc_f, acc_t, t_amount, 3);
end
$$;
create or replace function add_account(amt integer, pen boolean)
returns int
language plpgsql
as 
$$
begin 
	insert into accounts(amount, pending) values (amt, pen);
	return max(bank_account_id) from accounts;
end
$$;
create or replace function add_user(emp boolean, u_name varchar(20), u_pass varchar(20))
returns int
language plpgsql
as 
$$
begin 
	insert into users(is_employee, username, u_password)
	values(emp, u_name, u_pass);
	return max(user_id) from users;
end
$$;


