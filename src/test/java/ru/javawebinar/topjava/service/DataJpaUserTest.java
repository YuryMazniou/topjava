package ru.javawebinar.topjava.service;

import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles({"datajpa","datajpa_jpa"})
public class DataJpaUserTest extends AbstractUserServiceTest {
}
