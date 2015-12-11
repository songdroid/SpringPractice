package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import spring.AlreadyExistingMemberException;
import spring.ChangePasswordService;
import spring.IdPasswordNotMachingException;
import spring.MemberInfoPrinter;
import spring.MemberListPrinter;
import spring.MemberNotFoundException;
import spring.MemberRegisterService;
import spring.RegisterRequest;

public class MainForSpring {
	private static ApplicationContext ctx = null;
	
	public static void main(String[] args) throws IOException {
		ctx = new FileSystemXmlApplicationContext("classpath:appCtx.xml");
				
		BufferedReader reader =
				new BufferedReader(new InputStreamReader(System.in));
		while(true){
			System.out.println("명령어를 입력하세요");
			String command = reader.readLine();
			
			if(command.equalsIgnoreCase("exit")){
				System.out.println("종료 합니다.");
				break;
			}
			
			// 회원 가입
			if(command.startsWith("new ")){
				processNewCommand(command.split(" "));
				continue;
			}
			// 비밀번호 변경
			else if(command.startsWith("change ")){
				processChangeCommand(command.split(" "));
				continue;
			}
			// 모든 회원 정보 출력
			else if(command.startsWith("list")){
				processListCommand();
				continue;
			}
			// 지정한 회원 정보 출력
			else if(command.startsWith("info ")){
				processInfoCommand(command.split(" "));
				continue;
			}
			
			printHelp();
		}
	}
	
	private static void printHelp(){
		System.out.println();
		System.out.println("잘못된 명령입니다. 아래 명령어 사용법을 확인하세요");
		System.out.println("명령어 사용법 : ");
		System.out.println("new 이메일 이름 암호 암호확인");
		System.out.println("change 이메일 현재비번 변경비번");
		System.out.println();
	}
	
	private static void processNewCommand(String[] args){
		if(args.length != 5){
			printHelp();
			return;
		}
		
		MemberRegisterService regSvc = ctx.getBean("regSvc", MemberRegisterService.class);
		RegisterRequest req = new RegisterRequest();
		req.setEmail(args[1]);
		req.setName(args[2]);
		req.setPassword(args[3]);
		req.setConfirmPassword(args[4]);
		
		if(!req.isPasswordEqualToConfirmPassword()){
			System.out.println("암호와 확인이 일치하지 않습니다.\n");
			return;
		}
		
		try{
			regSvc.regist(req);
			System.out.println("등록 했습니다.");
		}
		catch(AlreadyExistingMemberException err){
			System.out.println("이미 존재하는 이메일 입니다.");
		}
	}
	
	private static void processChangeCommand(String[] args){
		if(args.length != 4){
			printHelp();
			return;
		}
		
		ChangePasswordService changePwdSvc = 
				ctx.getBean("pwdSvc",  ChangePasswordService.class);
		
		try{
			changePwdSvc.changePassword(args[1], args[2], args[3]);
			System.out.println("암호를 변경했습니다.\n");
		}
		catch(MemberNotFoundException err){
			System.out.println("존재하지 않는 이메일입니다.\n");
		}
		catch(IdPasswordNotMachingException err){
			System.out.println("이메일과 암호가 일치하지 않습니다.\n");
		}
	}
	
	private static void processListCommand(){
		MemberListPrinter listPrinter = ctx.getBean("listPrinter", MemberListPrinter.class);
		listPrinter.selectAll();
	}
	
	private static void processInfoCommand(String[] args){
		if(args.length != 2){
			printHelp();
			return;
		}
		
		MemberInfoPrinter  infoPrinter = ctx.getBean("infoPrinter",
				MemberInfoPrinter.class); 
		infoPrinter.printMemberInfo(args[1]);
	}
}
