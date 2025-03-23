-- select * from jv_commit;
-- select * from jv_commit_property;
-- select * from jv_global_id;

select s.snapshot_pk,
       c.author,
       c.commit_date,
       g.type_name,
       g.local_id,
       s.version,
       s.changed_properties,
       s.state
  from jv_snapshot s
  join jv_commit c
on c.commit_pk = s.commit_fk
  join jv_global_id g
on g.global_id_pk = s.global_id_fk
 where g.type_name = 'Issue'
   and g.local_id = 2
   --and c.author = 'User1'
 order by c.commit_date;

select to_char(i.EXPIRY_DATE,'YYYY-MM-DD'), i.*
  from issue_event i
 where i.id = 2;

 
