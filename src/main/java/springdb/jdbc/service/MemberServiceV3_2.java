package springdb.jdbc.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;
import springdb.jdbc.domain.Member;
import springdb.jdbc.repository.MemberRepositoryV3;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * 트랜잭션 - 트랜잭션 템플릿
 */
@Slf4j
public class MemberServiceV3_2 {

    private final TransactionTemplate transactionTemplate;
    private final MemberRepositoryV3 repository;

    public MemberServiceV3_2(PlatformTransactionManager transactionManager, MemberRepositoryV3 repository) {
        this.transactionTemplate = new TransactionTemplate(transactionManager);
        this.repository = repository;
    }

    public void accountTransfer(String fromId, String toId, int money) throws SQLException {
        transactionTemplate.executeWithoutResult((status) -> {
            try {
                bizLogic(fromId, toId, money);
            } catch (SQLException e) {
                throw new IllegalStateException(e);
            }
        });
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
