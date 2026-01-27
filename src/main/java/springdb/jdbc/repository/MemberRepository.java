package springdb.jdbc.repository;

import springdb.jdbc.domain.Member;

public interface MemberRepository {
    Member save(Member member);
    Member findById(String id);
    void update(String id, int money);
    void delete(String id);
}
