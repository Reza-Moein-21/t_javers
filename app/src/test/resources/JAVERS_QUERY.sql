-- select * from jv_commit;
-- select * from jv_commit_property;
-- select * from jv_global_id;

select s.snapshot_pk,
       c.author,
       c.commit_date,
       g.type_name,
       s.version,
       s.state,
       s.changed_properties
  from jv_snapshot s
  join jv_commit c
on c.commit_pk = s.commit_fk
  join jv_global_id g
on g.global_id_pk = s.global_id_fk
 order by c.commit_date desc;

select * from person p where p.id = 1;

select m.* from issue_event i 
join ISSUE_MASTER m on m.id = i.MASTER_FK
where i.id = 1;

