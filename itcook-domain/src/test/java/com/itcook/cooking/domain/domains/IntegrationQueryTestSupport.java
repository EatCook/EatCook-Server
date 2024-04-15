package com.itcook.cooking.domain.domains;

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
//@Import(DomainTestQuerydslConfiguration.class)
public abstract class IntegrationQueryTestSupport {

}
