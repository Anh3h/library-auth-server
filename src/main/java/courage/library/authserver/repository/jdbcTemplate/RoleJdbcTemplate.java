package courage.library.authserver.repository.jdbcTemplate;

import courage.library.authserver.dao.RoleEntity;
import courage.library.authserver.dto.Role;
import courage.library.authserver.exception.BadRequestException;
import courage.library.authserver.service.mapper.RoleMappper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class RoleJdbcTemplate {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void deleteEntity(String id) {
        String sql = "UPDATE role SET is_available = false WHERE id = ? ";

        try {
            this.jdbcTemplate.update(sql, new Object[] {id});
        } catch (DataAccessException ex) {
            throw BadRequestException.create(ex.getMessage());
        }
    }
}
