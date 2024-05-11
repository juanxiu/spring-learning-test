package cholog;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;

@Repository
public class UpdatingDAO {
    private JdbcTemplate jdbcTemplate;

    public UpdatingDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /*
    private final RowMapper<Customer> actorRowMapper = (resultSet, rowNum) -> {
        Customer customer = new Customer(
                resultSet.getLong("id"),
                resultSet.getString("first_name"),
                resultSet.getString("last_name")
        );
        return customer;
    };
    추후 rowMapper에 대해 학습해보고 이용해보기
    */

    /**
     * public int update(String sql, @Nullable Object... args)
     */
    public void insert(Customer customer) {
        //todo: customer를 디비에 저장하기
        String sql = "insert into customers (first_name, last_name) values (?, ?)";
        jdbcTemplate.update(sql, customer.getFirstName(), customer.getLastName());

    }
    /**
     * public int update(String sql, @Nullable Object... args)
     */
    public int delete(Long id) {
        //todo: id에 해당하는 customer를 지우고, 해당 쿼리에 영향받는 row 수반환하기
        String sql = "delete from customers where id = ?";
        return jdbcTemplate.update(sql, Long.valueOf(id));

    }

    /**
     * public int update(final PreparedStatementCreator psc, final KeyHolder generatedKeyHolder)
     */

    // insertWithKeyHolder 메소드: 데이터베이스에 새로운 레코드를 삽입하고, 삽입된 레코드의 primary key 값을 반환하기 위해 사용
    public Long insertWithKeyHolder(Customer customer) {
        String sql = "insert into customers (first_name, last_name) values (?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        //todo : keyHolder에 대해 학습하고, Customer를 저장후 저장된 Customer의 id를 반환하기


        jdbcTemplate.update(connection -> { // 'jdbcTemplate.update 메서드를 호출. 람다식을 통해 'Connecton' 객체를 얻고 쿼리 실행.
            PreparedStatement ps = connection.prepareStatement(  //Connection 객체를 통해 PreparedStatement 객체를 생성
                    "insert into customers (first_name, last_name) values (?, ?)",
                    new String[]{"id"}); //쿼리 실행 후 생성된 키를 얻기 위해 사용할 열. id열 지정.
            ps.setString(1, customer.getFirstName()); //첫 번째 ?에 customer 객체의 firstName 값을 바인딩
            ps.setString(2, customer.getLastName()); //두 번째 ?에 customer 객체의 lastName 값을 바인딩
            return ps;
        }, keyHolder); // 생성된 키를 keyHolder 에 저장.

        Long id = keyHolder.getKey().longValue(); //저장된 키를 얻기 위해 keyHolder에서 Key를 가져온 다음, 그 값을 Long 형태로 변환하여 id 변수에 저장


        return keyHolder.getKey().longValue(); //저장된 키를 반환. 새로 삽입된 고객의 id.
    }
}
