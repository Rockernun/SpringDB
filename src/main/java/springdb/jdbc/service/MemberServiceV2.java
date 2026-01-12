package springdb.jdbc.service;

import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import springdb.jdbc.domain.Member;
import springdb.jdbc.repository.MemberRepositoryV2;

/**
 * 트랜잭션 - 파라미터 연동, 풀을 고려한 연동
 */
@Slf4j
@RequiredArgsConstructor
public class MemberServiceV2 {

    private final DataSource dataSource;
    private final MemberRepositoryV2 repository;

    public void accountTransfer(String fromId, String toId, int money) throws SQLException {
        Connection con = dataSource.getConnection();
        try {
            con.setAutoCommit(false);  // 트랜잭션 시작
            bizLogic(con, fromId, toId, money);
            con.commit();  // 성공 시 커밋
        } catch (Exception e) {
            con.rollback();  // 실패 시 롤백
            throw new IllegalStateException(e);
        } finally {
            releaseConnection(con);
        }
    }

    private void bizLogic(Connection con, String fromId, String toId, int money) throws SQLException {
        Member fromMember = repository.findById(con, fromId);
        Member toMember = repository.findById(con, toId);

        repository.update(con, fromId, fromMember.getMoney() - money);
        validateTransfer(toMember);
        repository.update(con, toId, toMember.getMoney() + money);
    }

    private static void releaseConnection(Connection con) {
        if (con != null) {
            try {
                con.setAutoCommit(true);  // 커넥션 풀 고려
                con.close();
            } catch (Exception e) {
                log.info("error", e);
            }
        }
    }

    private static void validateTransfer(Member toMember) {
        if (toMember.getMemberId().equals("ex")) {
            throw new IllegalStateException("이체 중 오류 발생!");
        }
    }
}
