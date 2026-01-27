package springdb.jdbc.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import springdb.jdbc.domain.Member;
import springdb.jdbc.repository.MemberRepository;
import springdb.jdbc.repository.MemberRepositoryV3;

import java.sql.SQLException;

/**
 * 예외 누수 문제 해결
 * SQLException 제거
 * MemberRepository 의존
 */
@Slf4j
@RequiredArgsConstructor
public class MemberServiceV4 {

    private final MemberRepository repository;

    @Transactional
    public void accountTransfer(String fromId, String toId, int money) {
        bizLogic(fromId, toId, money);
    }

    private void bizLogic(String fromId, String toId, int money) {
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
