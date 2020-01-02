INSERT INTO customer (id, first_name, last_name, email, password, birth_date, phone_number, address)
VALUES
 ('customerId1', 'firstName1', 'lastName1', 'email1@email.com', 'encryptedPassword1', '1992-09-11', '9999999998', 'address1'),
 ('customerId2', 'firstName1', 'lastName1', 'email2@email.com', 'encryptedPassword2', '1988-01-11', '9999999997', 'address2'),
 ('customerId3', 'firstName3', 'lastName3', 'email3@email.com', 'encryptedPassword3', '1990-09-23', '9999999996', 'address3');


INSERT INTO invoice (id, customer_id, created_date, total_price)
VALUES
 ('invoiceId1', 'customerId1', '2020-01-05', 1.55),
 ('invoiceId2', 'customerId2', '2020-01-05', 1.55),
 ('invoiceId3', 'customerId3', '2020-01-05', 1.55),
 ('invoiceId4', 'customerId3', '2020-01-05', 11.54);


INSERT INTO invoice_item (invoice_id, description, amount, price, tax)
VALUES
('invoiceId2', 'description2', 1, '1.5500', '0.1860'),
('invoiceId3', 'description3', 1, '1.5500', '0.1860'),
 ('invoiceId4', 'description4', 1, '1.5500', '0.1860'),
 ('invoiceId4', 'description5', 2, '4.9950', '0.5994');
