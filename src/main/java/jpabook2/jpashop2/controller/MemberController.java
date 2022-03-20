package jpabook2.jpashop2.controller;

import jpabook2.jpashop2.domain.Address;
import jpabook2.jpashop2.domain.Member;
import jpabook2.jpashop2.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/members/new")
    public String createForm(Model model) {
        model.addAttribute("memberForm", new MemberForm());
        return "members/createMemberForm";
    }

    @PostMapping("/members/new")
    public String create(@Valid MemberForm form, BindingResult result) {

        // BindingResult를 사용하게 되면, 에러가 BindingResult에 담긴다.
        // 에러가 발생했을 경우 BindingResult 객체에서 확인할 수 있다.
        // 스프링이 BindfingResult를 화면까지 끌고간다.
        if (result.hasErrors()) {
            return "/members/createMemberForm";

        }

        // form에서 입력 받은 데이터로 Address 객체를 완성한다
        Address address = new Address(form.getCity(), form.getStreet(), form.getZipcode());

        Member member = new Member();
        member.setName(form.getName());
        member.setAddress(address);

        memberService.join(member); // 저장
        return "redirect:/"; // 홈으로 리다이렉트 보낸다

    }

    @GetMapping("/members")
    public String list(Model model) {
        List<Member> members = memberService.findMembers();
        model.addAttribute("members", members); // Model에 전체 멤버 리스트를 담아서 화면에 넘긴다
        return "members/memberList";
    }

}
