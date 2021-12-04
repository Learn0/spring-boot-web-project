# 공부용 웹사이트
[![Build Status](https://travis-ci.org/joemccann/dillinger.svg?branch=master)](https://ourshopping.shop)
[웹사이트 바로가기](https://ourshopping.shop)

[REST API 확인하기](http://ourshopping.shop/swagger-ui/index.html)

[개인정보처리방침](http://ourshopping.shop/personalInformation)

[서비스 약관](http://ourshopping.shop/serviceTerms)

---

## 설명
> 게시판, 영화 순위, 음식점 찾기, 미니 게임 등
> 다양한 기능이 존재함

---

## 프로그래밍 언어
- JAVA
- HTML
- Javascript
- PHP
- C#
- SQL

---

## 마크업 언어
- HTML
- CSS
- XML

---

## 프레임 워크
| 프레임워크 | 사용하는 곳 |
| ------ | ------ |
| Spring(Boot, Security) | 전체적으로 사용 |
| Mybatis | 게시판 및 댓글 부분 사용 |
| Bootstrap | 전체적으로 사용 |

---

## 템플릿 엔진
| 템플릿 엔진 | 사용하는 곳 |
| ------ | ------ |
| Jsp | 전체적으로 사용 |
| Thymeleaf | 전체적으로 사용 |

---

## 라이브러리
| 라이브러리 | 사용하는 곳 |
| ------ | ------ |
| Jquery | 전체적으로 사용 |
| Thymeleaf | HTML에 사용 |
| JSTL(EL) | JSP에서 <% %>대신 사용 |
| Tiles | JSP에서 인클루드 대신 사용 |
| VueJS | 음악 랭킹, 뉴스 |
| ReactJS | 게임목록, 영화목록 |
| HikariCP | DBCP로 사용(DBCP라이브러리 중에서 성능이 좋고 안정적) |
| JPA | 회원관리 부분 일부 사용 |
| Jsoup | HTML 데이터 파싱으로 사용 |
| Apache Commons | 파일 업로드 및 다운로드 |
| Json(~Simple Json~, Gson, Jackson) | json 파싱 및 ajax(HashMap => json)에 사용 |
| Xml(DOM, JAXB) | json 파싱 및 ajax(HashMap => json)에 사용 |
| Lombok | Getter, Setter, AllArgsConstructor를 사용한 간소화 |
| Stomp | 채팅 그룹 분리 |
| log4jdbc | SQL로그 확인 |

---

## 개발 환경
- Window10
- Eclipse IDE
- WebStorm -> VSCode
- OracleDB -> MariaDB
- NodeJS

---

## 배포 환경 
- Oracle VM VirtualBox(Ubuntu)
- Apache2
- Tomcat9
- MariaDB
- 가비아 도메인 사용
- NodeJS

> ### 우분투 환경
> 1. 방화벽 설치
> > -	sudo apt-get install ufw
> > -	sudo ufw allow Apache Full (80(http), 443(https))
> > -	sudo ufw allow 3000(nodejs)
> 2. 자바 설치
> > -	sudo apt-get install openjdk-16-jdk
> > -	환경변수 설정
> > -	sudo vi ~/.bashrc
> > -	export JAVA_HOME=/usr/lib/jvm/java> > -16-openjdk-amd64
> > -	export PATH="$PATH:$JAVA_HOME/bin"
> 3. 아파치 설치
> > -	sudo apt-get install apache2
> 4. PHP 설치
> > -	sudo apt-get install php
> 5. 톰캣 설치
> > -	sudo apt-get install tomcat9
> 6. 아파치-톰캣 연동 모듈
> > -	sudo apt-get install libapache2-mod-jk
> > -	libapache2 적용 안될 경우
> > -	/etc/apache2/mods-available/httpd-jk.conf를 jk.conf로 변경
> > -	sudo a2enmod jk.conf(적용 안될 경우 직접 심볼릭 링크를 직접 생성)
> > -	/etc/libapache2-mod-jk/workers.properties 수정
> > -	/etc/apache2/sites-available/*.conf 수정
> > -	/etc/tomcat9/server.xml 수정
> 7. 마리아DB 설치
> > -	sudo apt-get install mariadb-server
> 8. HTTPS(SSL) 인증서 발급 설치
> > -	sudo apt-get install letsencrypt
> > -	sudo apt-get install certbot python3-certbot-apache
> > -	가비아 도메인 등록
> > -	sudo certbot --apache
> > -	/etc/tomcat9/server.xml 수정
> 9. 톰캣 파일 업로드 설정
> > -	upload폴더 생성 후 소유자를 tomcat으로 변경
> > -	sudo chown tomcat:tomcat upload -R
> > -	sudo vi /etc/systemd/system/tomcat9.service
> > -	ReadWritePaths=/var/www/html/upload/
> 10. NodeJS 설치
> > -	sudo apt-get install curl
> > -	sudo apt-get install -y nodejs
> > -	sudo apt-get install npm
> 11. VScode 설치
> > -	sudo sh -c 'curl https://packages.microsoft.com/keys/microsoft.asc | gpg --dearmor > /etc/apt/trusted.gpg.d/microsoft.gpg'
> > -	sudo sh -c 'echo "deb [arch=amd64] https://packages.microsoft.com/repos/vscode stable main" > /etc/apt/sources.list.d/vscode.list'
> > -	sudo apt-get update
> > -	sudo apt-get install code
> 12. React 설치
> > -	sudo npm install -g create-react-app
> > -	sudo create-react-app [프로젝트 이름]
> > -	sudo npm install -g express
> > -	sudo npm install -g express-generator
> > -	sudo express [프로젝트 이름]

> ### SSL 인증 관련 오류 해결방법
> > -	와이파이 포트 포워딩 설정 및 와이파이 설정을 개인으로 변경
> > -	Oracle VM VirtualBox 포트 포워딩 설정
> > -	ufw allow 포트 설정
> > -	방화벽 인바운드 새 규칙 설정
> > -	sites-available에서 https 자동 이동하는 부분 주석처리

	RewriteEngine on  
	RewriteCond %{SERVER_NAME} =ourshopping.shop  
	RewriteRule ^ https://%{SERVER_NAME}%{REQUEST_URI} [END,NE,R=permanent]
> > -	sites-available에서 인증 URI는 톰캣으로 전송하지 않도록 설정

	SetEnvIf Request_URI "/.well-known/*" no-jk
> > -	/var/www/html/.well-known/acme-challenge 폴더 생성
> > -	sudo apt update, sudo apt upgrade

---

> ### WebSocket을 사용하기 위한 설정
> > -	sudo a2enmod proxy
> > -	sudo a2enmod proxy_http
> > -	sudo a2enmod proxy_wstunnel
> > -	sudo vi /etc/apache2/sites-available/.conf에서 아래 코드 추가  

	RewriteCond %{HTTP:Connection} Upgrade [NC]  
	RewriteCond %{HTTP:Upgrade} websocket [NC]  
	RewriteRule /(.*) ws://localhost:8080/$1 [P,L]
> > -	sudo vi /etc/tomcat9/server.xml 아래 코드 추가(주석 해제)

	<Connector port="8080" protocol="HTTP/1.1"  
               connectionTimeout="20000"  
               redirectPort="8443" />
> > -	sudo systemctl restart apache2

---

> ### NodeJS를 연동하기 위한 설정
> > -	NodeJS 설정

	<Location /nodejs>  
		ProxyPass http://localhost:3000  
		ProxyPassReverse http://localhost:3000  
	</Location>  
	<Location /static>  
		ProxyPass http://localhost:3000/static  
		ProxyPassReverse http://localhost:3000/static  
	</Location>
> > -	NodeJS Websocket 설정

	RewriteCond %{REQUEST_URI} =/sockjs-node  
	RewriteRule  /(.*) ws://localhost:3000/$1 [P,L]


---

> ### MariaDB SSL 외부 접속
https://ddart.net/xe/board/12867

---

[참조한 WebSocket 블로그](https://dev-gorany.tistory.com/212?category=901854, "")

[참조한 템플릿](https://ourshopping.shop, "")