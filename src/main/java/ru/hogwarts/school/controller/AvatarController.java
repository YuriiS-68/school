package ru.hogwarts.school.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.service.impl.AvatarServiceImpl;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;

@RestController
@RequestMapping("avatar")
public class AvatarController {
    private final AvatarServiceImpl avatarService;

    public AvatarController(AvatarServiceImpl avatarService) {
        this.avatarService = avatarService;
    }

    @PostMapping(value = "/{id}/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadAvatar(@PathVariable("id") Long studentId, @RequestParam MultipartFile file) throws IOException{
        avatarService.uploadAvatar(studentId, file);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/{id}/avatar-file")
    public void downloadAvatar(@PathVariable("id") Long studentId, HttpServletResponse response) throws IOException {
        Avatar avatar = avatarService.findAvatarByStudentId(studentId);
        Path path = Path.of(avatar.getFilePath());
        try(InputStream inputStream = Files.newInputStream(path);
            OutputStream outputStream = response.getOutputStream()
        ){
            response.setStatus(200);
            response.setContentType(avatar.getMediaType());
            response.setContentLength((int) avatar.getFileSize());
            inputStream.transferTo(outputStream);
        }
    }

    @GetMapping(value = "/{id}/avatar-preview")
    public ResponseEntity<byte[]> downloadAvatar(@PathVariable("id") Long studentId) {
        Avatar avatar = avatarService.findAvatarByStudentId(studentId);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(avatar.getMediaType()));
        headers.setContentLength(avatar.getData().length);
        return ResponseEntity.status(HttpStatus.OK).headers(headers).body(avatar.getData());
    }

    @GetMapping("/pages-avatars")
    public ResponseEntity<Collection<Avatar>> getAvatarsByPages(@RequestParam("page") Integer pageNumber,
                                                                @RequestParam("size") Integer pageSize){
        Collection<Avatar> avatarCollection = avatarService.getAvatarsByPages(pageNumber, pageSize);

        if (avatarCollection.size() == 0){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(avatarCollection);
    }
}
