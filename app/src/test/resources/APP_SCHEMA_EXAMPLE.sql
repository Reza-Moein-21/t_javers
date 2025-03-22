insert into customer (
   id,
   version,
   created_at,
   created_by,
   cif,
   full_name
) values ( customer_seq.nextval,
           1,
           sysdate,
           'test',
           '12345',
           'CUS NAME' );

insert into party (
   id,
   version,
   created_at,
   created_by,
   type_flag,
   key29
) values ( party_seq.nextval,
           1,
           sysdate,
           'test',
           'CUSTOMER',
           1 );

insert into issue_master (
   id,
   version,
   created_at,
   created_by,
   reference,
   input_branch,
   behalf_branch,
   issue_date,
   expiry_date,
   amount,
   ccy,
   applicant_fk,
   beneficiary_fk
) values ( issue_master_seq.nextval,
           1,
           sysdate,
           'test',
           'ILC1001/123456',
           '1001',
           '1001',
           date '2025-01-10',
           date '2030-01-10',
           1238348.23,
           'EUR',
           1,
           1 );


insert into issue_event (
   id,
   version,
   created_at,
   created_by,
   master_fk,
   applicant_reference,
   issue_date,
   expiry_date,
   amount,
   ccy,
   applicant_fk,
   beneficiary_fk
) values ( issue_event_seq.nextval,
           1,
           sysdate,
           'test',
           1,
           'ILC1001/123456',         
           date '2025-01-10',
           date '2030-01-10',
           1238348.23,
           'EUR',
           1,
           1 );

-- commit;