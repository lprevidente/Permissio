insert into users (id, creator_id)
values (1, null),
       (2, 1);

insert into offices(id, name, creator_id)
values (1, 'Head Office', 1);

update users
set office_id = 1
where id = 1;

insert into handlers (id, user_id, type)
values (1, 1, 'HR'),
       (1, 2, 'OP');

insert into teams (id, name)
values (1, 'Team 1'),
       (2, 'Team 2');

insert into team_members (team_id, member_id, is_handler, type)
values (1, 1, true, 'HR'),
       (1, 2, false, 'HR'),
       (2, 1, true, 'OP');