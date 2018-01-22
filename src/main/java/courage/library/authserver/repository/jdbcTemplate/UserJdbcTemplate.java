package courage.library.authserver.repository.jdbcTemplate;

import courage.library.authserver.exception.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserJdbcTemplate {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void deleteEntity( String uuid ) {
        String sql = "UPDATE user SET is_available = false WHERE uuid = ?";
        try {
            this.jdbcTemplate.update(sql, new Object[] {uuid});
        } catch (DataAccessException ex) {
            throw BadRequestException.create(ex.getMessage());
        }
    }

}
