package me.study.studyjpashop.api;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import me.study.studyjpashop.domain.Member;
import me.study.studyjpashop.service.MemberService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

@RestController
@RequiredArgsConstructor
public class MemberApicontroller {

    private final MemberService memberService;

    @PostMapping("/api/v1/members")
    public CreateMemerResponse saveMemberV1(@RequestBody @Valid Member member) {
        Long id = memberService.join(member);
        return new CreateMemerResponse(id);
    }

    @PostMapping("/api/v2/members")
    public CreateMemerResponse saveMemberV2(@RequestBody @Valid CreateMemberRequest request) {
        Member member = new Member();
        member.setName(request.getName());

        Long id = memberService.join(member);
        return new CreateMemerResponse(id);
    }

    @Data
    private class CreateMemberRequest {
        @NotEmpty
        private String name;
    }

    @Data
    static class CreateMemerResponse {
        private Long id;

        public CreateMemerResponse(Long id) {
            this.id = id;
        }
    }


}
