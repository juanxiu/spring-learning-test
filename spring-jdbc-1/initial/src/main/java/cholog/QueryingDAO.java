package cholog;

import jakarta.annotation.Nullable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class QueryingDAO { // controller 가 아닌 DAO 객체에서 데이터베이스 다루기.
    private JdbcTemplate jdbcTemplate;

    public QueryingDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    //RowMapper 인터페이스. ResultSet의 각 행을 Customer 객체로 매핑하는 데 사용되며, 'resultSet' 에서 각 열의 값을 가져와 'Customer' 객체를 생성

    //람다식 -> RowMapper 인터페이스(클래스 구현)와 사용(객체 생성)을 편하게. (원래는 .. 다른 곳에서 클래스 만들어주고, 여기서 new 객체 생성해서 아래 조회할 때 객체 불러오기)
    //resultSet 은 db의 레코드를 가져옴. 컬럼명은 id, first_name, last_name. rowNum 은 여기서 쓰이진 않지만, 조회한 레코드의 번호를 매겨주는 것.

    private final RowMapper<Customer> actorRowMapper = (resultSet, rowNum) -> {
        Customer customer = new Customer(
                resultSet.getLong("id"), // id는 Long 타입으로 가져온다는 뜻.
                resultSet.getString("first_name"),
                resultSet.getString("last_name")
        );
        return customer;
    };


    //메서드 queryForObject는 SQL 쿼리를 실행하여 결과를 반환. 두 번째 매개변수로 필요한 반환 형식을 전달
    public <T> T queryForObject(String sql, Class<T> requiredType) {
        return jdbcTemplate.queryForObject(sql, requiredType);
    }

        //메서드 count는 'customers' 테이블에서 행의 수를 셈. 이를 위해 queryForObject 메서드를 사용하여 "select count(*) from customers" 쿼리를 실행하고 결과를 정수로 반환함.
    public int count() {
        //TODO : customers 디비에 포함되어있는 row가 몇개인지 확인하는 기능 구현
        int rowCount= jdbcTemplate.queryForObject("select count(*) from customers", Integer.class);
        return rowCount;
    }

    // 메서드 queryForObject는 SQL 쿼리를 실행하여 결과를 반환. SQL 쿼리(sql), 반환되는 값의 타입(requiredType), 필요한 경우 쿼리에 전달되는 인자(args)들을 인수로 받음.
    public <T> T queryForObject(String sql, Class<T> requiredType, @Nullable Object... args){
        return jdbcTemplate.queryForObject(sql, requiredType, args);
    }

    /*메서드 getLastName은 주어진 ID에 해당하는 고객의 성(lastName)을 검색. queryForObject 메서드를 호출하여 ID를 기반으로 한 SQL 쿼리를 실행하고 결과로부터 성(lastName)을 반환
    ?는 placeholder로, 나중에 ID 값으로 대체됨. String.class는 쿼리 결과가 문자열로 반환될 것. 메서드에 전달된 id 값이 해당 위치에 대체됨.*/
    public String getLastName(Long id) {
        //TODO : 주어진 Id에 해당하는 customers의 lastName을 반환
        String lastName = jdbcTemplate.queryForObject("select last_name from customers where id = ?", String.class, id);

        return lastName;
    }

    /**
     * public <T> T queryForObject(String sql, RowMapper<T> rowMapper, @Nullable Object... args)
     */

    //RowMapper 이용.
    public Customer findCustomerById(Long id) {
        //TODO : 주어진 Id에 해당하는 customer를 객체로 반환
        Customer customer = jdbcTemplate.queryForObject(
                "select id, first_name, last_name from customers where id = ?",
                actorRowMapper,
                id
        );
        return customer;

    }

    /**
     * public <T> List<T> query(String sql, RowMapper<T> rowMapper)
     */

    //RowMapper 이용.
    public List<Customer> findAllCustomers() {
        //TODO : 저장된 모든 Customers를 list형태로 반환

        // 첫 번째 매개변수는 sql문, 두 번째 매개변수는 actorRowMapper. (actorRowMapper 에서 sql문에서 찾고 있는 레코드 조회)
        List<Customer> customers = jdbcTemplate.query("select id, first_name, last_name from customers where first_name = ?",
                actorRowMapper);

        return customers;
    }

    /**
     * public <T> List<T> query(String sql, RowMapper<T> rowMapper, @Nullable Object... args)
     */

    //RowMapper 이용.
    public List<Customer> findCustomerByFirstName(String firstName) {
        //TODO : firstName을 기준으로 customer를 list형태로 반환
        List<Customer> customers = jdbcTemplate.query("select id, first_name, last_name from customers where first_name = ?",
                actorRowMapper,
                firstName);

        return customers;
    }

}
