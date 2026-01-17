# java-filmorate

Template repository for Filmorate project.

![Предварительная схема БД](https://disk.yandex.ru/i/wVEKSr5S1UhKUA)

#### Вывод 5 самых популярных фильмов

SELECT f.name, COUNT(l.film_id) likes
from film f  
join likes l ON f.film_id = l.film_id  
GROUP BY name  
ORDER BY likes DESC  
Limit 5;

#### Найти пользователей без друзей

SELECT name   
FROM users  
WHERE NO EXISTS  
(SELECT 1  
FROM Friendship f  
join friendship_status fs ON f.status_id = fs.status_id  
WHERE u.user_id = f.user_id  
fs.name = 'CONFIRMED')

#### Посчитать сколько фильмов относится к каждому жанру

SELECT g.name genre, COUNT(fg.genre_id) count  
FROM Genre g  
join Film_genre fg ON g.genre_id = fg.genre_id  
GROUP BY genre  
ORDER BY count DESC  
limit 10;  
