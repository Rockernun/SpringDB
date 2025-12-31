package springdb.jdbc.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.NoSuchElementException;
import lombok.extern.slf4j.Slf4j;
import springdb.jdbc.connection.DbConnectionUtil;
import springdb.jdbc.domain.Member;

/**
 * JDBC DriverManager 사용
 */
@Slf4j
public class MemberRepositoryV0 {
    
    public Member save(Member member) throws SQLException {
        String sql = "INSERT INTO member (member_id, money) VALUES (?, ?)";
        
        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, member.getMemberId());
            pstmt.setInt(2, member.getMoney());
            pstmt.executeUpdate();
            return member;
        } catch (SQLException e) {
            log.error("DB 에러: ", e);
            throw e;
        } finally {
            close(con, pstmt, null);
        }
    }

    public Member findById(String memberId) throws SQLException {
        String sql = "SELECT * FROM member WHERE member_id = ?";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, memberId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                Member member = new Member();
                member.setMemberId(rs.getString("member_id"));
                member.setMoney(rs.getInt("money"));
                return member;
            } else {
                throw new NoSuchElementException("Member not found memberId = " + memberId);
            }
        } catch (SQLException e) {
            log.error("DB ERROR", e);
            throw e;
        } finally {
            close(con, pstmt, rs);
        }
    }

    private void close(Connection con, Statement stmt, ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                log.info("ERROR", e);
            }
        }

        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                log.info("ERROR", e);
            }
        }

        if (con != null) {
            try {
                con.close();
            } catch (SQLException e) {
                log.info("ERROR", e);
            }
        }
    }

    private static Connection getConnection() {
        return DbConnectionUtil.getConnection();
    }
}
