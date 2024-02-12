package com.javamentor.qa.platform.initdb;

import com.javamentor.qa.platform.models.entity.question.Question;
import com.javamentor.qa.platform.models.entity.question.Tag;
import com.javamentor.qa.platform.models.entity.question.answer.Answer;
import com.javamentor.qa.platform.models.entity.user.Role;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.models.entity.user.reputation.Reputation;
import com.javamentor.qa.platform.models.entity.user.reputation.ReputationType;
import com.javamentor.qa.platform.service.abstracts.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class TestDataInitService {

    private final UserService userService;
    private final TagService tagService;
    private final QuestionService questionService;
    private final AnswerService answerService;
    private final ReputationService reputationService;


    @Autowired
    public TestDataInitService(UserService userService, TagService tagService, QuestionService questionService, AnswerService answerService, ReputationService reputationService) {
        this.userService = userService;
        this.tagService = tagService;
        this.questionService = questionService;
        this.answerService = answerService;
        this.reputationService = reputationService;
    }


    @Transactional
    public void createEntity() {
        createUsers();
        userService.persistAll(users);

        createTags();
        tagService.persistAll(tags);

        createQuestions();
        questionService.persistAll(questions);

        createAnswers();
        answerService.persistAll(answers);

        createReputations();
        reputationService.persistAll(reputations);

    }

    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    List<User> users = new ArrayList<>();


    private void createUsers() {
        Role roleAdmin = new Role("ROLE_ADMIN");
        Role roleUser = new Role("ROLE_USER");

        User newAdminUser = new User();
        newAdminUser.setAbout("About admin");
        newAdminUser.setCity("AdminCity");
        newAdminUser.setEmail("admin@yandex.ru");
        newAdminUser.setFullName("Admin admin");
        newAdminUser.setImageLink("images link admin");
        newAdminUser.setLinkGitHub("link gitHub admin");
        newAdminUser.setLinkSite("link site admin");
        newAdminUser.setLinkVk("link vk admin");
        newAdminUser.setNickname("ADMIN");
        newAdminUser.setPassword(passwordEncoder().encode("admin"));
        newAdminUser.setRole(roleAdmin);

        users.add(newAdminUser);

        User newSimpleUser = new User();
        newSimpleUser.setAbout("About user");
        newSimpleUser.setCity("UserCity");
        newSimpleUser.setEmail("user@yandex.ru");
        newSimpleUser.setFullName("User user");
        newSimpleUser.setImageLink("images link user");
        newSimpleUser.setLinkGitHub("link gitHub user");
        newSimpleUser.setLinkSite("link site user");
        newSimpleUser.setLinkVk("link vk user");
        newSimpleUser.setNickname("USER");
        newSimpleUser.setPassword(passwordEncoder().encode("user"));
        newSimpleUser.setRole(roleUser);

        users.add(newSimpleUser);
    }

    List<Tag> tags = new ArrayList<>();

    private void createTags() {
        Tag tagOne = new Tag();

        tagOne.setDescription("Description tagOne");
        tagOne.setName("Name tagOne");
        tags.add(tagOne);

        Tag tagTwo = new Tag();

        tagTwo.setDescription("Description tagTwo");
        tagTwo.setName("Name tagTwo");
        tags.add(tagTwo);
    }

    List<Question> questions = new ArrayList<>();

    private void createQuestions() {

        for (int i = 0; i < 2; i++) {
            Question newQuestion = new Question();
            newQuestion.setDescription("Question description" + i);
            newQuestion.setTitle("Title question" + i);
            if (i < 1) {
                newQuestion.setTags(Collections.singletonList(tags.get(0)));
                newQuestion.setUser(users.get(0));
            } else {
                newQuestion.setTags(Collections.singletonList(tags.get(1)));
                newQuestion.setUser(users.get(1));
            }
            questions.add(newQuestion);
        }
    }

    List<Answer> answers = new ArrayList<>();

    private void createAnswers() {

        for (int i = 0; i < 2; i++) {
            Answer newAnswer = new Answer();
            newAnswer.setHtmlBody("Body answer" + i);
            if (i < 1) {
                newAnswer.setUser(users.get(0));
                newAnswer.setQuestion(questions.get(0));
            } else {
                newAnswer.setUser(users.get(1));
                newAnswer.setQuestion(questions.get(1));
            }
            newAnswer.setIsDeleted(false);
            newAnswer.setIsHelpful(true);
            newAnswer.setIsDeletedByModerator(false);
            newAnswer.setDateAcceptTime(LocalDateTime.now());
            answers.add(newAnswer);
        }

    }

    List<Reputation> reputations = new ArrayList<>();

    private void createReputations() {

        Reputation reputation = new Reputation();

        reputation.setAuthor(users.get(0));
        reputation.setSender(users.get(1));
        reputation.setQuestion(questions.get(1));
        reputation.setCount(1);
        reputation.setType(ReputationType.Question);
        reputations.add(reputation);

        Reputation reputation1 = new Reputation();

        reputation1.setAuthor(users.get(1));
        reputation1.setSender(users.get(0));
        reputation1.setAnswer(answers.get(1));
        reputation1.setCount(1);
        reputation1.setType(ReputationType.Answer);
        reputations.add(reputation1);
    }
}
