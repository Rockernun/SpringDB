package springdb.jdbc.repository;

import java.sql.SQLException;
import org.junit.jupiter.api.Test;
import springdb.jdbc.domain.Member;

class MemberRepositoryV0Test {

    MemberRepositoryV0 repository = new MemberRepositoryV0();

    @Test
    void crud() throws SQLException {
        Member member = new Member("V1", 10000);
        repository.save(member);
    }
}
