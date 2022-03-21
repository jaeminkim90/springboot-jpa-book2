package jpabook2.jpashop2.api;

import jpabook2.jpashop2.domain.Member;
import jpabook2.jpashop2.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController // 데이터는 JSON이나 XML로 바로 보내고자 할 때 쓰는 Annotation
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;

    /**
     * 회원 조회 v1 : 응답값으로 엔티티를 직접 JSON으로 전달하는 API
     */
    @GetMapping("/api/v1/members")
    public List<Member> memberV1() {
        return memberService.findMembers();
    }


    /**
     * 회원 조회 v2 : 응답값으로 엔티티가 아닌 별도의 DTO를 JSON으로 전달하는 API
     */
    @GetMapping("/api/v2/members")
    public Result memberV2() { // 껍데기 클래스로 Result를 만든다

        // 기존과 동일하게 전체 Member를 List로 받아온다
        List<Member> findMembers = memberService.findMembers();

        // stream을 이용해서 List<Member>를 List<MemberDTO>로 바꾼다
        List<MemberDTO> collect = findMembers.stream()
                .map(m -> new MemberDTO(m.getName()))
                .collect(Collectors.toList());

        // Result 객체로 한 번 더 감싸서 반환한다. JSON 배열 타입을 예방한다
        return new Result(collect.size(), collect);


    }

    @Data
    @AllArgsConstructor
    static class Result<T> {
        private int count;
        private T data;
    }

    @Data
    @AllArgsConstructor
    static class MemberDTO {
        private String name;
    }


    /**
     * 등록 V1: 요청 값으로 Member 엔티티를 직접 받는다.
     */
    @PostMapping("/api/v1/members")
    public CreateMemberResponse saveMemberV1(@RequestBody @Valid Member member) {
        Long id = memberService.join(member);

        return new CreateMemberResponse(id);
    }

    /**
     * 등록 V2: 요청 값으로 Member 엔티티 대신에 별도의 DTO를 받는다.
     */
    @PostMapping("/api/v2/members")
    public CreateMemberResponse saveMemberV2(@RequestBody @Valid CreateMemberRequest request) {

        // DTO가 memberService에 바로 join 되지 않기 때문에 member에 데이터를 옮긴 후 Member에 join 시켜야한다.
        Member member = new Member();
        member.setName(request.getName());

        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }

    @PutMapping("/api/v2/members/{id}")
    public UpdateMemberResponse updateMemberV2(
            @PathVariable("id") Long id,
            @RequestBody @Valid UpdateMemberRequest request) {
        // @RequestBody를 이용해 요청 시 넘어온 데이터를 업데이트용 DTO인 request에 담는다

        // 업데이트와 조회는 분리하는 것이 좋다
        memberService.update(id, request.getName());
        Member findMember = memberService.findOne(id);

        return new UpdateMemberResponse(findMember.getId(), findMember.getName());
    }

    @Data // 회원 업데이트 시 요청에 사용하는 DTO
    static class UpdateMemberRequest {
        private String name;
    }

    @Data // 회원 업데이트 시 응답에 사용하는 DTO
    @AllArgsConstructor // 모든 필드의 생성자를 만든다
    static class UpdateMemberResponse {
        private Long id;
        private String name;
    }


    @Data // 회원 생성 시 요청에 사용하는 DTO
    static class CreateMemberRequest {
        private String name;
    }

    @Data // 회원 생성 시 응답에 사용하는 DTO
    static class CreateMemberResponse {
        private Long id;

        public CreateMemberResponse(Long id) {
            this.id = id;
        }
    }
}

