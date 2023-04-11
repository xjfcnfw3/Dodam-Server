package com.example.dodam.controller.user;

import com.example.dodam.common.fileupload.service.ProfileUploadService;
import com.example.dodam.config.auth.MemberDetails;
import com.example.dodam.domain.member.UpdateMemberRequest;
import com.example.dodam.domain.member.Member;
import com.example.dodam.domain.member.MemberResponse;
import com.example.dodam.service.member.MemberService;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final MemberService memberService;
    private final ProfileUploadService fileUploadService;

    @GetMapping
    public ResponseEntity<MemberResponse> getUser(Authentication authentication) {
        return ResponseEntity.ok(MemberResponse.of(getPrincipalUser(authentication)));
    }

    @PutMapping
    public Object updateUser(@ModelAttribute UpdateMemberRequest request, Authentication authentication) throws IOException {
        Member member = getPrincipalUser(authentication);
        String imagePath = fileUploadService.storeFile(request.getProfileImage());
        request.setImgPath(imagePath);
        fileUploadService.deleteFile(member.getImgPath());
        memberService.update(member.getEmail(), request);
        return Map.of("result", "성공");
    }

    @DeleteMapping
    public Object deleteUser(Authentication authentication) {
        memberService.delete(getPrincipalUser(authentication).getId());
        return Map.of("result", "성공");
    }


    @GetMapping(value = "/images/{filename}", produces = MediaType.IMAGE_JPEG_VALUE)
    public Resource downloadImage(@PathVariable String filename) throws
        MalformedURLException {
        return new UrlResource("file:" + fileUploadService.getFullPath(filename));
    }

    @DeleteMapping("/images")
    public Object deleteImage(Authentication authentication) {
        Member member = getPrincipalUser(authentication);
        memberService.deleteImage(member.getId());
        fileUploadService.deleteFile(member.getImgPath());
        return Map.of("result", "성공");
    }

    private Member getPrincipalUser(Authentication authentication) {
        MemberDetails principal = (MemberDetails) authentication.getPrincipal();
        return principal.getUser();
    }
}
