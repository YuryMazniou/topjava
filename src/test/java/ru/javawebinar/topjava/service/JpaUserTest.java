package ru.javawebinar.topjava.service;

import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles({"jpa","datajpa_jpa"})
public class JpaUserTest extends AbstractUserServiceTest {
}
