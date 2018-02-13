package courage.library.authserver.repository.jdbcTemplate;

public interface CustomRepository<T> {

    Object updateEntity( T object );
    void deleteEntity( String uuid );
    void restoreEntity(String uuid);

}
