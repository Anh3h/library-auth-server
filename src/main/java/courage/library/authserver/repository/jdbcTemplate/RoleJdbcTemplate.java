package courage.library.authserver.repository.jdbcTemplate;

import courage.library.authserver.exception.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class RoleJdbcTemplate {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void deleteEntity(String id) {
        String sql = "UPDATE role SET enabled = false WHERE id = ? ";

        try {
            this.jdbcTemplate.update(sql, new Object[] {id});
        } catch (DataAccessException ex) {
            throw BadRequestException.create(ex.getMessage());
        }
    }
}
