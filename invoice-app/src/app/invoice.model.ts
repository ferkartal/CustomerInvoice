import { InvoiceItem } from './invoice-item.model';

export class Invoice {
    id: string;
    totalPrice: number;
    createdDate: Date;
    invoiceItems: Array<InvoiceItem>;
}
