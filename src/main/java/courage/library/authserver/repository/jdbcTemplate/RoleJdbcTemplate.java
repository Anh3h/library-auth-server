package courage.library.authserver.repository.jdbcTemplate;

import java.sql.ResultSet;
import java.sql.SQLException;

import courage.library.authserver.dao.RoleEntity;
import courage.library.authserver.dto.Role;
import courage.library.authserver.exception.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class RoleJdbcTemplate {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public RoleEntity findByName(String name) {
		String sql = "SELECT * FROM role WHERE name = ?";
		try {
			RoleEntity role = this.jdbcTemplate.queryForObject(sql, new Object[] { name }, new RoleMapper());
			return role;
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (DataAccessException ex) {
			throw BadRequestException.create(ex.getMessage());
		}

	}

	private static final class RoleMapper implements RowMapper<RoleEntity> {

		@Override
		public RoleEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
			RoleEntity role = new RoleEntity();
			role.setId(rs.getString("id"));
			role.setName(rs.getString("name"));

			return role;
		}
	}

}
