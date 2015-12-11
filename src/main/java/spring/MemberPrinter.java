package spring;

// 전체 회원을 조회하여 화면에 출력해주는 서비스
public class MemberPrinter {
	public void print(Member member){
		System.out.printf("회원 정보 :  아이디=%d, 이메일=%s, 등록일=%tF\n",
				member.getId(), member.getEmail(), member.getRegisterDate());
	}
}
