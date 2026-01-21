package springdb.jdbc.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import springdb.jdbc.domain.Member;
import springdb.jdbc.repository.MemberRepositoryV3;

import java.sql.SQLException;

/**
 * 트랜잭션 - @Transactional AOP
 */
@Slf4j
public class MemberServiceV3_3 {

    private final MemberRepositoryV3 repository;

    public MemberServiceV3_3(MemberRepositoryV3 repository) {
        this.repository = repository;
    }

    @Transactional
    public void accountTransfer(String fromId, String toId, int money) throws SQLException {
        bizLogic(fromId, toId, money);
    }

    private void bizLogic(String fromId, String toId, int money) throws SQLException {
        Member fromMember = repository.findById(fromId);
        Member toMember = repository.findById(toId);

        repository.update(fromId, fromMember.getMoney() - money);
        validateTransfer(toMember);
        repository.update(toId, toMember.getMoney() + money);
    }

    private static void validateTransfer(Member toMember) {
        if (toMember.getMemberId().equals("ex")) {
            throw new IllegalStateException("이체 중 오류 발생!");
        }
    }
}
