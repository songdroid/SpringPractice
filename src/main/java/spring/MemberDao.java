package spring;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

public class MemberDao {
	private	static long nextId = 0;
	private JdbcTemplate jdbcTemplate;
	
	public MemberDao(JdbcTemplate jdbcTemplate){
		this.jdbcTemplate = jdbcTemplate;
	}
	
	public Member selectByEmail(String email){
		String sql = "select * from MEMBER where EMAIL=?";
		List<Member> results = 
				jdbcTemplate.query(sql, new RowMapperImpl(), email);
		
		return results.isEmpty() ? null : results.get(0);
	}
	
	class RowMapperImpl implements RowMapper{
		public Object mapRow(ResultSet rs, int arg1) throws SQLException {
			Member member = new Member(rs.getString("EMAIL"), 
					rs.getString("PASSWORD"), rs.getString("NAME"),
					rs.getTimestamp("REGDATE"));
			member.setId(rs.getLong("ID"));
			
			return member;
		}
	}

	public void insert(final Member member){
		KeyHolder keyHolder = new GeneratedKeyHolder();
		
		jdbcTemplate.update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				String sql = "insert into MEMBER(EMAIL, PASSWORD, NAME, REGDATE) values(?, ?, ?, ?)";
				PreparedStatement pstmt = con.prepareStatement(sql, new String[]{"ID"});
				pstmt.setString(1, member.getEmail());
				pstmt.setString(2, member.getPassword());
				pstmt.setString(3, member.getName());
				pstmt.setTimestamp(4, new Timestamp(member.getRegisterDate().getTime()));
				return pstmt;
			}
		}, keyHolder);
		
		Number keyValue = keyHolder.getKey();
		member.setId(keyValue.longValue());
	}
	
	public void update(Member member){
		//map.put(member.getEmail(), member);
	}
	
	public Collection<Member> selectAll(){
		String sql = "select * from MEMBER";
		List<Member> results = 
				jdbcTemplate.query(sql, new RowMapperImpl());
		
		return results;
	}
}
