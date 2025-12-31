package springdb.jdbc.connection;

import java.sql.Connection;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

@Slf4j
public class DbConnectionUtilTest {

    @Test
    void connection() {
        Connection connection = DbConnectionUtil.getConnection();
        Assertions.assertThat(connection).isNotNull();
    }
}
