delete from invoice where id = 'invoiceId1';
delete from invoice where id = 'invoiceId2';
delete from invoice where id = 'invoiceId3';
delete from invoice where id = 'invoiceId4';

delete from invoice_item where invoice_id = 'invoiceId3';
delete from invoice_item where invoice_id = 'invoiceId4';

delete from customer where id='customerId1';
delete from customer where id='customerId2';
delete from customer where id='customerId3';