package courage.library.authserver.repository.jdbcTemplate;


import courage.library.authserver.dao.LibraryEntity;
import courage.library.authserver.dto.Library;
import courage.library.authserver.exception.BadRequestException;
import courage.library.authserver.service.mapper.LibraryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class LibraryJdbcTemplate implements CustomRepository<LibraryEntity> {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Library updateEntity(LibraryEntity libraryEntity) {
        String sql = "UPDATE library SET name = ?, address = ?, logo = ? WHERE  " +
                "is_available = true AND uuid = ? ";

        Object[] parameters = { libraryEntity.getName(), libraryEntity.getAddress(), libraryEntity.getLogo(),
        libraryEntity.getUuid()};

        return (Library) this.sqlUpdate( LibraryMapper.getLibraryDTO(libraryEntity), sql, parameters);
    }

    @Override
    public void deleteEntity(String uuid) {
        String sql = "UPDATE library SET is_available = false WHERE uuid = ? ";
        this.sqlUpdate(null, sql, new Object[] {uuid});

    }

    Object sqlUpdate( Object returnValue, String sql, Object[] parameters ) {
        try {
            this.jdbcTemplate.update(sql, parameters);
            return returnValue;
        } catch (DataAccessException ex) {
            throw BadRequestException.create(ex.getMessage());
        }
    }
}
