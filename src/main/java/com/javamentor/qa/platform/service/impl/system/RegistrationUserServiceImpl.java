package com.javamentor.qa.platform.service.impl.system;

import com.javamentor.qa.platform.converters.UserConverter;
import com.javamentor.qa.platform.dao.abstracts.model.RoleDao;
import com.javamentor.qa.platform.dao.abstracts.model.UserDao;
import com.javamentor.qa.platform.models.dto.UserRegistrationDto;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.abstracts.system.EmailService;
import com.javamentor.qa.platform.service.abstracts.system.RegistrationUserService;
import com.javamentor.qa.platform.service.impl.repository.ReadWriteServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import javax.xml.bind.DatatypeConverter;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.time.LocalDateTime;

@Service
public class RegistrationUserServiceImpl extends ReadWriteServiceImpl<User, Long> implements RegistrationUserService {

    private final UserDao userDao;
    private final RoleDao roleDao;
    private final UserConverter userConverter;
    private final EmailService emailService;
    private final int expirationTime;

    public RegistrationUserServiceImpl(UserDao userDao,
                                       RoleDao roleDao,
                                       UserConverter userConverter,
                                       EmailService emailService,
                                       @Value("${email.expiration_time_in_minutes}") int expirationTime
    ) {
        super(userDao);
        this.userDao = userDao;
        this.roleDao = roleDao;
        this.userConverter = userConverter;
        this.emailService = emailService;
        this.expirationTime = expirationTime;
    }


    @Transactional
    @Override
    public boolean verifyEmail(String email, String code) {
        if (userWithEmailExists(email)) {
            User user = userDao.getByEmail(email).get();

            if (Duration.between(user.getLastUpdateDateTime(), LocalDateTime.now()).toMinutes() > expirationTime) {
                return false;
            }

            if (getMD5Hash(user.getEmail() + user.getPassword()).equals(code)) {
                user.setIsEnabled(true);
                userDao.persist(user);
                return true;
            }
        }
        return false;
    }


    @Transactional
    @Override
    public boolean registerUser(UserRegistrationDto userRegistrationDto) {
        if (userWithEmailExists(userRegistrationDto.getEmail())) {
            return false;
        }


        User newUser = userConverter.userRegistrationDtoToUser(userRegistrationDto);
        //TODO Добавить шифрование пароля после подключения Spring Security
        newUser.setNickname(newUser.getEmail());
        newUser.setRole(roleDao.getByName("USER").get()); //TODO Исправить после добавления реальных ролей в базу
        newUser.setIsEnabled(false);

        //TODO исправить ссылку на страницу реальную страницу
        String verifyCode = getMD5Hash(newUser.getEmail() + newUser.getPassword());

        final String html = getHtml(newUser, verifyCode);

        emailService.sendMessage(newUser.getEmail(), html, "Verify email");
        userDao.persist(newUser);
        return newUser.getId() != 0;
    }

    private boolean userWithEmailExists(String email) {
        return userDao.getByEmail(email).isPresent();
    }

    private static String getHtml(User newUser, String verifyCode) {
        ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
        resolver.setTemplateMode("HTML5");
        resolver.setSuffix(".html");

        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(resolver);

        Context context = new Context();
        context.setVariable("name", newUser.getFullName());
        context.setVariable("email", newUser.getEmail());
        context.setVariable("code", verifyCode);

        return templateEngine.process("templates/mail/email_confirm", context);
    }

    private String getMD5Hash(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes(StandardCharsets.UTF_8));
            return DatatypeConverter.printHexBinary(messageDigest).toUpperCase();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5 должен быть поддержан вашей Java Virtual Machine.", e);
        }
    }
}
