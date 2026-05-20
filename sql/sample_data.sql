-- 샘플 테이터
insert into user(email, name)
values
('psh@gmail.com', 'psh'),
('jmg@gmail.com', 'jmg');

insert into music(title, artist, genre, play_time)
values
('Daisies', "Justin Bieber", 'pop', '2:57'),
("SPEED DEMON", "Justin Bieber", 'pop', '3:31'),
('REDRED', 'Cortis', 'kpop', '2:43'),
("A COLD PLAY", "The Kid LAROI", 'pop', '3:00');

insert into playlist(user_id, playlist_name)
values
(1, '힙합'),
(1, '운동할때'),
(2, '집중용'),
(2, '드라이브');

insert into playlist_music(playlist_id, music_id)
values
(1, 2),
(1, 4),
(2, 1),
(2, 2),
(3, 1),
(3, 2),
(4, 3),
(4, 4);

-- 샘플데이터 추가
insert into music(title, artist, genre, play_time)
values
('GO BABY', 'Justin Bieber', 'pop', '3:15'),
('How Sweet', 'NewJeans', 'kpop', '3:39'),
('Minute by Minute', 'Penthouse', 'jpop', '3:59'),
('Nan-Nan', 'Fuji Kaze', 'jpop', '5:21'),
('Ditto', 'NewJeans', 'kpop', '3:05'),
('Hype Boy', 'NewJeans', 'kpop', '2:59'),
('Steady', 'NCT WISH', 'kpop', '3:00'),
('OMG', 'NewJeans', 'kpop', '3:32'),
('Nobody But Us', 'Mario', 'pop', '3:14'),
('Get A Guitar', 'RIIZE', 'kpop', '2:40');

insert into playlist_music(playlist_id, music_id)
values
(1, 5),
(1, 6),
(1, 7),
(2, 5),
(2, 8),
(2, 9),
(3, 5),
(3, 6),
(3, 10),
(4, 5),
(4, 7),
(4, 11);

-- 확인
select * from user;
select * from music;
select * from playlist;
select * from playlist_music;