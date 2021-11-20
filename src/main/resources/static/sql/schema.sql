CREATE TABLE IF NOT EXISTS board(
idx INT NOT NULL AUTO_INCREMENT COMMENT '게시글 번호(PK)', 
group_idx INT NOT NULL COMMENT '그룹 번호', 
group_order INT NOT NULL COMMENT '그룹 순서', 
group_depth INT NOT NULL COMMENT '그룹 깊이', 
title VARCHAR(30) NOT NULL COMMENT '제목', 
content VARCHAR(300) NOT NULL COMMENT '내용', 
member_idx INT NOT NULL COMMENT '회원 번호', 
password VARCHAR(100) NOT NULL COMMENT '비밀번호', 
view_count INT NOT NULL COMMENT '조회 수', 
notice_check ENUM('Y','N') NOT NULL COMMENT '공지 여부', 
delete_check ENUM('Y','N') NOT NULL COMMENT '삭제 여부', 
insert_time DATETIME NOT NULL COMMENT '등록일', 
update_time DATETIME COMMENT '수정일', 
delete_time DATETIME COMMENT '삭제일',
PRIMARY KEY(idx)) 
COMMENT '게시판' CHARSET = utf8;

CREATE TABLE IF NOT EXISTS board_file(
idx INT NOT NULL AUTO_INCREMENT COMMENT '파일 번호(PK)', 
board_idx INT NOT NULL COMMENT '게시글 번호(FK)', 
original_name VARCHAR(250) NOT NULL COMMENT '원본 파일명', 
save_file VARCHAR(200) NOT NULL COMMENT '저장 파일', 
size INT NOT NULL COMMENT '파일 크기', 
insert_time DATETIME NOT NULL COMMENT '등록일', 
PRIMARY KEY(idx), 
CONSTRAINT fk_board_file FOREIGN KEY (board_idx) REFERENCES board(idx)) 
COMMENT '게시판 첨부 파일' CHARSET = utf8;

CREATE TABLE IF NOT EXISTS restaurant(
idx INT NOT NULL AUTO_INCREMENT COMMENT '레스토랑 번호(PK)', 
title VARCHAR(50) NOT NULL COMMENT '제목', 
content VARCHAR(50) NOT NULL COMMENT '내용', 
score DECIMAL(2,1) NOT NULL COMMENT '평점', 
view_count INT NOT NULL COMMENT '조회 수', 
poster VARCHAR(500) NOT NULL COMMENT '사진 경로', 
address VARCHAR(70) NOT NULL COMMENT '주소', 
PRIMARY KEY(idx), 
CONSTRAINT uk_restaurant_content UNIQUE (title, content)) 
COMMENT '레스토랑' CHARSET = utf8;

CREATE TABLE IF NOT EXISTS restaurant_detail(
idx INT NOT NULL AUTO_INCREMENT COMMENT '레스토랑 세부사항 번호(PK)',
restaurant_idx INT NOT NULL COMMENT '레시피 번호(FK)', 
old_address VARCHAR(70) COMMENT '지번 주소', 
phone_number VARCHAR(30) COMMENT '전화번호', 
food_type VARCHAR(40) COMMENT '음식 종류', 
price VARCHAR(50) COMMENT '가격', 
parking VARCHAR(20) COMMENT '주차 여부', 
business_time VARCHAR(100) COMMENT '영업시간', 
menu VARCHAR(200) COMMENT '메뉴', 
site VARCHAR(300) COMMENT '사이트', 
good INT COMMENT '좋음', 
soso INT COMMENT '보통', 
bad INT COMMENT '나쁨', 
PRIMARY KEY(idx), 
CONSTRAINT fk_restaurant_detail FOREIGN KEY (restaurant_idx) REFERENCES restaurant(idx)) 
COMMENT '레스토랑 세부사항' CHARSET = utf8;

CREATE TABLE IF NOT EXISTS restaurant_evaluation(
restaurant_idx INT NOT NULL COMMENT '레스토랑 번호(FK)', 
evaluation ENUM('good', 'soso', 'bad') NOT NULL COMMENT '평가 점수', 
member_idx INT NOT NULL COMMENT '회원 번호', 
CONSTRAINT fk_restaurant_evaluation FOREIGN KEY (restaurant_idx) REFERENCES restaurant(idx), 
CONSTRAINT uk_restaurant_member UNIQUE (restaurant_idx, member_idx)) 
COMMENT '레스토랑 평가' CHARSET = utf8;

CREATE TABLE IF NOT EXISTS restaurant_hashtag(
idx INT NOT NULL AUTO_INCREMENT COMMENT '레스토랑 해시태그 번호(PK)', 
hashtag VARCHAR(30) NOT NULL COMMENT '해시태그', 
PRIMARY KEY(idx)) 
COMMENT '레스토랑 해시태그' CHARSET = utf8;

CREATE TABLE IF NOT EXISTS restaurant_connect(
restaurant_idx INT NOT NULL COMMENT '레스토랑 번호(PK,FK)', 
hashtag_idx INT NOT NULL COMMENT '해시태그 번호(PK,FK)', 
PRIMARY KEY (restaurant_idx, hashtag_idx), 
CONSTRAINT fk_connect_restaurant FOREIGN KEY (restaurant_idx) REFERENCES restaurant(idx), 
CONSTRAINT fk_connect_restaurant_hashtag FOREIGN KEY (hashtag_idx) REFERENCES restaurant_hashtag(idx)) 
COMMENT '레스토랑 해시태그 연결' CHARSET = utf8;

CREATE TABLE IF NOT EXISTS recipe(
idx INT NOT NULL AUTO_INCREMENT COMMENT '레시피 번호(PK)', 
title VARCHAR(100) NOT NULL COMMENT '제목', 
writer VARCHAR(50) NOT NULL COMMENT '작성자', 
view_count INT NOT NULL COMMENT '조회 수', 
poster VARCHAR(1000) NOT NULL COMMENT '사진 경로', 
PRIMARY KEY(idx), 
CONSTRAINT uk_recipe_writer UNIQUE (title, writer)) 
COMMENT '레시피' CHARSET = utf8;

CREATE TABLE IF NOT EXISTS recipe_detail(
idx INT NOT NULL AUTO_INCREMENT COMMENT '레시피 세부사항 번호(PK)',
recipe_idx INT NOT NULL COMMENT '레시피 번호(FK)',
content TEXT COMMENT '내용', 
amount VARCHAR(50) COMMENT '음식량', 
time VARCHAR(50) COMMENT '조리 시간', 
difficulty VARCHAR(50) COMMENT '난이도', 
cookingOrder TEXT NOT NULL COMMENT '요리 순서', 
tip VARCHAR(400) COMMENT '팁', 
PRIMARY KEY(idx), 
CONSTRAINT fk_recipe_detail FOREIGN KEY (recipe_idx) REFERENCES recipe(idx)) 
COMMENT '레시피 세부사항' CHARSET = utf8;

CREATE TABLE IF NOT EXISTS recipe_evaluation(
recipe_idx INT NOT NULL COMMENT '레시피 번호(FK)', 
evaluation ENUM('good', 'soso', 'bad') NOT NULL COMMENT '평가 점수', 
member_idx INT NOT NULL COMMENT '회원 번호', 
CONSTRAINT fk_recipe_evaluation FOREIGN KEY (recipe_idx) REFERENCES recipe(idx), 
CONSTRAINT uk_recipe_member UNIQUE (recipe_idx, member_idx)) 
COMMENT '레시피 평가' CHARSET = utf8;

CREATE TABLE IF NOT EXISTS recipe_hashtag(
idx INT NOT NULL AUTO_INCREMENT COMMENT '레시피 해시태그 번호(PK)', 
hashtag VARCHAR(30) NOT NULL COMMENT '해시태그', 
PRIMARY KEY(idx)) 
COMMENT '레시피 해시태그' CHARSET = utf8;

CREATE TABLE IF NOT EXISTS recipe_connect(
recipe_idx INT NOT NULL COMMENT '레시피 번호(FK)', 
hashtag_idx INT NOT NULL COMMENT '해시태그 번호(FK)', 
PRIMARY KEY (recipe_idx, hashtag_idx), 
CONSTRAINT fk_connect_recipe FOREIGN KEY (recipe_idx) REFERENCES recipe(idx), 
CONSTRAINT fk_connect_recipe_hashtag FOREIGN KEY (hashtag_idx) REFERENCES recipe_hashtag(idx)) 
COMMENT '레시피 해시태그 연결' CHARSET = utf8;

CREATE TABLE IF NOT EXISTS recipe_save(
recipe_idx INT NOT NULL COMMENT '레시피 번호(FK)', 
member_idx INT NOT NULL COMMENT '회원 번호(FK)', 
PRIMARY KEY (recipe_idx, member_idx), 
CONSTRAINT fk_recipe_save FOREIGN KEY (recipe_idx) REFERENCES recipe(idx)) 
COMMENT '레시피 저장' CHARSET = utf8;

CREATE TABLE IF NOT EXISTS seoul_attractions(
idx INT NOT NULL AUTO_INCREMENT COMMENT '명소 번호(PK)', 
type VARCHAR(20) NOT NULL COMMENT '타입', 
title VARCHAR(200) NOT NULL COMMENT '제목', 
content TEXT NOT NULL COMMENT '내용', 
score DECIMAL(2,1) NOT NULL COMMENT '평점', 
view_count INT NOT NULL COMMENT '조회 수', 
poster VARCHAR(500) NOT NULL COMMENT '사진 경로', 
address VARCHAR(70) NOT NULL COMMENT '주소', 
PRIMARY KEY(idx), 
CONSTRAINT uk_seoul_attractions UNIQUE (title, type)) 
COMMENT '서울 명소' CHARSET = utf8;

CREATE TABLE IF NOT EXISTS seoul_attractions_detail(
idx INT NOT NULL AUTO_INCREMENT COMMENT '명소 세부사항 번호(PK)', 
seoul_attractions_idx INT NOT NULL COMMENT '레시피 번호(FK)', 
phone_number VARCHAR(30) COMMENT '전화번호', 
business_time VARCHAR(300) COMMENT '영업시간', 
business_week VARCHAR(100) COMMENT '운영 요일', 
site VARCHAR(300) COMMENT '사이트', 
tip VARCHAR(500) COMMENT '팁', 
PRIMARY KEY(idx), 
CONSTRAINT fk_attractions_detail FOREIGN KEY (seoul_attractions_idx) REFERENCES seoul_attractions(idx)) 
COMMENT '서울 명소 세부사항' CHARSET = utf8;

CREATE TABLE IF NOT EXISTS music(
idx INT NOT NULL COMMENT '랭킹 번호(PK)', 
poster VARCHAR(200) NOT NULL COMMENT '사진 경로', 
title VARCHAR(100) NOT NULL COMMENT '제목', 
singer VARCHAR(100) NOT NULL COMMENT '가수', 
album VARCHAR(100) NOT NULL COMMENT '앨범', 
state VARCHAR(10) NOT NULL COMMENT '랭킹 변동', 
increment INT COMMENT '랭킹 변동값', 
youtube_key VARCHAR(300) NOT NULL COMMENT '유튜브 키', 
PRIMARY KEY(idx)) 
COMMENT '음악 랭킹' CHARSET = utf8;

CREATE TABLE IF NOT EXISTS movie(
idx INT NOT NULL COMMENT '영화 번호(PK)',
title VARCHAR(50) NOT NULL COMMENT '제목',
score DECIMAL(4,2) NOT NULL COMMENT '점수', 
grade VARCHAR(10) NOT NULL COMMENT '등급',
reserve VARCHAR(20) NOT NULL COMMENT '예매율',
genre VARCHAR(30) NOT NULL COMMENT '장르',
time VARCHAR(30) NOT NULL COMMENT '시간',
regdate VARCHAR(20) NOT NULL COMMENT '개봉일',
director VARCHAR(50) NOT NULL COMMENT '감독',
actor VARCHAR(300) NOT NULL COMMENT '배우',
showUser VARCHAR(20) NOT NULL COMMENT '관객 수',
view_count INT NOT NULL COMMENT '조회 수', 
poster VARCHAR(200) NOT NULL COMMENT '사진 경로',
story TEXT NOT NULL COMMENT '내용',
youtube_key VARCHAR(300) NOT NULL COMMENT '유튜브 키',
PRIMARY KEY(idx)) 
COMMENT '영화' CHARSET = utf8;

CREATE TABLE IF NOT EXISTS movie_reservation(
idx INT NOT NULL AUTO_INCREMENT COMMENT '예약 번호(PK)', 
movie_idx INT NOT NULL COMMENT '영화 번호(FK)', 
member_idx INT NOT NULL COMMENT '회원 번호', 
adult_number INT NOT NULL COMMENT '일반 수', 
minors_number INT NOT NULL COMMENT '청소년 수', 
movie_theater VARCHAR(50) NOT NULL COMMENT '영화관', 
reservation_time DATETIME NOT NULL COMMENT '예약일', 
PRIMARY KEY(idx), 
CONSTRAINT fk_movie_reservation FOREIGN KEY (movie_idx) REFERENCES movie(idx)) 
COMMENT '영화 예매' CHARSET = utf8;

CREATE TABLE IF NOT EXISTS movie_seat(
reservation_idx INT NOT NULL COMMENT '예약 번호(FK)', 
seat VARCHAR(10) NOT NULL COMMENT '좌석 자리', 
CONSTRAINT fk_movie_seat FOREIGN KEY (reservation_idx) REFERENCES movie_reservation(idx)) 
COMMENT '영화 좌석' CHARSET = utf8;

CREATE TABLE IF NOT EXISTS shopping_basket(
idx INT NOT NULL AUTO_INCREMENT COMMENT '장바구니 번호(PK)', 
item_name VARCHAR(70) NOT NULL COMMENT '상품 이름', 
item_price INT NOT NULL COMMENT '상품 가격', 
item_link VARCHAR(300) NOT NULL COMMENT '상품 링크', 
item_image VARCHAR(100) NOT NULL COMMENT '상품 이미지', 
item_number INT NOT NULL COMMENT '상품 갯수', 
member_idx INT NOT NULL COMMENT '회원 번호', 
PRIMARY KEY(idx)) 
COMMENT '장바구니' CHARSET = utf8;

CREATE TABLE IF NOT EXISTS room(
room_id VARCHAR(100) NOT NULL COMMENT '방ID', 
title VARCHAR(100) NOT NULL COMMENT '방이름', 
people_number INT NOT NULL DEFAULT 0 COMMENT '인원수', 
insert_time DATETIME NOT NULL DEFAULT NOW() COMMENT '생성일', 
PRIMARY KEY(room_id)) 
COMMENT '채팅방' CHARSET = utf8;

CREATE TABLE IF NOT EXISTS basic_comment(
idx INT NOT NULL COMMENT '댓글 번호(PK)', 
target_idx INT NOT NULL COMMENT '타겟 번호', 
target_type VARCHAR(20) NOT NULL COMMENT '타겟 타입', 
content VARCHAR(50) NOT NULL COMMENT '내용', 
member_idx INT NOT NULL COMMENT '회원 번호', 
delete_check ENUM('Y', 'N') NOT NULL COMMENT '삭제 여부', 
insert_time DATETIME NOT NULL COMMENT '등록일', 
update_time DATETIME COMMENT '수정일', 
delete_time DATETIME COMMENT '삭제일', 
PRIMARY KEY(idx)) 
COMMENT '댓글' CHARSET = utf8;

CREATE TABLE IF NOT EXISTS basic_reservation(
idx INT NOT NULL AUTO_INCREMENT COMMENT '예약 번호(PK)', 
target_idx INT NOT NULL COMMENT '타겟 번호', 
target_type VARCHAR(20) NOT NULL COMMENT '타겟 타입', 
member_idx INT NOT NULL COMMENT '회원 번호', 
people_number INT NOT NULL COMMENT '인원수', 
reservation_time DATETIME NOT NULL COMMENT '예약일', 
parking ENUM('Y', 'N') NOT NULL COMMENT '주차 여부',
status INT NOT NULL COMMENT '처리 현황', 
PRIMARY KEY(idx)) 
COMMENT '예약' CHARSET = utf8;

CREATE TABLE IF NOT EXISTS game(
idx INT NOT NULL COMMENT '게임 번호(PK)', 
title VARCHAR(50) NOT NULL COMMENT '이름', 
content VARCHAR(100) NOT NULL COMMENT '내용', 
link VARCHAR(100) NOT NULL COMMENT '게임 링크', 
PRIMARY KEY(idx))
COMMENT '게임' CHARSET = utf8;

CREATE TABLE IF NOT EXISTS game_ranking(
idx INT NOT NULL AUTO_INCREMENT COMMENT '랭킹 번호(PK)', 
game_idx INT NOT NULL COMMENT '게임 번호(FK)', 
member_idx INT NOT NULL COMMENT '회원 번호', 
score INT NOT NULL COMMENT '점수', 
ranking_time DATETIME NOT NULL COMMENT '등록일', 
PRIMARY KEY (idx), 
CONSTRAINT fk_game_ranking FOREIGN KEY (game_idx) REFERENCES game(idx), 
CONSTRAINT uk_game_member UNIQUE (game_idx, member_idx)) 
COMMENT '미니 게임 랭킹' CHARSET = utf8;

CREATE TABLE IF NOT EXISTS game_dash_adventure(
member_idx INT NOT NULL COMMENT '회원 번호', 
dia INT NOT NULL COMMENT '다이아', 
ball INT NOT NULL COMMENT '장착중인 캐릭터', 
shop_ball VARCHAR(111) NOT NULL COMMENT '캐릭터 구매 내역', 
normal_stage INT NOT NULL COMMENT '노멀 스테이지', 
normal_stage_count INT NOT NULL COMMENT '노멀 스테이지 카운터', 
hard_stage_open INT NOT NULL COMMENT '하드 스테이지 오픈',
hard_stage INT NOT NULL COMMENT '하드 스테이지', 
hard_stage_count INT NOT NULL COMMENT '하드 스테이지 카운터',
PRIMARY KEY (member_idx)) 
COMMENT '대쉬 어드벤쳐' CHARSET = utf8;

UPDATE room SET people_number = 0;

/*
--START TRANSACTION; SET AUTOCOMMIT=0; 2가지는 같은 기능
--COMMIT; SET AUTOCOMMIT=1;
--레스토랑 데이터 저장
DELIMITER $$
CREATE PROCEDURE IF NOT EXISTS restaurant_insert(
 IN in_title VARCHAR(50) CHARACTER SET utf8, 
 IN in_content VARCHAR(50) CHARACTER SET utf8, 
 IN in_score DECIMAL(2,1), 
 IN in_view_count INT, 
 IN in_poster VARCHAR(500) CHARACTER SET utf8, 
 IN in_hashtag VARCHAR(500) CHARACTER SET utf8, 
 IN in_address VARCHAR(70) CHARACTER SET utf8, 
 IN in_old_address VARCHAR(70) CHARACTER SET utf8, 
 IN in_phone_number VARCHAR(30) CHARACTER SET utf8, 
 IN in_food_type VARCHAR(40) CHARACTER SET utf8, 
 IN in_price VARCHAR(50) CHARACTER SET utf8, 
 IN in_parking VARCHAR(20) CHARACTER SET utf8, 
 IN in_business_time VARCHAR(100) CHARACTER SET utf8, 
 IN in_menu VARCHAR(200) CHARACTER SET utf8, 
 IN in_site VARCHAR(300) CHARACTER SET utf8, 
 IN in_good INT, 
 IN in_soso INT, 
 IN in_bad INT
)
BEGIN
 DECLARE hashtag_nm VARCHAR(100) CHARACTER SET utf8;
 DECLARE restaurant_idx INT;
 DECLARE hashtag_idx INT;
 DECLARE count_check INT;
 START TRANSACTION;
 INSERT INTO restaurant 
 VALUES(NULL, in_title, in_content, in_score, in_view_count, in_poster, in_address);
 SET restaurant_idx = (SELECT LAST_INSERT_ID());
 INSERT INTO restaurant_detail 
 VALUES(NULL, restaurant_idx, in_old_address, in_phone_number, in_food_type, in_price, in_parking, in_business_time, in_menu, in_site, in_good, in_soso, in_bad);
 WHILE (SELECT in_hashtag LIKE '%#%') DO 
  SET in_hashtag = TRIM(SUBSTRING(in_hashtag, INSTR(in_hashtag, '#')+1, 500));
  IF (SELECT in_hashtag LIKE '%#%') THEN
   SET hashtag_nm = TRIM(SUBSTRING_INDEX(in_hashtag, '#', 1));
  ELSE
   SET hashtag_nm = in_hashtag;
  END IF;
  IF CHAR_LENGTH(hashtag_nm) > 0 THEN
   SET count_check = (SELECT COUNT(*) FROM restaurant_hashtag WHERE hashtag = hashtag_nm);
   IF count_check > 0 THEN
    SET hashtag_idx = (SELECT idx FROM restaurant_hashtag WHERE hashtag = hashtag_nm);
   ELSE
    INSERT INTO restaurant_hashtag VALUES(NULL, hashtag_nm);
    SET hashtag_idx = (SELECT LAST_INSERT_ID());
   END IF;
   INSERT INTO restaurant_connect VALUES(restaurant_idx, hashtag_idx);
  END IF;
 END WHILE;
 COMMIT;
END $$
DELIMITER ;

--레스토랑 리스트 카운트
DELIMITER $$
CREATE PROCEDURE IF NOT EXISTS restaurant_list_count(
 IN in_keyword VARCHAR(100) CHARACTER SET utf8
)
BEGIN
 IF INSTR(in_keyword, '#') > 0 THEN
  SET in_keyword = TRIM(SUBSTRING(in_keyword, 2, 100));
  SELECT COUNT(*) 
  FROM restaurant as rs JOIN (SELECT DISTINCT rs_cn.restaurant_idx as idx FROM restaurant_hashtag as rs_ht JOIN restaurant_connect as rs_cn ON rs_ht.idx = rs_cn.hashtag_idx WHERE rs_ht.hashtag = in_keyword) as rs_gr 
  ON rs.idx = rs_gr.idx;
 ELSE
  SELECT COUNT(*) 
  FROM restaurant 
  WHERE title LIKE CONCAT('%', in_keyword, '%') OR content like CONCAT('%', in_keyword, '%') OR address like CONCAT('%', in_keyword, '%');
 END IF;
END $$
DELIMITER ;

--레스토랑 리스트
DELIMITER $$
CREATE PROCEDURE IF NOT EXISTS restaurant_list_select(
 IN in_keyword VARCHAR(100) CHARACTER SET utf8,
 IN in_min INT,
 IN in_max INT
)
BEGIN
 IF INSTR(in_keyword, '#') > 0 THEN
  SET in_keyword = TRIM(SUBSTRING(in_keyword, 2, 100));
  SELECT rs.idx, title, content, score, view_count, poster 
  FROM restaurant as rs JOIN (SELECT DISTINCT rs_cn.restaurant_idx as idx FROM restaurant_hashtag as rs_ht JOIN restaurant_connect as rs_cn ON rs_ht.idx = rs_cn.hashtag_idx WHERE rs_ht.hashtag = in_keyword) as rs_gr 
  ON rs.idx = rs_gr.idx 
  ORDER BY score DESC, view_count DESC 
  LIMIT in_min, in_max;
 ELSE
  SELECT idx, title, content, score, view_count, poster 
  FROM restaurant 
  WHERE title LIKE CONCAT('%', in_keyword, '%') OR content like CONCAT('%', in_keyword, '%') OR address like CONCAT('%', in_keyword, '%') 
  ORDER BY score DESC, view_count DESC 
  LIMIT in_min, in_max;
 END IF;
END $$
DELIMITER ;

--레스토랑
DELIMITER $$
CREATE PROCEDURE IF NOT EXISTS restaurant_select(
 IN in_idx INT
)
BEGIN
 DECLARE hashtag_nm VARCHAR(500) CHARACTER SET utf8 DEFAULT '';
 DECLARE hashtag_nm_ft VARCHAR(100) CHARACTER SET utf8;
 DECLARE end_cursor BOOLEAN DEFAULT FALSE;
 DECLARE hashtag_cursor CURSOR FOR SELECT hashtag FROM restaurant_hashtag as rs_hs JOIN (SELECT hashtag_idx FROM restaurant_connect WHERE restaurant_idx = in_idx) as rs_cn ON rs_hs.idx = rs_cn.hashtag_idx;
 DECLARE CONTINUE HANDLER FOR NOT FOUND SET end_cursor = TRUE;
 OPEN hashtag_cursor;
 cursor_loop: LOOP
  FETCH hashtag_cursor INTO hashtag_nm_ft;
  IF end_cursor THEN
   LEAVE cursor_loop;
  END IF;
  SET hashtag_nm = CONCAT(hashtag_nm, '#', hashtag_nm_ft, ' ');
 END LOOP cursor_loop;
 CLOSE hashtag_cursor;
 SELECT rs.idx, title, content, score, view_count, poster, address, old_address, phone_number, food_type, price, parking, business_time, menu, site, good, soso, bad, TRIM(hashtag_nm) as hashtag 
 FROM restaurant as rs JOIN restaurant_detail as rs_de 
 ON rs.idx = rs_de.restaurant_idx 
 WHERE rs.idx = in_idx;
END $$
DELIMITER ;

--레스토랑 평가 저장
DELIMITER $$
CREATE PROCEDURE IF NOT EXISTS restaurant_evaluation_insert(
 IN in_restaurant_idx INT,
 IN in_evaluation VARCHAR(10) CHARACTER SET utf8,
 IN in_member_idx INT
)
BEGIN
 DECLARE count_check INT;
 SET count_check = (SELECT COUNT(*) FROM restaurant_evaluation WHERE restaurant_idx = in_restaurant_idx AND member_idx = in_member_idx);
 IF count_check > 0 THEN
  SET count_check = (SELECT COUNT(*) FROM restaurant_evaluation WHERE restaurant_idx = in_restaurant_idx AND evaluation = in_evaluation AND member_idx = in_member_idx);
  IF count_check > 0 THEN
   DELETE FROM restaurant_evaluation 
   WHERE restaurant_idx = in_restaurant_idx AND member_idx = in_member_idx;
  ELSE
   UPDATE restaurant_evaluation 
   SET evaluation = in_evaluation 
   WHERE restaurant_idx = in_restaurant_idx AND member_idx = in_member_idx;
  END IF;
 ELSE
  INSERT INTO restaurant_evaluation 
  VALUES(in_restaurant_idx, in_evaluation, in_member_idx);
 END IF;
END $$
DELIMITER ;

--레시피 데이터 저장
DELIMITER $$
CREATE PROCEDURE IF NOT EXISTS recipe_insert(
 IN in_title VARCHAR(100) CHARACTER SET utf8, 
 IN in_writer VARCHAR(50) CHARACTER SET utf8, 
 IN in_view_count INT, 
 IN in_poster VARCHAR(1000) CHARACTER SET utf8, 
 IN in_hashtag VARCHAR(500) CHARACTER SET utf8, 
 IN in_content TEXT CHARACTER SET utf8, 
 IN in_amount VARCHAR(50) CHARACTER SET utf8, 
 IN in_time VARCHAR(50) CHARACTER SET utf8, 
 IN in_difficulty VARCHAR(50) CHARACTER SET utf8, 
 IN in_cookingOrder TEXT CHARACTER SET utf8, 
 IN in_tip VARCHAR(400) CHARACTER SET utf8
)
BEGIN
 DECLARE hashtag_nm VARCHAR(100) CHARACTER SET utf8;
 DECLARE recipe_idx INT;
 DECLARE hashtag_idx INT;
 DECLARE count_check INT;
 START TRANSACTION;
 INSERT INTO recipe 
 VALUES(NULL, in_title, in_writer, in_view_count, in_poster);
 SET recipe_idx = (SELECT LAST_INSERT_ID());
 INSERT INTO recipe_detail 
 VALUES(NULL, recipe_idx, in_content, in_amount, in_time, in_difficulty, in_cookingOrder, in_tip);
 WHILE (SELECT in_hashtag LIKE '%#%') DO 
  SET in_hashtag = TRIM(SUBSTRING(in_hashtag, INSTR(in_hashtag, '#')+1, 500));
  IF (SELECT in_hashtag LIKE '%#%') THEN
   SET hashtag_nm = TRIM(SUBSTRING_INDEX(in_hashtag, '#', 1));
  ELSE
   SET hashtag_nm = in_hashtag;
  END IF;
  IF CHAR_LENGTH(hashtag_nm) > 0 THEN
   SET count_check = (SELECT COUNT(*) FROM recipe_hashtag WHERE hashtag = hashtag_nm);
   IF count_check > 0 THEN
    SET hashtag_idx = (SELECT idx FROM recipe_hashtag WHERE hashtag = hashtag_nm);
   ELSE
    INSERT INTO recipe_hashtag VALUES(NULL, hashtag_nm);
    SET hashtag_idx = (SELECT LAST_INSERT_ID());
   END IF;
   INSERT INTO recipe_connect VALUES(recipe_idx, hashtag_idx);
  END IF;
 END WHILE;
 COMMIT;
END $$
DELIMITER ;

--레시피 리스트 카운트
DELIMITER $$
CREATE PROCEDURE IF NOT EXISTS recipe_list_count(
 IN in_keyword VARCHAR(100) CHARACTER SET utf8,
 OUT count INT
)
BEGIN
 IF INSTR(in_keyword, '#') > 0 THEN
  SET in_keyword = TRIM(SUBSTRING(in_keyword, 2, 100));
  SELECT COUNT(*) INTO count 
  FROM recipe as rc JOIN (SELECT DISTINCT rc_cn.recipe_idx as idx FROM recipe_hashtag as rc_ht JOIN recipe_connect as rc_cn ON rc_ht.idx = rc_cn.hashtag_idx WHERE rc_ht.hashtag = in_keyword) as rc_gr 
  ON rc.idx = rc_gr.idx;
 ELSE
  SELECT COUNT(*) INTO count 
  FROM recipe 
  WHERE title LIKE CONCAT('%', in_keyword, '%') OR writer like CONCAT('%', in_keyword, '%');
 END IF;
END $$
DELIMITER ;

--레시피 리스트
DELIMITER $$
CREATE PROCEDURE IF NOT EXISTS recipe_list_select(
 IN in_keyword VARCHAR(100) CHARACTER SET utf8,
 IN in_min INT,
 IN in_max INT
)
BEGIN
 IF INSTR(in_keyword, '#') > 0 THEN
  SET in_keyword = TRIM(SUBSTRING(in_keyword, 2, 100));
  SELECT rc.idx, title, writer, view_count, poster 
  FROM recipe as rc JOIN (SELECT DISTINCT rc_cn.recipe_idx as idx FROM recipe_hashtag as rc_ht JOIN recipe_connect as rc_cn ON rc_ht.idx = rc_cn.hashtag_idx WHERE rc_ht.hashtag = in_keyword) as rc_gr 
  ON rc.idx = rc_gr.idx 
  ORDER BY view_count DESC 
  LIMIT in_min, in_max;
 ELSE
  SELECT idx, title, writer, view_count, poster 
  FROM recipe 
  WHERE title LIKE CONCAT('%', in_keyword, '%') OR writer like CONCAT('%', in_keyword, '%') 
  ORDER BY view_count DESC 
  LIMIT in_min, in_max;
 END IF;
END $$
DELIMITER ;

--레시피
DELIMITER $$
CREATE PROCEDURE IF NOT EXISTS recipe_select(
 IN in_idx INT
)
BEGIN
 DECLARE hashtag_nm VARCHAR(500) CHARACTER SET utf8 DEFAULT '';
 DECLARE hashtag_nm_ft VARCHAR(100) CHARACTER SET utf8;
 DECLARE end_cursor BOOLEAN DEFAULT FALSE;
 DECLARE hashtag_cursor CURSOR FOR SELECT hashtag FROM recipe_hashtag as rc_hs JOIN (SELECT hashtag_idx FROM recipe_connect WHERE recipe_idx = in_idx) as rc_cn ON rc_hs.idx = rc_cn.hashtag_idx;
 DECLARE CONTINUE HANDLER FOR NOT FOUND SET end_cursor = TRUE;
 OPEN hashtag_cursor;
 cursor_loop: LOOP
  FETCH hashtag_cursor INTO hashtag_nm_ft;
  IF end_cursor THEN
   LEAVE cursor_loop;
  END IF;
  SET hashtag_nm = CONCAT(hashtag_nm, '#', hashtag_nm_ft, ' ');
 END LOOP cursor_loop;
 CLOSE hashtag_cursor;
 SELECT rc.idx, title, writer, view_count, poster, content, amount, time, difficulty, cookingOrder, tip, TRIM(hashtag_nm) as hashtag 
 FROM recipe as rc JOIN recipe_detail as rc_de 
 ON rc.idx = rc_de.recipe_idx 
 WHERE rc.idx = in_idx;
END $$
DELIMITER ;

--레시피 평가 저장
DELIMITER $$
CREATE PROCEDURE IF NOT EXISTS recipe_evaluation_insert(
 IN in_recipe_idx INT,
 IN in_evaluation VARCHAR(10) CHARACTER SET utf8,
 IN in_member_idx INT
)
BEGIN
 DECLARE count_check INT;
 SET count_check = (SELECT COUNT(*) FROM recipe_evaluation WHERE recipe_idx = in_recipe_idx AND member_idx = in_member_idx);
 IF count_check > 0 THEN
  SET count_check = (SELECT COUNT(*) FROM recipe_evaluation WHERE recipe_idx = in_recipe_idx AND evaluation = in_evaluation AND member_idx = in_member_idx);
  IF count_check > 0 THEN
   DELETE FROM recipe_evaluation 
   WHERE recipe_idx = in_recipe_idx AND member_idx = in_member_idx;
  ELSE
   UPDATE recipe_evaluation 
   SET evaluation = in_evaluation 
   WHERE recipe_idx = in_recipe_idx AND member_idx = in_member_idx;
  END IF;
 ELSE
  INSERT INTO recipe_evaluation 
  VALUES(in_recipe_idx, in_evaluation, in_member_idx);
 END IF;
END $$
DELIMITER ;

--서울 명소 데이터 저장
DELIMITER $$
CREATE PROCEDURE IF NOT EXISTS seoul_attractions_insert(
 IN in_type VARCHAR(30) CHARACTER SET utf8, 
 IN in_title VARCHAR(200) CHARACTER SET utf8, 
 IN in_content TEXT CHARACTER SET utf8, 
 IN in_score DECIMAL(2,1), 
 IN in_view_count INT, 
 IN in_poster VARCHAR(500) CHARACTER SET utf8, 
 IN in_address VARCHAR(70) CHARACTER SET utf8, 
 IN in_phone_number VARCHAR(30) CHARACTER SET utf8, 
 IN in_business_time VARCHAR(300) CHARACTER SET utf8, 
 IN in_business_week VARCHAR(100) CHARACTER SET utf8, 
 IN in_site VARCHAR(300) CHARACTER SET utf8, 
 IN in_tip VARCHAR(500) CHARACTER SET utf8
)
BEGIN
 DECLARE seoul_attractions_idx INT;
 START TRANSACTION;
 INSERT INTO seoul_attractions 
 VALUES(NULL, in_type, in_title, in_content, in_score, in_view_count, in_poster, in_address);
 SET seoul_attractions_idx = (SELECT LAST_INSERT_ID());
 INSERT INTO seoul_attractions_detail 
 VALUES(NULL, seoul_attractions_idx, in_phone_number, in_business_time, in_business_week, in_site, in_tip);
 COMMIT;
END $$
DELIMITER ;

--게임 데이터
INSERT INTO game VALUES(1, 'Dash Adventure', '2015년도에 출시한 귀여운 아케이드 게임!!', '/games/DashAdventure');
INSERT INTO game VALUES(2, 'Change Color', '색상을 바꿔 블럭들을 통과하는 캐주얼 게임!!', '/games/ChangeColor1');
INSERT INTO game VALUES(3, 'Change Color', '테스트용', '/games/ChangeColor2');
INSERT INTO game VALUES(4, 'Connect', '타이밍을 맞춰 선을 연결하는 캐주얼 게임!!', '/games/Connect');
INSERT INTO game VALUES(5, 'Cross Point', '상,하,좌,우 2개 이상의 같은 모형을 맞추는 간단한 캐주얼 게임 게임!!!', '/games/CrossPoint');
INSERT INTO game VALUES(6, 'Laser Ball', '떠다니는 모형을 맞추는 캐주얼 게임!!', '/games/LaserBall');
INSERT INTO game VALUES(7, 'Pop The SimSImi', '타이밍을 맞추는 간단한 캐주얼 게임!!!', '/games/PopTheSimSImi');
INSERT INTO game VALUES(8, 'ZigZag Jump', '색상을 바꿔 이동하는 캐주얼 게임!!', '/games/ZigZagJump');
INSERT INTO game VALUES(9, 'ZigZag Stair', '색상을 바꿔 계단을 올라가는 캐주얼 게임!!', '/games/ZigZagStair');

--랭킹 Top30
DELIMITER $$
CREATE PROCEDURE IF NOT EXISTS game_ranking_top30(
 IN in_game_idx INT
)
BEGIN
 SELECT (SELECT COUNT(*)+1 FROM game_ranking WHERE (score > my.score OR (score = my.score AND ranking_time < my.ranking_time)) AND game_idx = in_game_idx) AS rank, nickname, score, ranking_time 
 FROM game_ranking AS my JOIN member 
 ON my.member_idx = member.idx 
 WHERE game_idx = in_game_idx 
 ORDER BY rank 
 LIMIT 30;
END $$
DELIMITER ;

--나의 랭킹 TOP30
DELIMITER $$
CREATE PROCEDURE IF NOT EXISTS game_ranking_my30(
 IN in_game_idx INT,
 IN in_member_idx INT
)
BEGIN
 DECLARE ranking_count INT;
 DECLARE my_ranking INT;
 SET ranking_count = (SELECT COUNT(*) FROM game_ranking WHERE game_idx = in_game_idx);
 SET my_ranking = (SELECT (SELECT COUNT(*)+1 FROM game_ranking WHERE (score > my.score OR (score = my.score AND ranking_time < my.ranking_time)) AND game_idx = in_game_idx) AS rank FROM game_ranking AS my WHERE member_idx=in_member_idx AND game_idx = in_game_idx);
 CASE
 WHEN ranking_count <= 30 OR my_ranking <= 15 THEN
  SELECT (SELECT COUNT(*)+1 FROM game_ranking WHERE (score > my.score OR (score = my.score AND ranking_time < my.ranking_time)) AND game_idx = in_game_idx) AS rank, nickname, score, ranking_time 
  FROM game_ranking AS my JOIN member 
  ON my.member_idx = member.idx 
  WHERE game_idx = in_game_idx 
  ORDER BY rank 
  LIMIT 30;
 WHEN (ranking_count - my_ranking) <= 15 THEN
  SELECT * 
  FROM (SELECT (SELECT COUNT(*)+1 FROM game_ranking WHERE (score > my.score OR (score = my.score AND ranking_time < my.ranking_time)) AND game_idx = in_game_idx) AS rank, nickname, score, ranking_time FROM game_ranking AS my JOIN member ON my.member_idx = member.idx WHERE game_idx = in_game_idx ORDER BY rank DESC LIMIT 30) sort 
  ORDER BY rank;
 ELSE
  SELECT (SELECT COUNT(*)+1 FROM game_ranking WHERE (score > my.score OR (score = my.score AND ranking_time < my.ranking_time)) AND game_idx = in_game_idx) AS rank, nickname, score, ranking_time 
  FROM game_ranking AS my JOIN member 
  ON my.member_idx = member.idx 
  WHERE game_idx = in_game_idx 
  ORDER BY rank 
  LIMIT my_ranking, 30;
 END CASE;
END $$
DELIMITER ;

--랭킹 점수 추가
DELIMITER $$
CREATE PROCEDURE IF NOT EXISTS game_ranking_insert(
 IN in_game_idx INT,
 IN in_member_idx INT,
 IN in_score INT
)
BEGIN
 DECLARE count_check INT;
 SET count_check = (SELECT COUNT(*) FROM game_ranking WHERE game_idx = in_game_idx AND member_idx = in_member_idx);
 IF count_check > 0 THEN
  UPDATE game_ranking 
  SET score = in_score, ranking_time = NOW() 
  WHERE game_idx = in_game_idx AND member_idx = in_member_idx;
 ELSE
  INSERT INTO game_ranking 
  VALUES(NULL, in_game_idx, in_member_idx, in_score, NOW());
 END IF;
END $$
DELIMITER ;

--대쉬 어드벤쳐 데이터 저장
DELIMITER $$
CREATE PROCEDURE IF NOT EXISTS game_dash_adventure_insert(
 IN in_member_idx INT,
 IN in_dia INT,
 IN in_ball INT,
 IN in_shop_ball VARCHAR(111) CHARACTER SET utf8,
 IN in_normal_stage INT,
 IN in_normal_stage_count INT,
 IN in_hard_stage_open INT,
 IN in_hard_stage INT,
 IN in_hard_stage_count INT
)
BEGIN
 DECLARE count_check INT;
 SET count_check = (SELECT COUNT(*) FROM game_dash_adventure WHERE member_idx = in_member_idx);
 IF count_check > 0 THEN
  UPDATE game_dash_adventure 
  SET dia = in_dia, ball = in_ball, shop_ball = in_shop_ball, normal_stage = in_normal_stage, normal_stage_count = in_normal_stage_count, hard_stage_open = in_hard_stage_open, hard_stage = in_hard_stage, hard_stage_count = in_hard_stage_count WHERE member_idx = in_member_idx;
 ELSE
  INSERT INTO game_dash_adventure 
  VALUES(in_member_idx, in_dia, in_ball, in_shop_ball, in_normal_stage, in_normal_stage_count, in_hard_stage_open, in_hard_stage, in_hard_stage_count);
 END IF;
END $$
DELIMITER ;

--멤버 생성, 삭제 트리거(게임 랭킹)
DELIMITER $$
CREATE TRIGGER ranking_trigger
AFTER INSERT ON member
FOR EACH ROW
BEGIN
 DECLARE count INT;
 SET count=2;
 myloop: LOOP
  CALL game_ranking_insert(count, NEW.idx, 0);
  SET count = count+1;
  IF count = 10 THEN
   LEAVE myloop;
  END IF;
 END LOOP myloop;
END $$
DELIMITER ;

DELIMITER $$
CREATE TRIGGER ranking_delete_trigger
AFTER DELETE ON member
FOR EACH ROW
BEGIN
 DECLARE count INT;
 SET count=2;
 myloop: LOOP
  DELETE FROM game_ranking WHERE game_idx = count AND idx = OLD.idx;
  SET count = count+1;
  IF count = 10 THEN
   LEAVE myloop;
  END IF;
 END LOOP myloop;
END $$
DELIMITER ;
*/



/*
--연습
DELIMITER $$
CREATE FUNCTION IF EXISTS get_age(bYear INT)
  RETURNS INT
BEGIN
  DECLARE age INT;
  SET age = YEAR(CURDATE()) - bYear;
  RETURN age;
END $$
DELIMITER ;

DROP FUNCTION IF EXISTS get_age;

SELECT get_age(1979); - function
call get_age(1979); - procedure


DELIMITER $$
START TRANSACTION; --SET AUTOCOMMIT=0;
BEGIN TEST
 DECLARE @num INT;
 SET @num = 1;
 INSERT Scores VALUES (@num, 10, 80, GETDATE());
 IF NOT EXISTS (SELECT * FROM STAT WHERE num=@num)
  ROLLBACK TEST;
  SELECT 'ERROR : No STAT data';
 ELSE
  UPDATE Stat SET Total=Total+80;
  COMMIT TEST;
 END IF;
END $$
--SET AUTOCOMMIT=1;
DELIMITER ;

--프로시저 전부 삭제
DELETE FROM mysql.proc WHERE db = 'test' AND type = 'PROCEDURE';

DROP TRIGGER IF EXISTS ranking_trigger;
DROP TRIGGER IF EXISTS ranking_delete_trigger;
*/