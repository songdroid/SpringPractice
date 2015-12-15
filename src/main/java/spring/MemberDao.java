package spring;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

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

	public void insert(Member member){
		member.setId(++nextId);
		//map.put(member.getEmail(), member);
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
