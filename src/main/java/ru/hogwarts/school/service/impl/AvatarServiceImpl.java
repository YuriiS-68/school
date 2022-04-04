package ru.hogwarts.school.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.dao.AvatarRepository;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.AvatarService;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Objects;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

@Service
@Transactional
public class AvatarServiceImpl implements AvatarService {

    private static final Logger log = LoggerFactory.getLogger(AvatarServiceImpl.class);

    @Value("${student.avatars.dir.path}")
    private String avatarsDir;

    private final StudentServiceImpl studentService;
    private final AvatarRepository avatarRepository;

    public AvatarServiceImpl(StudentServiceImpl studentService, AvatarRepository avatarRepository) {
        this.studentService = studentService;
        this.avatarRepository = avatarRepository;
    }

    public void uploadAvatar(Long studentId, MultipartFile file) throws IOException {
        log.info("Was invoked method to upload avatar for student: {}", studentId);

        Student student = studentService.findStudent(studentId);
        if (student == null){
            String errorMessage = "Student with id " + studentId + " does not exist.";
            log.error(errorMessage);
            throw new IllegalArgumentException(errorMessage);
        }

        Path filePath = Path.of(avatarsDir, studentId + "." + getExtension(Objects.requireNonNull(file.getOriginalFilename())));
        Files.createDirectories(filePath.getParent());
        Files.deleteIfExists(filePath);

        try(InputStream inputStream = file.getInputStream();
            OutputStream outputStream = Files.newOutputStream(filePath, CREATE_NEW);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream, 1024);
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream, 1024);
        ) {
            bufferedInputStream.transferTo(bufferedOutputStream);
        }

        Avatar avatar = findAvatarByStudentId(studentId);
        avatar.setStudent(student);
        avatar.setFilePath(filePath.toString());
        avatar.setFileSize(file.getSize());
        avatar.setMediaType(file.getContentType());
        avatar.setData(generateImageData(filePath));

        avatarRepository.save(avatar);
    }

    public Avatar findAvatarByStudentId(Long studentId){
        log.info("Was invoked method to find avatar by student id: {}", studentId);

        return avatarRepository.findByStudentId(studentId).orElse(new Avatar());
    }

    public Collection<Avatar> getAvatarsByPages(Integer pageNumber, Integer pageSize){
        log.info("Was invoked method to get avatars by pages: {}, {}", pageNumber, pageSize);

        PageRequest pageRequest = PageRequest.of(pageNumber - 1, pageSize);
        return avatarRepository.findAll(pageRequest).getContent();
    }

    private byte[] generateImageData(Path filePath) throws IOException {
        log.info("Was invoked method to generateImageData: {}", filePath);

        try(InputStream inputStream = Files.newInputStream(filePath);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream, 1024);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ){
            BufferedImage image = ImageIO.read(bufferedInputStream);

            int height = image.getHeight() / (image.getWidth() / 100);
            BufferedImage preview = new BufferedImage(100, height, image.getType());
            Graphics2D graphics2D = preview.createGraphics();
            graphics2D.drawImage(image, 0, 0, 100, height, null);
            graphics2D.dispose();

            ImageIO.write(preview, getExtension(filePath.getFileName().toString()), byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        }
    }

    private String getExtension(String fileName){
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }
}
