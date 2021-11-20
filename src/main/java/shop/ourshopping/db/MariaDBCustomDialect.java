package shop.ourshopping.db;

import org.hibernate.dialect.MariaDBDialect;

// JPA로 생성되는 mariadb 테이블을 인코딩 하기 위해 사용
public class MariaDBCustomDialect extends MariaDBDialect{
	
	@Override
    public String getTableTypeString() {
        return " CHARSET = utf8";
    }
}